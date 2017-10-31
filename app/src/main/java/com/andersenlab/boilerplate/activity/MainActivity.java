package com.andersenlab.boilerplate.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andersenlab.boilerplate.R;
import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.presenter.MainPresenter;
import com.andersenlab.boilerplate.view.MainMvpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView {

    private static final String CURRENT_ITEM_BUNDLE_KEY = "com.andersenlab.boilerplate.activity.currentItem";

    @BindView(R.id.tv_main_hello_world) TextView tvHelloWorld;

    private MainPresenter mainPresenter;
    private Image currentItem = new Image();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            currentItem = savedInstanceState.getParcelable(CURRENT_ITEM_BUNDLE_KEY);
        }

        setText();

        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_ITEM_BUNDLE_KEY, currentItem);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }

    @OnClick(R.id.btn_main_add_item)
    public void addItem(View view) {
        Timber.i("Tap on add item button");
        loadItem();
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
        Timber.i("showNewItem " + item.getId());
        this.currentItem = item;
        setText();
    }

    @Override
    public void showEmpty() {
        Toast.makeText(this, "No more elements in the list", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError() {
        Timber.e("Error. load next item");
    }

    private void setText() {
        if (currentItem != null) {
            tvHelloWorld.setText(currentItem.getContentDescription() != null ?
                    currentItem.getContentDescription() : currentItem.getImageUrl());
        }
    }

    private void loadItem() {
        if (mainPresenter != null) {
            mainPresenter.loadItem();
        }
    }
}
