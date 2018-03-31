package ru.pussy_penetrator.randomcounter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Sex_predator on 09.12.2016.
 */
public class RandomActivity extends SingleFragmentActivity {

    private static final String EXTRA_RANDOM    = "ru.pussy_penetrator.randomcounter.random_number";
    private static final String EXTRA_MAX_BOUND = "ru.pussy_penetrator.randomcounter.random_max_bound";

    public static Intent newIntent(Context context, int randomNumber, int randomMaxBound) {
        Intent intent = new Intent(context, RandomActivity.class);
        intent.putExtra(EXTRA_RANDOM, randomNumber);
        intent.putExtra(EXTRA_MAX_BOUND, randomMaxBound);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int random = getIntent().getIntExtra(EXTRA_RANDOM, 1);
        int maxBound = getIntent().getIntExtra(EXTRA_MAX_BOUND, 2);
        return RandomFragment.newInstance(random, maxBound);
    }
}
