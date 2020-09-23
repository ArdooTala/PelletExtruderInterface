package com.example.pelletextruder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MotorControl#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MotorControl extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView rpmVal;
    SeekBar rpmInput;
    ToggleButton motorState;
    Switch enabled, direction;
    Button applyButton;

    public MotorControl() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MotorControl.
     */
    public static MotorControl newInstance(String param1, String param2) {
        MotorControl fragment = new MotorControl();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_motor_control, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rpmVal = view.findViewById(R.id.rpmValue);
        rpmInput = view.findViewById(R.id.rpmInput);
        rpmInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                rpmVal.setText(i + " RPM");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rpmVal.setText(rpmInput.getProgress() + " RPM");

        motorState = view.findViewById(R.id.motorState);
        motorState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeMotorState(b);
            }
        });

        enabled = view.findViewById(R.id.motorEnable);

        direction = view.findViewById(R.id.motorDirection);

        applyButton = view.findViewById(R.id.applyMotorConfig);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyConfig();
            }
        });
    }

    void changeMotorState(boolean b) {
        if (b) ((HomeActivity) getActivity()).sendToBT("ms1");
        else ((HomeActivity) getActivity()).sendToBT("ms0");
    }

    void applyConfig() {
        ((HomeActivity) getActivity()).sendToBT("md" + (direction.isChecked() ? "0" : "1"));
        ((HomeActivity) getActivity()).sendToBT("me" + (enabled.isChecked() ? "0" : "1"));
        if (rpmInput.getProgress() > 2) ((HomeActivity) getActivity()).sendToBT("mr" + 2000000 / (rpmInput.getProgress() * 800 / 60));
        else Toast.makeText(((HomeActivity) getActivity()).getBaseContext(), "RPM too low", Toast.LENGTH_LONG).show();
    }
}