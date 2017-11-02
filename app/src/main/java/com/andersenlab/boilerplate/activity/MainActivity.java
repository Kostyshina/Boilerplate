package com.andersenlab.boilerplate.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andersenlab.boilerplate.R;
import com.andersenlab.boilerplate.adapter.ImageAdapter;
import com.andersenlab.boilerplate.customview.FloatingBottomSheet;
import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.presenter.MainPresenter;
import com.andersenlab.boilerplate.view.MainMvpView;

import java.util.ArrayList;

import butterknife.BindView;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView {

    private static final String IMAGES_BUNDLE_KEY = "com.andersenlab.boilerplate.activity.imageList";

    @BindView(R.id.rv_main_images) RecyclerView imageRecyclerView;
    @BindView(R.id.fbs_main_add_item) FloatingBottomSheet addItem;

    private MainPresenter mainPresenter;
    private ImageAdapter imageAdapter;
    private ArrayList<Image> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageAdapter = new ImageAdapter(this);

        if (savedInstanceState != null) {
            imageList = savedInstanceState.getParcelableArrayList(IMAGES_BUNDLE_KEY);
        }

        if (imageList == null)
            imageList = new ArrayList<>();

        if (!imageList.isEmpty()) {
            imageAdapter.setItems(imageList);
        }

        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        ((SimpleItemAnimator) imageRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        imageRecyclerView.setAdapter(imageAdapter);

        addItem.setOnClickListener(this::onAddItemClicked);

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
        addItemToList(item);
        Timber.i("showNewItem " + item.getId());
    }

    @Override
    public void showNoNewItem() {
        Toast.makeText(this, R.string.main_no_items, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(MvpViewException exc) {
        Timber.e(exc);
    }

    private void onAddItemClicked(View view) {
        loadItem();
        Timber.i("Tap on add item button");
    }

    private void loadItem() {
        if (mainPresenter != null) {
            mainPresenter.loadItem(this);
        } else Timber.e("You must initialize presenter first");
    }

    private void addItemToList(Image item) {
        imageList.add(item);
        imageAdapter.addItem(item);
        imageRecyclerView.scrollToPosition(imageList.size() - 1);
    }
}
