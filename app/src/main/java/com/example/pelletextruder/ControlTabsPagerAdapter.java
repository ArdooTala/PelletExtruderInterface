package com.example.pelletextruder;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ControlTabsPagerAdapter extends FragmentPagerAdapter {
    public ControlTabsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position){
        switch(position) {
            case 0:
                return new MotorControl();
            case 1:
                return new CoolingControl();
            case 2:
                return new TempControl();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch(position) {
            case 0:
                return "FEEDRATE";
            case 1:
                return "COOLING";
            case 2:
                return "TEMPERATURE";
            default:
                return null;
        }
    }
}
