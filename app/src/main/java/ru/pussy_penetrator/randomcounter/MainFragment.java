package ru.pussy_penetrator.randomcounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Sex_predator on 07.12.2016.
 */
public class MainFragment extends Fragment {

    private final int MIN_NUMBER_VALUE = 2;
    private final int MAX_NUMBER_VALUE = 99;
    private       int mNumberValue     = MAX_NUMBER_VALUE;

    private TextView mText;
    private TextView mHintText;
    private Button   mStartButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

        if (savedInstanceState != null)
            mNumberValue = savedInstanceState.getInt("value", 999);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_change_number:
                requestDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case 0: //dialog
                mNumberValue = data.getIntExtra(NumberPickerDialog.EXTRA_NUMBER, mNumberValue);
                updateText();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("value", mNumberValue);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main, container, false);

        mText = (TextView) view.findViewById(R.id.main_text);
        mStartButton = (Button) view.findViewById(R.id.main_start_button);
        mHintText = (TextView) view.findViewById(R.id.main_hint_text);

        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog();
            }
        });
        mHintText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog();
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                Intent intent = RandomActivity
                        .newIntent(getActivity(), random.nextInt(mNumberValue) + 1, mNumberValue);
                startActivity(intent);
            }
        });

        updateText();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.blink);
        mText.startAnimation(anim);
        mHintText.startAnimation(anim);
    }

    private void updateText() {
        mText.setText("1..." + mNumberValue);
    }

    private void requestDialog() {
        NumberPickerDialog dialog = NumberPickerDialog
                .newInstance(MIN_NUMBER_VALUE, MAX_NUMBER_VALUE, mNumberValue);
        dialog.setTargetFragment(MainFragment.this, 0);
        dialog.show(getFragmentManager(), "NUMBER_DIALOG");
    }
}
