package ru.tsu.hits.healmeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.tsu.hits.healmeapp.misc.AddressName;
import ru.tsu.hits.healmeapp.misc.CustomStringRequest;
import ru.tsu.hits.healmeapp.misc.MedicineName;
import ru.tsu.hits.healmeapp.misc.SaveSharedPreference;
import ru.tsu.hits.healmeapp.misc.SimpleRequestQueueFactory;

public class RequestMedicineDescriptionFragment extends AppCompatActivity {

    private static final String TAG = "HealMe";
    private TextView txtCancel, request_AddAddress;
    private ArrayList<MedicineName> listMedicineItemsAdded;
    private ArrayList<AddressName> listOfUserAddresses;
    private AddressNameAdapter addressNameAdapter;
    private Spinner addressSpinner;
    private AddressName selectedAddress;
    private Button btnSubmit;
    private EditText txtDescription;
    private String user_ID;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_medicine2);
        user_ID = SaveSharedPreference.getUserID(getApplicationContext());

        listMedicineItemsAdded = (ArrayList<MedicineName>) getIntent().getSerializableExtra("MedicineList");

        txtCancel = (TextView) findViewById(R.id.Request_Cancel);
        addressSpinner = (Spinner) findViewById(R.id.request_spinner_address);
        txtDescription = (EditText) findViewById(R.id.txtRequest_Description);
        btnSubmit = (Button) findViewById(R.id.Request_btnSubmit);
        request_AddAddress = (TextView) findViewById(R.id.Request_AddAddress);

        request_AddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestMedicineDescriptionFragment.this, AddAddress.class);
                intent.putExtra("MedicineList", listMedicineItemsAdded);
                startActivity(intent);
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestMedicineDescriptionFragment.this, ToHomeActivity.class);
                startActivity(intent);
            }
        });

        // API get all medicine

        String url = "apis/api/address/read_one.php?id=" + user_ID;

        CustomStringRequest postRequest = new CustomStringRequest(Request.Method.GET, url, "token", onSuccess, onFailure);

        try {
            SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
            RequestQueue queue = factory.getQueueInstance();
            queue.add(postRequest);

        } catch (Exception e) {
            Log.e("Volley", e.getMessage());
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtDescription.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Fill the description", Toast.LENGTH_LONG).show();
                } else if (selectedAddress == null) {
                    Toast.makeText(getBaseContext(), "No Address Selected", Toast.LENGTH_LONG).show();
                } else {
                    // send request api

                    String url = "apis/api/request_med/create.php";

                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_ID", user_ID);
                        jsonObject.put("address_ID", selectedAddress.getAddress_ID());
                        jsonObject.put("status", "pending");
                        jsonObject.put("description", txtDescription.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final JsonObjectRequest jsonObjectRequest = new
                            JsonObjectRequest(Request.Method.POST, url,
                                    jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(@NonNull JSONObject response) {
                                    // Api call succeeded
                                    try {
                                        // Get the access token
                                        @NonNull final String message = response.getString("message");
                                        @NonNull final String request_ID = response.getString("request_ID");
                                        Log.d(TAG, "Message: " + message);
                                        Log.d(TAG, "request_ID: " + request_ID);

                                        // send medicine item api

                                        for (MedicineName item : listMedicineItemsAdded) {
                                            String url = "apis/api/medicine_item/create.php";

                                            final JSONObject jsonObjectItem = new JSONObject();
                                            try {
                                                jsonObjectItem.put("medicine_ID", item.getMedicine_ID());
                                                jsonObjectItem.put("request_ID", request_ID);
                                                jsonObjectItem.put("quantity", item.getMedicine_Quantity());
                                                jsonObjectItem.put("prescription", "null");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            final JsonObjectRequest jsonObjectRequestItem = new
                                                    JsonObjectRequest(Request.Method.POST, url,
                                                            jsonObjectItem, new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(@NonNull JSONObject response) {
                                                            // Api call succeeded
                                                            try {
                                                                // Get the access token
                                                                @NonNull final String message = response.getString("message");
                                                                Log.d(TAG, "Message: " + message);

                                                                //done

                                                                if (i++ == listMedicineItemsAdded.size() - 1) {
                                                                    Intent intent = new Intent(RequestMedicineDescriptionFragment.this, ToHomeActivity.class);
                                                                    startActivity(intent);
                                                                }


                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(@NonNull VolleyError error) {
                                                            Toast.makeText(getBaseContext(), "Failed to Add medicine", Toast.LENGTH_LONG).show();
                                                            error.printStackTrace();
                                                        }
                                                    }) {
                                                        @NonNull
                                                        @Override
                                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                                            // Build the headers
                                                            final Map<String, String> params = new HashMap<>();
                                                            params.put("Content-Type", "application/json");
                                                            return params;
                                                        }
                                                    };
                                            Volley.newRequestQueue(RequestMedicineDescriptionFragment.this).add(jsonObjectRequestItem);

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(@NonNull VolleyError error) {
                                    Toast.makeText(getBaseContext(), "Failed to create Request", Toast.LENGTH_LONG).show();
                                    error.printStackTrace();
                                }
                            }) {
                                @NonNull
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    // Build the headers
                                    final Map<String, String> params = new HashMap<>();
                                    params.put("Content-Type", "application/json");
                                    return params;
                                }
                            };
                    Volley.newRequestQueue(RequestMedicineDescriptionFragment.this).add(jsonObjectRequest);
                }
            }
        });

    }

    Response.Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            addressSpinner.setEnabled(true);
            addressSpinner.setClickable(true);

            JSONObject jsonObject;
            JSONArray records;
            try {
                jsonObject = new JSONObject(response);
                records = jsonObject.getJSONArray("records");

                listOfUserAddresses = new ArrayList<AddressName>();
                AddressName address;
                for (int i = 0; i < records.length(); i++) {
                    address = new AddressName();
                    address.setAddress_ID(records.getJSONObject(i).getString("address_ID"));
                    address.setUser_ID(records.getJSONObject(i).getString("user_ID"));
                    address.setCountry(records.getJSONObject(i).getString("country"));
                    address.setDistrict(records.getJSONObject(i).getString("disrtrict"));
                    address.setBuilding(records.getJSONObject(i).getString("building"));
                    address.setHouseName(records.getJSONObject(i).getString("houseName"));
                    address.setStreet(records.getJSONObject(i).getString("street"));
                    address.setFloor(records.getJSONObject(i).getString("floor"));

                    listOfUserAddresses.add(address);
                }
                addressNameAdapter = new AddressNameAdapter(getApplicationContext(), listOfUserAddresses);

                addressSpinner.setAdapter(addressNameAdapter);

                addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedAddress = (AddressName) addressNameAdapter.getItem(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedAddress = (AddressName) addressNameAdapter.getItem(0);
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
            Toast.makeText(getBaseContext(), "No addresses found", Toast.LENGTH_LONG).show();
            addressSpinner.setEnabled(false);
            addressSpinner.setClickable(false);
        }
    };

}