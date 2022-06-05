package ru.tsu.hits.healmeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import ru.tsu.hits.healmeapp.misc.RequestMedicineName;
import ru.tsu.hits.healmeapp.misc.SaveSharedPreference;
import ru.tsu.hits.healmeapp.misc.SimpleRequestQueueFactory;

public class ActiveOrdersMainFragment extends AppCompatActivity {

    private ListView activeOrdersListView;
    private ArrayList<RequestMedicineName> listActiveOrders;
    private ActiveRequestsAdapter activeOrdersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_orders);

        activeOrdersListView = (ListView) findViewById(R.id.ActiveOrdersList);
        listActiveOrders = new ArrayList<RequestMedicineName>();

        // API get all requests ongoing
        String url = "apis/api/request_med/readActiveOrders.php?id=" + SaveSharedPreference.getUserID(getApplicationContext());

        CustomStringRequest postRequest = new CustomStringRequest(Request.Method.GET, url, "token", onSuccess, onFailure);

        try {
            SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
            RequestQueue queue = factory.getQueueInstance();
            queue.add(postRequest);

        } catch (Exception e) {
            Log.e("Volley", e.getMessage());
        }

        activeOrdersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RequestMedicineName selectedRequest = (RequestMedicineName) parent.getAdapter().getItem(position);
                Intent intent = new Intent(ActiveOrdersMainFragment.this, ActiveOrdersDescriptionFragment.class);
                intent.putExtra("request_ID", selectedRequest.getRequest_ID());
                startActivity(intent);
            }
        });
    }

    Response.Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // on success Request
            JSONObject jsonObjectRequest;
            JSONArray recordsRequest;

            try {
                jsonObjectRequest = new JSONObject(response);
                recordsRequest = jsonObjectRequest.getJSONArray("records");

                RequestMedicineName requestMedicine;
                for (int i = 0; i < recordsRequest.length(); i++) {
                    requestMedicine = new RequestMedicineName();

                    requestMedicine.setRequest_ID(recordsRequest.getJSONObject(i).getString("request_ID"));
                    requestMedicine.setDescription(recordsRequest.getJSONObject(i).getString("description"));

                    String price = recordsRequest.getJSONObject(i).getString("price");
                    requestMedicine.setUserName(price);

                    String address_name = "country: " + recordsRequest.getJSONObject(i).getString("country") + " district: " + recordsRequest.getJSONObject(i).getString("disrtrict") + " street: " + recordsRequest.getJSONObject(i).getString("street");
                    requestMedicine.setAddress(address_name);

                    listActiveOrders.add(requestMedicine);

                    activeOrdersAdapter = new ActiveRequestsAdapter(getApplicationContext(), listActiveOrders);
                    activeOrdersListView.setAdapter(activeOrdersAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    Response.ErrorListener onFailure = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getBaseContext(), "No Active Orders found", Toast.LENGTH_LONG).show();
        }
    };
}
