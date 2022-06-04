package ru.tsu.hits.healmeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.tsu.hits.healmeapp.misc.AddressName;

public class AddressNameAdapter extends BaseAdapter {

    Context context;
    ArrayList<AddressName> arrayList;

    public AddressNameAdapter(Context context, ArrayList<AddressName> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address_name_spinner, parent, false);
        }
        TextView address = (TextView) convertView.findViewById(R.id.txtAddressItemSpinner);
        address.setText(arrayList.get(position).getCountry() + " "+
                arrayList.get(position).getDistrict() +" "+
                arrayList.get(position).getStreet() + " "+
                arrayList.get(position).getBuilding() + " "+
                arrayList.get(position).getHouseName()+ " "+
                arrayList.get(position).getFloor());
        return convertView;
    }
}
