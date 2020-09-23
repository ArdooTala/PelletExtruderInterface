package com.example.pelletextruder;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private Handler writeHandler;
    BluetoothThread bt;

    SharedViewModel model;
    FloatingActionButton dlConfig;
    ProgressBar connectionProgress;
    GraphView graph;
    List<LineGraphSeries<tempPoint>> tempGraphs = new ArrayList<>();
    int[] graphColors = {Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewPager viewpager = findViewById(R.id.viewPager);

        PagerAdapter pagerAdapter = new ControlTabsPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        model = ViewModelProviders.of(this).get(SharedViewModel.class);

        TabLayout tabs = findViewById(R.id.controlTabs);
        tabs.setupWithViewPager(viewpager);

        dlConfig = findViewById(R.id.DownloadConfig);

        connectionProgress = findViewById(R.id.ConnectionProgress);

        graph = findViewById(R.id.graph);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(false);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(500);
        graph.getGridLabelRenderer().setNumHorizontalLabels(0);
        graph.getGridLabelRenderer().setNumVerticalLabels(10);
        graph.getViewport().setDrawBorder(false);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.LTGRAY);
        graph.getGridLabelRenderer().setHighlightZeroLines(false);

        for (int ti = 0; ti < 5; ti++){
            tempGraphs.add(new LineGraphSeries<>(new tempPoint[] {
                    new tempPoint(ti, 0),
                    new tempPoint(ti, 0)
            }));
            tempGraphs.get(ti).setTitle("Heater #" + ti);
            tempGraphs.get(ti).setColor(graphColors[ti]);
            tempGraphs.get(ti).setThickness(2);
            graph.addSeries(tempGraphs.get(ti));
        }


        Intent intent = getIntent();
        String id = intent.getStringExtra("btAddress");
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(id);

        Handler readHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String recMsg = (String) msg.obj;
                switch (msg.what) {
                    case MessageConstants.MESSAGE_READ: {
                        if (recMsg.contains("t")) {
                            int index = Integer.parseInt(recMsg.charAt(1) + "");
                            float _t = Float.parseFloat(recMsg.substring(3));
                            tempGraphs.get(index).appendData(
                                    new tempPoint(index, _t),
                                    true, 500, false);
                            model.setT(index, _t);
                        }
                        else if (recMsg.contains("v")) {
                            int index = Integer.parseInt(recMsg.charAt(1) + "");
                            if (index < 4) model.setV(index, Integer.parseInt(recMsg.substring(3)));
                        }
                        else if (recMsg.contains("s")) {
                            int index = Integer.parseInt(recMsg.charAt(1) + "");
                            model.setS(index, Float.parseFloat(recMsg.substring(3)));
                        }
                        else if (recMsg.contains("p")) {
                            int index = Integer.parseInt(recMsg.charAt(1) + "");
                            model.setP(index, Float.parseFloat(recMsg.substring(3)));
                        }
                        else if (recMsg.contains("i")) {
                            int index = Integer.parseInt(recMsg.charAt(1) + "");
                            model.setI(index, Float.parseFloat(recMsg.substring(3)));
                        }
                        else if (recMsg.contains("d")) {
                            int index = Integer.parseInt(recMsg.charAt(1) + "");
                            model.setD(index, Float.parseFloat(recMsg.substring(3)));
                        }
                        else if (recMsg.contains("m")) {
                            switch (recMsg.charAt(1)) {
                                case 's':
                                    // TODO
                                    break;
                                case 'r':
                                    // TODO
                                    break;
                                case 'l':
                                    //TODO
                                    break;
                                default:
                                    break;
                            }
                        }
                        else if (recMsg.contains("f")) {
                            model.setHF(Integer.parseInt(recMsg.substring(3)));
                        }
                        break;
                    }
                    case MessageConstants.MESSAGE_TOAST: {
                        if (recMsg.contains("Successfully")) connectionProgress.setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(), recMsg, Toast.LENGTH_LONG).show();
                        break;
                    }
                    case MessageConstants.MESSAGE_WRITE: {
//                        Toast.makeText(getBaseContext(), recMsg, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        break;
                }
            }
        };

        bt = new BluetoothThread(device.getAddress(), readHandler);
        writeHandler = bt.getWriteHandler();
        bt.setDaemon(true);
        bt.start();
    }

    void sendToBT(String s) {
        Message.obtain(writeHandler, MessageConstants.MESSAGE_WRITE, s).sendToTarget();
    }

    public void downloadConfig(View view) {
        sendToBT("l");
    }

    private interface MessageConstants {
        int MESSAGE_READ = 1;
        int MESSAGE_WRITE = 2;
        int MESSAGE_TOAST = 3;
    }
}