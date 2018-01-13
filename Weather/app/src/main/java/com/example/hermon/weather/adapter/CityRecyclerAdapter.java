package com.example.hermon.weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hermon.weather.MainActivity;
import com.example.hermon.weather.R;
import com.example.hermon.weather.data.City;
import com.example.hermon.weather.touch.CityTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class CityRecyclerAdapter
        extends RecyclerView.Adapter<CityRecyclerAdapter.ViewHolder>
        implements CityTouchHelperAdapter {

    private List<City> cityList;
    private Context context;
    private Realm realmCities;

    public CityRecyclerAdapter(Context context, Realm realmCities) {
        this.context = context;
        this.realmCities = realmCities;


        RealmResults<City> allCityList = realmCities.where(City.class).findAll();

        cityList = new ArrayList<City>();

        for (int i = 0; i < allCityList.size(); i++) {
            cityList.add(allCityList.get(i));
        }
    }

    @Override

    public CityRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.city_view, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final CityRecyclerAdapter.ViewHolder holder, int position) {
        holder.cityName.setText(cityList.get(position).getCityName());
        // add any row associated functionality here (like what happens when pressing the row)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).showWeatherDetails(
                        cityList.get(holder.getAdapterPosition()).getCityName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        realmCities.beginTransaction();
        cityList.get(position).deleteFromRealm();
        realmCities.commitTransaction();

        cityList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

        if (fromPosition > toPosition){
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(cityList,i,i-1);
            }
        }
        else{
            for (int j = fromPosition; j < toPosition; j++) {
                Collections.swap(cityList,j,j+1);
            }
        }

        notifyItemMoved(fromPosition,toPosition);
    }

    public void addCity(String cityName){
        realmCities.beginTransaction();
        City newCity = realmCities.createObject(City.class, UUID.randomUUID().toString());
        newCity.setCityName(cityName);
        realmCities.commitTransaction();

        cityList.add(0,newCity);
        notifyItemInserted(0);
    }

    public void deleteAll(){
        realmCities.beginTransaction();
        RealmResults<City> allCityList = realmCities.where(City.class).findAll();
        allCityList.deleteAllFromRealm();
        realmCities.commitTransaction();

        cityList.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // should reflect what each row looks like
        private TextView cityName;
        public ViewHolder(View itemView) {
            super(itemView);

            cityName = (TextView) itemView.findViewById(R.id.tv_cityName);
        }
    }
}
