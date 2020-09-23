package com.example.pelletextruder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TempControl#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TempControl extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int[] tmpTextID = {R.id.tmp0, R.id.tmp1, R.id.tmp2, R.id.tmp3};
    int[] tmpActualID = {R.id.tmpActual0, R.id.tmpActual1, R.id.tmpActual2, R.id.tmpActual3};
    int[] intActualID = {R.id.intensityText0, R.id.intensityText1, R.id.intensityText2, R.id.intensityText3};
    int[] tmpDisplayID = {R.id.tmpDisplay0, R.id.tmpDisplay1, R.id.tmpDisplay2, R.id.tmpDisplay3};
    int[] intDisplayID = {R.id.intensityDisplay0, R.id.intensityDisplay1, R.id.intensityDisplay2, R.id.intensityDisplay3};
    int[] heaterSwitchID = {R.id.heaterSwitch0, R.id.heaterSwitch1, R.id.heaterSwitch2, R.id.heaterSwitch3};
    int[] tmpControlID = {R.id.tmpControl0, R.id.tmpControl1, R.id.tmpControl2, R.id.tmpControl3};

    TextView[] tmpText = new TextView[4];
    TextView[] tmpAct = new TextView[4];
    TextView[] intAct = new TextView[4];
    SeekBar[] tmpInput = new SeekBar[4];
    Button applyButton;
    Switch[] hState = new Switch[4];
    ProgressBar[] tmpDisplay = new ProgressBar[4];
    ProgressBar[] intDisplay = new ProgressBar[4];
    private SharedViewModel sharedViewModel;

    public TempControl() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TempControl.
     */
    // TODO: Rename and change types and number of parameters
    public static TempControl newInstance(String param1, String param2) {
        TempControl fragment = new TempControl();
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
        return inflater.inflate(R.layout.fragment_temp_control, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);

        applyButton = view.findViewById(R.id.applyTmpButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int j = 0; j < 4; j++) {
                    ((HomeActivity) getActivity()).sendToBT(formatCommand('s', j, tmpInput[j].getProgress()));
                }
            }
        });

        for (int hi = 0; hi < 4; hi++) {

            final int finalHi = hi;

            tmpText[hi] = view.findViewById(tmpTextID[hi]);

            tmpAct[hi] = view.findViewById(tmpActualID[hi]);
            intAct[hi] = view.findViewById(intActualID[hi]);

            hState[hi] = view.findViewById(heaterSwitchID[hi]);

            tmpDisplay[hi] = view.findViewById(tmpDisplayID[hi]);
            sharedViewModel.getT(hi).observe(this, new Observer<Float>() {
                @Override
                public void onChanged(@Nullable Float s) {
                    if (s != null) {
                        tmpDisplay[finalHi].setProgress(s.intValue());
                        tmpAct[finalHi].setText(s.toString());
                    }
                    else {
                        tmpDisplay[finalHi].setProgress(80);
                    }
                }
            });

            intDisplay[hi] = view.findViewById(intDisplayID[hi]);
            sharedViewModel.getV(hi).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer s) {
                    if (s != null) {
                        intDisplay[finalHi].setProgress(s);
                        intAct[finalHi].setText("i: " + s.toString() + "%");
                    }
                    else {
                        intDisplay[finalHi].setProgress(80);
                    }
                }
            });

            tmpInput[hi] = getView().findViewById(tmpControlID[hi]);

            tmpInput[hi].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b)
                {
                    tmpText[finalHi].setText("" + i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    private String formatCommand(char c, int id, int val) {
        if (hState[id].isChecked()) {
            return "" + c + id + val;
        }
        else {
            return "" + c + id + "0";
        }
    }
}