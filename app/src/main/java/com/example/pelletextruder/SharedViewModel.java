package com.example.pelletextruder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private List<MutableLiveData<Float>> t = new ArrayList<>();
    private List<MutableLiveData<Integer>> v = new ArrayList<>();

    private List<MutableLiveData<Float>> s = new ArrayList<>();
    private List<MutableLiveData<Float>> p = new ArrayList<>();
    private List<MutableLiveData<Float>> i = new ArrayList<>();
    private List<MutableLiveData<Float>> d = new ArrayList<>();

    private MutableLiveData<Integer> hFan;
    private MutableLiveData<Float> heatSinkTmp;
    private MutableLiveData<Integer> rpm;


    public LiveData<Float> getT(int index) {
        if (t.size() == 0) {
            for (int i = 0; i < 5; i++) {
                t.add(new MutableLiveData<Float>());
            }
        }
        if (t.get(index) == null) {
            t.set(index, new MutableLiveData<Float>());
            t.get(index).postValue(0f);
        }
        return t.get(index);
    }

    public void setT(int index, float _t) {
        if (t.size() == 0) {
            for (int i = 0; i < 5; i++) {
                t.add(new MutableLiveData<Float>());
            }
        }
        if (t.get(index) == null) {
            t.set(index, new MutableLiveData<Float>());
        }
        t.get(index).setValue(_t);
    }

    public LiveData<Integer> getV(int index) {
        if (v.size() == 0) {
            for (int i = 0; i < 4; i++) {
                v.add(new MutableLiveData<Integer>());
            }
        }
        if (v.get(index) == null) {
            v.set(index, new MutableLiveData<Integer>());
            v.get(index).postValue(0);
        }
        return v.get(index);
    }

    public void setV(int index, int _v) {
        if (v.size() == 0) {
            for (int i = 0; i < 4; i++) {
                v.add(new MutableLiveData<Integer>());
            }
        }
        if (v.get(index) == null) {
            v.set(index, new MutableLiveData<Integer>());
        }
        v.get(index).setValue(_v);
    }

    public LiveData<Float> getS(int index) {
        if (s.size() == 0) {
            for (int i = 0; i < 5; i++) {
                s.add(new MutableLiveData<Float>());
            }
        }
        if (s.get(index) == null) {
            s.set(index, new MutableLiveData<Float>());
            s.get(index).postValue(0f);
        }
        return s.get(index);
    }

    public void setS(int index, float _t) {
        if (s.size() == 0) {
            for (int i = 0; i < 5; i++) {
                s.add(new MutableLiveData<Float>());
            }
        }
        if (s.get(index) == null) {
            s.set(index, new MutableLiveData<Float>());
        }
        s.get(index).setValue(_t);
    }

    public LiveData<Float> getP(int index) {
        if (p.size() == 0) {
            for (int i = 0; i < 5; i++) {
                p.add(new MutableLiveData<Float>());
            }
        }
        if (p.get(index) == null) {
            p.set(index, new MutableLiveData<Float>());
            p.get(index).postValue(0f);
        }
        return t.get(index);
    }

    public void setP(int index, float _t) {
        if (p.size() == 0) {
            for (int i = 0; i < 5; i++) {
                p.add(new MutableLiveData<Float>());
            }
        }
        if (p.get(index) == null) {
            p.set(index, new MutableLiveData<Float>());
        }
        p.get(index).setValue(_t);
    }

    public LiveData<Float> getI(int index) {
        if (i.size() == 0) {
            for (int j = 0; j < 5; j++) {
                i.add(new MutableLiveData<Float>());
            }
        }
        if (i.get(index) == null) {
            i.set(index, new MutableLiveData<Float>());
            i.get(index).postValue(0f);
        }
        return i.get(index);
    }

    public void setI(int index, float _t) {
        if (i.size() == 0) {
            for (int j = 0; j < 5; j++) {
                i.add(new MutableLiveData<Float>());
            }
        }
        if (i.get(index) == null) {
            i.set(index, new MutableLiveData<Float>());
        }
        i.get(index).setValue(_t);
    }

    public LiveData<Float> getD(int index) {
        if (d.size() == 0) {
            for (int i = 0; i < 5; i++) {
                d.add(new MutableLiveData<Float>());
            }
        }
        if (d.get(index) == null) {
            d.set(index, new MutableLiveData<Float>());
            d.get(index).postValue(0f);
        }
        return d.get(index);
    }

    public void setD(int index, float _t) {
        if (d.size() == 0) {
            for (int i = 0; i < 5; i++) {
                d.add(new MutableLiveData<Float>());
            }
        }
        if (d.get(index) == null) {
            d.set(index, new MutableLiveData<Float>());
        }
        d.get(index).setValue(_t);
    }

    public LiveData<Integer> getHF() {
        if (hFan == null) {
            hFan = new MutableLiveData<>();
            hFan.postValue(0);
        }
        return hFan;
    }

    public void setHF(int _v) {
        if (hFan == null) {
            hFan = new MutableLiveData<>();
        }
        hFan.setValue(_v);
    }

    public LiveData<Float> getHT() {
        if (heatSinkTmp == null) {
            heatSinkTmp = new MutableLiveData<Float>();
            heatSinkTmp.postValue(0f);
        }
        return heatSinkTmp;
    }

    public void setHT(float _t) {
        if (heatSinkTmp == null) {
            heatSinkTmp = new MutableLiveData<Float>();
        }
        heatSinkTmp.setValue(_t);
    }
}
