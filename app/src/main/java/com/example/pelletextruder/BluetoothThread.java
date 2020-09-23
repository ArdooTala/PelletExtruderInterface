package com.example.pelletextruder;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothThread extends Thread {
    private static final char DELIMITER = '\n';

    private String address;
    private BluetoothSocket socket;
    private OutputStream outStream;

    private Handler readHandler;
    private Handler writeHandler;

    BufferedReader in;

    private interface MessageConstants {
        int MESSAGE_READ = 1;
        int MESSAGE_WRITE = 2;
        int MESSAGE_TOAST = 3;

        // ... (Add other message types here as needed.)
    }

    public BluetoothThread(String _address, Handler handler) {
        this.address = _address.toUpperCase();
        this.readHandler = handler;
        this.writeHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                write((String) message.obj);
            }
        };
    }

    public Handler getWriteHandler() {
        return this.writeHandler;
    }

    private void connect(String address) throws Exception {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if ((adapter == null) || !adapter.isEnabled()) {
            throw new Exception("Bluetooth Not Enabled!");
        }

        BluetoothDevice extruder = adapter.getRemoteDevice(address);
        socket = extruder.createRfcommSocketToServiceRecord(UUID.fromString(extruder.getUuids()[0].toString()));
        adapter.cancelDiscovery();
        socket.connect();

        outStream = socket.getOutputStream();
        InputStream inStream = socket.getInputStream();
        in = new BufferedReader(new InputStreamReader(inStream));
    }

    private void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String read() throws IOException {
        String s = in.readLine();
//        Log.d("Received", s);
        return s;
    }

    private void write(String s) {
        s += DELIMITER;
        try {
            outStream.write(s.getBytes());
            Log.i("Sending", s);
            sendToHandler(s, MessageConstants.MESSAGE_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("WRITE ERROR -> ", e.toString());
        }
    }

    void sendToHandler(String _msg, int type) {
        readHandler.obtainMessage(type, _msg).sendToTarget();
    }

    public void run() {
        try {
            connect(this.address);
            sendToHandler("Connection Established Successfully.", MessageConstants.MESSAGE_TOAST);
        } catch (Exception e) {
            Log.e("CONNECTION ERROR -> ", e.toString());
            sendToHandler("Connection Failed.", MessageConstants.MESSAGE_TOAST);
            disconnect();
            return;
        }

        while (true) {
            try {
                String readStr = read();
                sendToHandler(readStr, MessageConstants.MESSAGE_READ);
            } catch (IOException e) {
                Log.e("READ ERROR -> ", e.toString());
                sendToHandler("Failed to Receive Data from Bluetooth", MessageConstants.MESSAGE_TOAST);
                break;
            }
        }

        disconnect();
    }
}
