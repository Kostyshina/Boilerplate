package com.andersenlab.boilerplate.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andersenlab.boilerplate.R;
import com.andersenlab.boilerplate.adapter.ImageAdapter;
import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.presenter.MainPresenter;
import com.andersenlab.boilerplate.view.MainMvpView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView {

    private static final String IMAGES_BUNDLE_KEY = "com.andersenlab.boilerplate.activity.imageList";

    @BindView(R.id.rv_main_images) RecyclerView rvImages;
    @BindView(R.id.fl_main_button_container) FrameLayout addItemContainer;

    private MainPresenter mainPresenter;
    private ImageAdapter imageAdapter;
    private ArrayList<Image> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        setContentView(R.layout.activity_main);

        imageAdapter = new ImageAdapter(this);

        if (savedInstanceState != null) {
            imageList = savedInstanceState.getParcelableArrayList(IMAGES_BUNDLE_KEY);
        }

        if (imageList != null && !imageList.isEmpty()) {
            imageAdapter.setItems(imageList);
        }

        rvImages.setLayoutManager(new LinearLayoutManager(this));
        rvImages.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        ((SimpleItemAnimator)rvImages.getItemAnimator()).setSupportsChangeAnimations(false);
        rvImages.setAdapter(imageAdapter);

        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(IMAGES_BUNDLE_KEY, imageList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }

    @OnClick(R.id.btn_main_add_item)
    public void addItem(View view) {
        loadItem();
        Timber.i("Tap on add item button");
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.app_name);
    }

    @Override
    public void showNewItem(Image item) {
        setNewItem(item);
        Timber.i("showNewItem " + item.getId());
    }

    @Override
    public void showEmpty() {
        Toast.makeText(this, R.string.main_no_items, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(MvpViewException exc) {
        Timber.e(exc);
    }

    private void loadItem() {
        if (mainPresenter != null) {
            mainPresenter.loadItem(this);
        }
    }

    private void setNewItem(Image item) {
        if (imageList == null)
            imageList = new ArrayList<>();

        imageList.add(item);
        imageAdapter.addItem(item);
        rvImages.scrollToPosition(imageList.size()-1);
    }
}
