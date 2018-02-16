package com.andersenlab.boilerplate.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.andersenlab.boilerplate.R;
import com.andersenlab.boilerplate.customview.FloatingBottomSheet;
import com.andersenlab.boilerplate.fragment.ImagesFragment;

import butterknife.BindView;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    @BindView(R.id.fbs_main_add_item) FloatingBottomSheet addItem;

    public interface OnAddItemClickListener {
        void addItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_main,
                            ImagesFragment.newInstance(ImagesFragment.LoadingRepository.LOAD_FROM_REALM))
                    .commit();
        }

        addItem.setOnClickListener(this::onAddItemClicked);
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.app_name);
    }

    private void onAddItemClicked(View view) {
        Timber.i("Tap on add item button");
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
        if (currentFragment instanceof OnAddItemClickListener) {
            ((OnAddItemClickListener) currentFragment).addItem();
        }
    }
}
