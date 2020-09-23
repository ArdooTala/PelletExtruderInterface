package com.example.pelletextruder;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    TextView statusText;
    LinearLayout logView;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice extruder;
    ProgressBar btProgBar;
    Button connectButt;
    ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.progress_textView);
        logView = findViewById(R.id.log_view);
        connectButt = findViewById(R.id.btConnect);
        btProgBar = findViewById(R.id.bt_prog_bar);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btProgBar.setVisibility(View.INVISIBLE);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.BLUETOOTH},
                    0);
        }

        assert bluetoothAdapter != null;
        if (!bluetoothAdapter.isEnabled()) {
            btProgBar.setVisibility(View.VISIBLE);
            statusText.setText(R.string.bt_disabled);
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 8);
        }
        else {
            btProgBar.setVisibility(View.INVISIBLE);
            statusText.setText(R.string.bt_Enabled);
        }
    }

    private void logToScreen(String _msg, boolean highlight) {
        TextView logMessage = new TextView(this);
        logMessage.setText(_msg);
        logMessage.setTextSize(10);
        logMessage.setGravity(Gravity.CENTER);
        logMessage.setTextColor(getResources().getColor(R.color.midGray));
        logMessage.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        if (highlight) logMessage.setBackgroundColor(getResources().getColor(R.color.lightGray));
        logView.addView(logMessage);
    }
    private void logToScreen(String _msg) {
        logToScreen(_msg, false);
    }

    public void connectToBT (View view) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        btProgBar.setVisibility(View.VISIBLE);
        if (pairedDevices.size() > 0) {
            statusText.setText(R.string.bt_searching);
            logToScreen("<Paired Bluetooth Devices>");
            for (final BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                if (deviceName.contains("COMMAND")){
                    logToScreen(deviceName, true);
                    statusText.setText(R.string.bt_found);
                    connectButt.setText(R.string.start);
                    connectButt.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            startExtruder(device);
                        }
                    });
                    extruder = device;
                    for (int i = 0; i < extruder.getUuids().length; i++) {
                        logToScreen("UUID: " + extruder.getUuids()[i].toString());
                    }
                    break;
                }
                else{
                    logToScreen(deviceName);
                }
            }
            if (extruder != null) {
                statusText.setText(R.string.bt_found);
                btProgBar.setVisibility(View.INVISIBLE);
            }
            else {
                statusText.setText(R.string.bt_not_paired);
                searchForBT();
            }
        }
        else {
            statusText.setText(R.string.bt_not_paired);
            searchForBT();
        }
    }

    public void startExtruder (BluetoothDevice device) {
        btProgBar.setVisibility(View.VISIBLE);
        statusText.setText(R.string.starting);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("btAddress", device.getAddress());
        startActivity(intent);
    }

    public void searchForBT() {
        btProgBar.setVisibility(View.VISIBLE);

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        boolean discoveryResult = bluetoothAdapter.startDiscovery();
        if(discoveryResult) {
            statusText.setText(R.string.bt_searching);
        }
        else {
            statusText.setText(R.string.bt_error);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            btProgBar.setVisibility(View.INVISIBLE);
            statusText.setText(R.string.bt_Enabled);
        }else{
            btProgBar.setVisibility(View.INVISIBLE);
            statusText.setText(R.string.bt_Disabled);
            connectButt.setEnabled(false);
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                discoveredDevices.add(device);
                String deviceName = device.getName();

                if (deviceName != null) {
                    if (deviceName.contains("COMMAND")) {
                        bluetoothAdapter.cancelDiscovery();
                        logToScreen(deviceName, true);
                        btProgBar.setVisibility(View.INVISIBLE);
                        statusText.setText(R.string.bt_found);
                        connectButt.setText(R.string.start);
                        connectButt.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                startExtruder(device);
                            }
                        });
                        extruder = device;
                    } else {
                        String deviceHardwareAddress = device.getAddress(); // MAC address
                        logToScreen(deviceName + ": " + deviceHardwareAddress);
                    }
                }
            }
//            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                btProgBar.setVisibility(View.INVISIBLE);
//                statusText.setText(R.string.bt_Ready);
//                for (BluetoothDevice dev:
//                     discoveredDevices) {
//                    String deviceName = dev.getName();
//                    String deviceHardwareAddress = dev.getAddress(); // MAC address
//                    logToScreen(deviceName + ":: " + deviceHardwareAddress);
//                }
//            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }
}


