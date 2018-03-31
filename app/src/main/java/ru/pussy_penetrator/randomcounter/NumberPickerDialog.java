package ru.pussy_penetrator.randomcounter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by Sex_predator on 07.12.2016.
 */
public class NumberPickerDialog extends DialogFragment {

    public static final String EXTRA_NUMBER = "ru.pussy_penetrator.randomcounter.dialog_number";

    private static final String ARG_MAX_VALUE = "max_value";
    private static final String ARG_MIN_VALUE = "min_value";
    private static final String ARG_VALUE     = "value";

    private int          mMinValue;
    private int          mMaxValue;
    private int          mValue;
    private NumberPicker mNumberPicker;

    public static NumberPickerDialog newInstance(int minValue, int maxValue, int value) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_MIN_VALUE, minValue);
        bundle.putInt(ARG_MAX_VALUE, maxValue);
        bundle.putInt(ARG_VALUE, value);

        NumberPickerDialog dialog = new NumberPickerDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMinValue = getArguments().getInt(ARG_MIN_VALUE);
        mMaxValue = getArguments().getInt(ARG_MAX_VALUE);
        mValue = getArguments().getInt(ARG_VALUE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_number_picker, null);
        mNumberPicker = (NumberPicker) view.findViewById(R.id.dialog_number_picker);
        mNumberPicker.setMinValue(mMinValue);
        mNumberPicker.setMaxValue(mMaxValue);
        mNumberPicker.setValue(mValue);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_change_number_message);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getTargetFragment() == null)
                    return;

                int result = mNumberPicker.getValue();
                Intent intent = new Intent();
                intent.putExtra(EXTRA_NUMBER, result);
                getTargetFragment()
                        .onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }
}
