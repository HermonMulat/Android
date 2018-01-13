package com.example.hermon.weather;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    private Realm realmCities;
    public void openRealm(){
        RealmConfiguration config = new RealmConfiguration.
                Builder().
                deleteRealmIfMigrationNeeded().
                build();
        realmCities = Realm.getInstance(config);
    }

    public void closeRealm(){
        realmCities.close();
    }

    public Realm getRealmCities() {
        return realmCities;
    }

}
