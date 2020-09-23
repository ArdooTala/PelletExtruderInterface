package com.example.pelletextruder;

import com.jjoe64.graphview.series.DataPointInterface;

public class tempPoint implements DataPointInterface {
    private int x;
    private float y;
    static private int[] _x = {0, 0, 0, 0, 0};

    public tempPoint (int i, float t) {
        x = _x[i];
        _x[i]++;
        y = t;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }
}
