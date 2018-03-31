package ru.pussy_penetrator.randomcounter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import ru.pussy_penetrator.randomcounter.firework.FireworkLayout;

/**
 * Created by Sex_predator on 09.12.2016.
 */
public class RandomFragment extends Fragment {

    private static final String ARG_RANDOM    = "random_number";
    private static final String ARG_MAX_BOUND = "random_max_bound";

    private RandomView     mRandomView;
    private FireworkLayout mFireworkLayout;

    private int mRandomNumber;
    private int mRandomMaxBound;

    private boolean mAnimationEnded = false;

    public static RandomFragment newInstance(int randomNumber, int randomMaxBound) {
        Bundle args = new Bundle();
        args.putInt(ARG_RANDOM, randomNumber);
        args.putInt(ARG_MAX_BOUND, randomMaxBound);
        RandomFragment fragment = new RandomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRandomNumber = getArguments().getInt(ARG_RANDOM);
        mRandomMaxBound = getArguments().getInt(ARG_MAX_BOUND);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.random_number, container, false);

        mRandomView = (RandomView) view.findViewById(R.id.random_number_canvas);
        mRandomView.setRandomNumber(mRandomNumber, mRandomMaxBound);
        mRandomView.setKeepScreenOn(true);
        final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.appear);

        MusicPlayerLab.getPlayer().play(getActivity(), R.raw.appear);
        anim.setDuration(MusicPlayerLab.getPlayer().getDuration());
        mRandomView.startAnimation(anim);

        mFireworkLayout = (FireworkLayout) view.findViewById(R.id.random_number_firework);
        mRandomView.setAnimationListener(new RandomView.AnimationListener() {
            @Override
            public void onAnimationEnd() {
                mFireworkLayout.startFirework();
                mAnimationEnded = true;

                //start firework soundtrack
                MusicPlayerLab.getPlayer().play(getActivity(), R.raw.firework);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1_000 + anim.getDuration() + anim.getStartOffset());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MusicPlayerLab.getPlayer().play(getActivity(), R.raw.volchok);
                            mRandomView.setAnimationTime(MusicPlayerLab.getPlayer().getDuration());
                            mRandomView.setMinSpinCount(200);
                            mRandomView.startAnimation();
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAnimationEnded)
            mFireworkLayout.startFirework();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicPlayerLab.getPlayer().release();
    }
}
