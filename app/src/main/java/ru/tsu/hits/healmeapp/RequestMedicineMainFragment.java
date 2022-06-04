package ru.tsu.hits.healmeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.tsu.hits.healmeapp.misc.CustomStringRequest;
import ru.tsu.hits.healmeapp.misc.MedicineName;
import ru.tsu.hits.healmeapp.misc.SimpleRequestQueueFactory;

public class RequestMedicineMainFragment extends AppCompatActivity {

    private Button btnNext, btnMinus, btnPlus, btnAddMedicine;
    private TextView txtQuantity;
    private SearchView searchMedicineName;
    private ListView listViewMedicineName;
    private ArrayList<MedicineName> listMedicineNames;
    private MedicineNameAdapter adapterForSearch;
    private ArrayList<MedicineName> filteredMedicineNames;

    private ListView listViewMedicineItem;
    private MedicineNameListAdapter adapterForMedicineItems;
    private ArrayList<MedicineName> listMedicineItemsAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_medicine);

        btnNext = (Button) findViewById(R.id.Request_btnNext);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        txtQuantity = (TextView) findViewById(R.id.Request_QuantityIndiciator);
        searchMedicineName = (SearchView) findViewById(R.id.txtRequest_MedicineName);
        listViewMedicineName = (ListView) findViewById(R.id.listViewRequestMedicineMedicineName);


        listViewMedicineItem = (ListView) findViewById(R.id.listViewRequestMedicineItemInList);
        btnAddMedicine = (Button) findViewById(R.id.Request_btnAdd);
        listMedicineItemsAdded = new ArrayList<MedicineName>();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listMedicineItemsAdded.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Medicine List is empty", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(RequestMedicineMainFragment.this, RequestMedicineDescriptionFragment.class);
                    intent.putExtra("MedicineList", listMedicineItemsAdded);
                    startActivity(intent);
                }
            }
        });

        btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filteredMedicineNames != null) {
                    boolean exist = false;
                    for (MedicineName medicineName : listMedicineItemsAdded) {
                        if (medicineName.getMedicine_ID().contains(filteredMedicineNames.get(0).getMedicine_ID())) {
                            medicineName.setMedicine_Quantity(String.valueOf(Integer.valueOf(txtQuantity.getText().toString()) + Integer.valueOf(medicineName.getMedicine_Quantity())));
                            exist = true;
                        }
                    }
                    if (!exist) {
                        MedicineName medicineName = new MedicineName();
                        medicineName.setMedicine_ID(filteredMedicineNames.get(0).getMedicine_ID());
                        medicineName.setMedicine_Name(filteredMedicineNames.get(0).getMedicine_Name());
                        medicineName.setMedicine_Quantity(txtQuantity.getText().toString());
                        listMedicineItemsAdded.add(medicineName);
                    }
                    adapterForMedicineItems = new MedicineNameListAdapter(getApplicationContext(), listMedicineItemsAdded);
                    listViewMedicineItem.setAdapter(adapterForMedicineItems);
                    txtQuantity.setText("1");
                } else {
                    Toast.makeText(getBaseContext(), "Please Select A Medicine", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf((String) txtQuantity.getText()) > 1) {
                    txtQuantity.setText(String.valueOf((Integer.valueOf((String) txtQuantity.getText())) - (1)));
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtQuantity.setText(String.valueOf(Integer.valueOf((String) txtQuantity.getText()) + (1)));
            }
        });

        // API get all medicine
        String url = "apis/api/medicine/read.php";

        CustomStringRequest postRequest = new CustomStringRequest(Request.Method.GET, url, "token", onSuccess, onFailure);

        try {
            SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
            RequestQueue queue = factory.getQueueInstance();
            queue.add(postRequest);

        } catch (Exception e) {
            Log.e("Volley", e.getMessage());
        }

    }

    Response.Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject;
            JSONArray records;
            try {
                jsonObject = new JSONObject(response);
                records = jsonObject.getJSONArray("records");

                listMedicineNames = new ArrayList<MedicineName>();
                MedicineName medicineName;
                for (int i = 0; i < records.length(); i++) {
                    medicineName = new MedicineName();
                    medicineName.setMedicine_ID(records.getJSONObject(i).getString("medicine_ID"));
                    medicineName.setMedicine_Name(records.getJSONObject(i).getString("medicine_Name"));
                    listMedicineNames.add(medicineName);
                }

                adapterForSearch = new MedicineNameAdapter(getApplicationContext(), listMedicineNames);
                listViewMedicineName.setAdapter(adapterForSearch);

                listViewMedicineName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        searchMedicineName.setQuery(adapterForSearch.getItem(position).toString(), false);
                        listViewMedicineName.setVisibility(View.INVISIBLE);
                    }
                });

                searchMedicineName.setOnSearchClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listViewMedicineName.setVisibility(View.VISIBLE);
                    }
                });

                searchMedicineName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filteredMedicineNames = new ArrayList<MedicineName>();
                        listViewMedicineName.setVisibility(View.VISIBLE);
                        for (MedicineName md : listMedicineNames) {
                            if (md.getMedicine_Name().toLowerCase().contains(newText.toLowerCase())) {
                                filteredMedicineNames.add(md);
                            }
                        }
                        adapterForSearch = new MedicineNameAdapter(getApplicationContext(), filteredMedicineNames);
                        listViewMedicineName.setAdapter(adapterForSearch);
                        return true;
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Response.ErrorListener onFailure = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Volley", error.getMessage());
            error.printStackTrace();
        }
    };
}
