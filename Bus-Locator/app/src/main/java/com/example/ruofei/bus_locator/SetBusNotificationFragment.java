package com.example.ruofei.bus_locator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ruofeixu on 7/3/16.
 */
public class SetBusNotificationFragment extends DialogFragment {
    private SeekBar seekBar;
    private TextView textView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle("set notification dialog title");
        View view = inflater.inflate(R.layout.fragment_set_bus_notification_dialog, null);
        builder.setView(view);

        initializeVariables(view);
        builder.setMessage("dialog message")
                .setPositiveButton("button 1", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Ok
                    }
                })
                .setNegativeButton("button 2", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cancel
                    }
                });
        // Create the AlertDialog object and return it


        textView.setText("Covered: " + seekBar.getProgress() + "/" + seekBar.getMax());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                Toast.makeText(getActivity(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getActivity(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("Covered: " + progress + "/" + seekBar.getMax());
                Toast.makeText(getActivity(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }

    // A private method to help us initialize our variables.
    private void initializeVariables(View view) {
        seekBar = (SeekBar) view.findViewById(R.id.timeSeekBar);
        textView = (TextView) view.findViewById(R.id.timeText);
    }

}
