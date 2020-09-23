package com.example.pelletextruder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoolingControl#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoolingControl extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView hsfValue, nfValue, hsTmpAct;
    SeekBar nfInput, hsfInput;
    ProgressBar hsfDisplay;
    Button applyButton;

    public CoolingControl() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoolingControl.
     */
    // TODO: Rename and change types and number of parameters
    public static CoolingControl newInstance(String param1, String param2) {
        CoolingControl fragment = new CoolingControl();
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
        return inflater.inflate(R.layout.fragment_cooling_control, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nfValue = view.findViewById(R.id.NFValue);
        nfInput = view.findViewById(R.id.NozzleFanInput);
        nfInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nfValue.setText(i + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        hsfValue = view.findViewById(R.id.HSFValue);
        hsfInput = view.findViewById(R.id.HeatSinkFanInput);
        hsfInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                hsfValue.setText(i + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        hsTmpAct = view.findViewById(R.id.HSTmpAct);
        hsfDisplay = view.findViewById(R.id.HeatSinkFanDisplay);
        SharedViewModel sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getHT().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(@Nullable Float s) {
                if (s != null) {
                    hsfDisplay.setProgress(s.intValue());
                }
                else {
                    hsfDisplay.setProgress(0);
                }
            }
        });
        sharedViewModel.getT(4).observe(this, new Observer<Float>() {
            @Override
            public void onChanged(@Nullable Float s) {
                if (s != null) {
                    hsTmpAct.setText("Temperature: " + s.toString() + " (C)");
                }
            }
        });

        applyButton = view.findViewById(R.id.applyCooling);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyFanConfig();
            }
        });
    }

    private void applyFanConfig() {
        ((HomeActivity) getActivity()).sendToBT("f1" + (int) ((float)nfInput.getProgress() * 2.55));
    }
}