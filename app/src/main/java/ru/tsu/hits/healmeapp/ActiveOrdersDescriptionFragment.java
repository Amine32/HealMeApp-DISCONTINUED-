package ru.tsu.hits.healmeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.tsu.hits.healmeapp.misc.CustomStringRequest;
import ru.tsu.hits.healmeapp.misc.SimpleRequestQueueFactory;

public class ActiveOrdersDescriptionFragment extends AppCompatActivity {

    private String selected_id;
    private TextView activeOrders2_OrderId, activeOrders2_Name, activeOrders2_Medicine, activeOrders2_Address, activeOrders2_Description, activeOrders2_Price;
    private Button activeOrders2_btnBack;

    private String medicineName = "";
    private String user_ID;
    private String address_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders2);

        selected_id = getIntent().getStringExtra("request_ID");

        activeOrders2_OrderId = (TextView) findViewById(R.id.ActiveOrders2_OrderId);
        activeOrders2_Name = (TextView) findViewById(R.id.ActiveOrders2_Name);
        activeOrders2_Medicine = (TextView) findViewById(R.id.ActiveOrders2_Medicine);
        activeOrders2_Address = (TextView) findViewById(R.id.ActiveOrders2_Address);
        activeOrders2_Description = (TextView) findViewById(R.id.ActiveOrders2_Description);
        activeOrders2_Price = (TextView) findViewById(R.id.ActiveOrders2_Price);
        activeOrders2_btnBack = (Button) findViewById(R.id.ActiveOrders2_btnBack);

        //get request details
        String url = "apis/api/request_med/read_one.php?id=" + selected_id;

        CustomStringRequest postRequest = new CustomStringRequest(Request.Method.GET, url, "token", onSuccessRequest, onFailure);

        try {
            SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
            RequestQueue queue = factory.getQueueInstance();
            queue.add(postRequest);

        } catch (Exception e) {
            Log.e("Volley", e.getMessage());
        }

        // get medicines

        String urlMedicineItem = "apis/api/medicine_item/read_one.php?id=" + selected_id;

        CustomStringRequest postRequest1 = new CustomStringRequest(Request.Method.GET, urlMedicineItem, "token", onSuccessItem, onFailure);

        try {
            SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
            RequestQueue queue = factory.getQueueInstance();
            queue.add(postRequest1);

        } catch (Exception e) {
            Log.e("Volley", e.getMessage());
        }


        // get price
        String urlPrice = "apis/api/deliver_med/read_one_by_request_id.php?id=" + selected_id;

        CustomStringRequest postRequest2 = new CustomStringRequest(Request.Method.GET, urlPrice, "token", onSuccessPrice, onFailure);

        try {
            SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
            RequestQueue queue = factory.getQueueInstance();
            queue.add(postRequest2);

        } catch (Exception e) {
            Log.e("Volley", e.getMessage());
        }

        activeOrders2_OrderId.setText(selected_id);

        activeOrders2_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActiveOrdersDescriptionFragment.this, ToHomeActivity.class);
                startActivity(intent);
            }
        });
    }
    Response.Listener<String> onSuccessRequest = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);

                user_ID = jsonObject.getString("user_ID");
                address_ID = jsonObject.getString("address_ID");
                activeOrders2_Description.setText(jsonObject.getString("description"));

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

    Response.Listener<String> onSuccessPrice = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                activeOrders2_Price.setText(jsonObject.getString("price"));
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

                activeOrders2_Name.setText(user_FirstName + " " + user_LastName);

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

                activeOrders2_Address.setText("country: " + recordsAddress.getJSONObject(0).getString("country") + " district: " + recordsAddress.getJSONObject(0).getString("disrtrict") + " street: " + recordsAddress.getJSONObject(0).getString("street"));
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
                activeOrders2_Medicine.setText(medicineName);

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
