package ru.tsu.hits.healmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

import ru.tsu.hits.healmeapp.misc.CustomStringRequest;
import ru.tsu.hits.healmeapp.misc.SaveSharedPreference;
import ru.tsu.hits.healmeapp.misc.SimpleRequestQueueFactory;

public class DeliverMedicineDescriptionFragment extends AppCompatActivity {

    private static final String TAG = "HealMe";
    private String selected_id;
    private TextView deliverMedicine2_OrderId, deliverMedicine2_Name, deliverMedicine2_Medicine, deliverMedicine2_Address, deliverMedicine2_Description, deliver_Cancel;
    private EditText editTextPrice;
    private Button deliver_btnSubmit;

    private String medicineName = "";
    private String user_ID;
    private String address_ID;
    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_medicine2);

        selected_id = getIntent().getStringExtra("request_ID");

        deliverMedicine2_OrderId = (TextView) findViewById(R.id.DeliverMedicine2_OrderId);
        deliverMedicine2_Name = (TextView) findViewById(R.id.DeliverMedicine2_Name);
        deliverMedicine2_Medicine = (TextView) findViewById(R.id.DeliverMedicine2_Medicine);
        deliverMedicine2_Address = (TextView) findViewById(R.id.DeliverMedicine2_Address);
        deliverMedicine2_Description = (TextView) findViewById(R.id.DeliverMedicine2_Description);
        editTextPrice = (EditText) findViewById(R.id.EditTextPrice);
        deliver_Cancel = (TextView) findViewById(R.id.Deliver_Cancel);
        deliver_btnSubmit = (Button) findViewById(R.id.Deliver_btnSubmit);

        String url = "apis/api/request_med/read_one.php?id=" + selected_id;

        CustomStringRequest postRequest = new CustomStringRequest(Request.Method.GET, url, "token", onSuccessRequest, onFailure);

        try {
            SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
            RequestQueue queue = factory.getQueueInstance();
            queue.add(postRequest);

        } catch (Exception e) {
            Log.e("Volley", e.getMessage());
        }

        String urlMedicineItem = "apis/api/medicine_item/read_one.php?id=" + selected_id;

        CustomStringRequest postRequest1 = new CustomStringRequest(Request.Method.GET, urlMedicineItem, "token", onSuccessItem, onFailure);

        try {
            SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
            RequestQueue queue = factory.getQueueInstance();
            queue.add(postRequest1);

        } catch (Exception e) {
            Log.e("Volley", e.getMessage());
        }

        deliverMedicine2_OrderId.setText(selected_id);

        deliver_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliverMedicineDescriptionFragment.this, ToHomeActivity.class);
                startActivity(intent);
            }
        });

        deliver_btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(editTextPrice.getText().toString());

                if (price <= 0) {
                    Toast.makeText(getBaseContext(), "Insert a valid Price", Toast.LENGTH_LONG).show();
                } else {

                    String urlInsertDeliver = "http://212.36.203.6:8077/apis/api/deliver_med/create.php";

                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("request_ID", selected_id);
                        jsonObject.put("user_ID", SaveSharedPreference.getUserID(getApplicationContext()));
                        jsonObject.put("price", price);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final JsonObjectRequest jsonObjectRequest = new
                            JsonObjectRequest(Request.Method.POST, urlInsertDeliver,
                                    jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(@NonNull JSONObject response) {
                                    // Api call succeeded
                                    try {
                                        // Get the access token
                                        @NonNull final String message = response.getString("message");
                                        Log.d(TAG, "Message: " + message);

                                        // update status from pending to ongoing
                                        String urlUpdate = "http://212.36.203.6:8077/apis/api/request_med/update.php";

                                        final JSONObject jsonObject1 = new JSONObject();
                                        try {
                                            jsonObject1.put("request_ID", selected_id);
                                            jsonObject1.put("user_ID", user_ID);
                                            jsonObject1.put("address_ID", address_ID);
                                            jsonObject1.put("status", "ongoing");
                                            jsonObject1.put("description", description);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        final JsonObjectRequest jsonObjectRequest1 = new
                                                JsonObjectRequest(Request.Method.POST, urlUpdate,
                                                        jsonObject1, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(@NonNull JSONObject response) {
                                                        // go to home page
                                                        Intent intent = new Intent(DeliverMedicineDescriptionFragment.this, ToHomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(@NonNull VolleyError error) {
                                                        Toast.makeText(getBaseContext(), "contact support asap", Toast.LENGTH_LONG).show();
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
                                        Volley.newRequestQueue(DeliverMedicineDescriptionFragment.this).add(jsonObjectRequest1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(@NonNull VolleyError error) {
                                    Toast.makeText(getBaseContext(), "Failed to Accept Request", Toast.LENGTH_LONG).show();
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
                    Volley.newRequestQueue(DeliverMedicineDescriptionFragment.this).add(jsonObjectRequest);
                }

            }

        });

    }

    Response.Listener<String> onSuccessRequest = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                deliverMedicine2_Description.setText(jsonObject.getString("description"));


                description = jsonObject.getString("description");
                user_ID = jsonObject.getString("user_ID");
                address_ID = jsonObject.getString("address_ID");

                String urlGetUserName = "apis/api/users/read_one.php?id=" + user_ID;

                CustomStringRequest postRequestUsers = new CustomStringRequest(Request.Method.GET, urlGetUserName, "token", onSuccessUser, onFailure);

                try {
                    SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
                    RequestQueue queue = factory.getQueueInstance();
                    queue.add(postRequestUsers);

                } catch (Exception e) {
                    Log.e("Volley", e.getMessage());
                }

                String urlGetAddress = "apis/api/address/readOneByID.php?id=" + address_ID;

                CustomStringRequest postRequestAddress = new CustomStringRequest(Request.Method.GET, urlGetAddress, "token", onSuccessAddress, onFailure);

                try {
                    SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
                    RequestQueue queue = factory.getQueueInstance();
                    queue.add(postRequestAddress);

                } catch (Exception e) {
                    Log.e("Volley", e.getMessage());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    Response.Listener<String> onSuccessUser = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                String user_FirstName = jsonObject.getString("user_FirstName");
                String user_LastName = jsonObject.getString("user_LastName");

                deliverMedicine2_Name.setText(user_FirstName + " " + user_LastName);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    Response.Listener<String> onSuccessAddress = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // on success Address
            JSONObject jsonObjectAddress;
            JSONArray recordsAddress;

            try {
                jsonObjectAddress = new JSONObject(response);
                recordsAddress = jsonObjectAddress.getJSONArray("records");

                deliverMedicine2_Address.setText("country: " + recordsAddress.getJSONObject(0).getString("country") + " district: " + recordsAddress.getJSONObject(0).getString("disrtrict") + " street: " + recordsAddress.getJSONObject(0).getString("street"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.Listener<String> onSuccessItem = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject;
            JSONArray records;

            try {
                jsonObject = new JSONObject(response);
                records = jsonObject.getJSONArray("records");

                for (int i = 0; i < records.length(); i++) {
                    medicineName += records.getJSONObject(i).getString("medicine_Name") + "(" + records.getJSONObject(i).getString("quantity") + ")";
                }
                deliverMedicine2_Medicine.setText(medicineName);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener onFailure = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getBaseContext(), "Error please try again later", Toast.LENGTH_LONG).show();
        }
    };
}
