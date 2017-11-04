package com.andersenlab.boilerplate.model.realm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Class-interactor with {@link Realm} manager.
 */

public class RealmInteractor {
    private static RealmInteractor instance;
    private final Realm realm;

    private RealmInteractor() {
        realm = Realm.getDefaultInstance();
    }

    public static synchronized RealmInteractor getInstance() {
        if (instance == null) {
            instance = new RealmInteractor();
        }
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //Refresh the realm instance
    public void refresh() {
        realm.refresh();
    }

    //clear all objects from specific class
    public <T extends RealmModel> void clearAll(Class<T> clazz) {
        realm.beginTransaction();
        realm.delete(clazz);
        realm.commitTransaction();
    }

    //find all objects in the specific class
    public <T extends RealmModel> List<T> getObjects(Class<T> clazz) {
        RealmResults<T> objResult = realm.where(clazz).findAll();
        List<T> objList = new ArrayList<>();
        for (T obj : objResult)
            objList.add(obj);
        return objList;
    }

    //query a single item with the given id
    public <T extends RealmModel> T getObjectById(Class<T> clazz, String id) {
        return realm.where(clazz).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public <T extends RealmModel> boolean hasObjects(Class<T> clazz) {
        return realm.where(clazz).count() != 0;
    }

    public <T extends RealmModel> void addObject(T obj) {
        realm.executeTransaction(realmInstance ->
                realmInstance.copyToRealmOrUpdate(obj));
    }
}
