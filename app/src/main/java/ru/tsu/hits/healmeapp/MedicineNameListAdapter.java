package ru.tsu.hits.healmeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.tsu.hits.healmeapp.misc.MedicineName;

public class MedicineNameListAdapter extends BaseAdapter {
    Context context;
    ArrayList<MedicineName> arrayList;

    public MedicineNameListAdapter(Context context, ArrayList<MedicineName> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_medicine_name_list, parent, false);
        }
        TextView medicineName = (TextView) convertView.findViewById(R.id.txtMedicineNameItemListView);
        medicineName.setText(arrayList.get(position).getMedicine_Name());
        TextView medicineQuantity = (TextView) convertView.findViewById(R.id.txtMedicineQuantityItemListView);
        medicineQuantity.setText("("+ arrayList.get(position).getMedicine_Quantity()+")");
        return convertView;
    }

}
