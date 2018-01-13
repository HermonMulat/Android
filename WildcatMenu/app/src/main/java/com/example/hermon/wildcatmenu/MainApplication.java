package com.example.hermon.wildcatmenu;


import android.app.Application;

import com.example.hermon.wildcatmenu.data.Food;



import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {
    private Realm realmDB;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }

    public Realm getRealmDB(){
        return realmDB;
    }

    public void openRealm(){
        RealmConfiguration config = new RealmConfiguration.
                Builder().
                deleteRealmIfMigrationNeeded().build();

        realmDB = Realm.getInstance(config);

        if (realmDB.where(Food.class).findAll().size() == 0){
            populateMenu();
        }
    }

    private void populateMenu() {
        String[] names = getResources().getStringArray(R.array.foodNames);
        String[] descr = getResources().getStringArray(R.array.foodDescription);
        String[] categ = getResources().getStringArray(R.array.foodCategory);
        String[] price = getResources().getStringArray(R.array.foodPrice);

        for (int i = 0; i < names.length; i++) {
            addFood(names[i],descr[i],categ[i],Float.parseFloat(price[i]),i);
        }
    }


    private void addFood(String n, String Des,String cat, float p,int id) {
        realmDB.beginTransaction();
        Food f = realmDB.createObject(Food.class, ""+id);
        f.setName(n);
        f.setDescription(Des);
        f.setCategory(cat);
        f.setPrice(p);
        realmDB.commitTransaction();
    }

    public  void closeRealm(){
        realmDB.close();
    }

}
