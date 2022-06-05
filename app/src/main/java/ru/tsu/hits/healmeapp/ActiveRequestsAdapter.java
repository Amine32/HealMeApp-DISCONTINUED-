package ru.tsu.hits.healmeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.tsu.hits.healmeapp.misc.RequestMedicineName;

public class ActiveRequestsAdapter extends BaseAdapter {
    Context context;
    ArrayList<RequestMedicineName> arrayList;

    public ActiveRequestsAdapter(Context context, ArrayList<RequestMedicineName> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_active_request_list, parent, false);
        }
        TextView requestID = (TextView) convertView.findViewById(R.id.ActiveRequestsList_OrderId);
        TextView price = (TextView) convertView.findViewById(R.id.ActiveRequestsList_Price);
        TextView address = (TextView) convertView.findViewById(R.id.ActiveRequestsList_Address);
        TextView description = (TextView) convertView.findViewById(R.id.ActiveRequestsList_Description);

        requestID.setText(arrayList.get(position).getRequest_ID());
        price.setText(arrayList.get(position).getUserName());
        address.setText(arrayList.get(position).getAddress());
        description.setText(arrayList.get(position).getDescription());

        return convertView;
    }
}

