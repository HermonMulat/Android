package com.example.hermon.shoppinglist;


import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {
    private Realm realmItemList;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmItemList = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmItemList.close();
    }

    public Realm getRealmItemList() {
        return realmItemList;
    }
}
