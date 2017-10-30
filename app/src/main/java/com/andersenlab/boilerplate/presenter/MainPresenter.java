package com.andersenlab.boilerplate.presenter;

import com.andersenlab.boilerplate.BoilerplateApp;
import com.andersenlab.boilerplate.R;
import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.view.MainMvpView;

public class MainPresenter extends BasePresenter<MainMvpView> {
    public void loadItem(int serialNum) {
        if (isViewAttached()) {
            if (serialNum % 4 == 0)
                getMvpView().showError();
            else
                getMvpView().showNewItem(
                        new Image(serialNum,
                                BoilerplateApp.getInstance()
                                .getString(R.string.main_item_text) + " " + serialNum, null));
        } else
            getMvpView().showError();
    }
}
