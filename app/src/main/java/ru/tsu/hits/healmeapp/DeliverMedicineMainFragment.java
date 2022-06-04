package ru.tsu.hits.healmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class DeliverMedicineMainFragment extends AppCompatActivity {

    private ListView deliverMedicineListView;
    private ArrayList<RequestMedicineName> listRequestMedicines;
    private RequestMedicineNameAdapter requestMedicineNameAdapter;

    private RequestMedicineName requestMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_medicine);

        deliverMedicineListView = (ListView) findViewById(R.id.DeliverMedicineList);

        listRequestMedicines = new ArrayList<RequestMedicineName>();

        // API get all requests pending
        String url = "apis/api/request_med/readDeliverMedicine.php?id="+ SaveSharedPreference.getUserID(getApplicationContext());

        CustomStringRequest postRequest = new CustomStringRequest(Request.Method.GET, url, "token", onSuccessRequest, onFailure);

        try {
            SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
            RequestQueue queue = factory.getQueueInstance();
            queue.add(postRequest);

        } catch (Exception e) {
            Log.e("Volley", e.getMessage());
        }

        deliverMedicineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RequestMedicineName selectedRequest = (RequestMedicineName) parent.getAdapter().getItem(position);
                Intent intent = new Intent(DeliverMedicineMainFragment.this, DeliverMedicineDescriptionFragment.class);
                intent.putExtra("request_ID",selectedRequest.getRequest_ID());
                startActivity(intent);
            }
        });
    }

    Response.Listener<String> onSuccessRequest = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // on success Request
            JSONObject jsonObjectRequest;
            JSONArray recordsRequest;

            try {
                jsonObjectRequest = new JSONObject(response);
                recordsRequest = jsonObjectRequest.getJSONArray("records");

                for (int i = 0; i < recordsRequest.length(); i++) {
                    requestMedicine = new RequestMedicineName();

                    requestMedicine.setRequest_ID(recordsRequest.getJSONObject(i).getString("request_ID"));
                    requestMedicine.setDescription(recordsRequest.getJSONObject(i).getString("description"));

                    String user_FirstName = recordsRequest.getJSONObject(i).getString("user_FirstName");
                    String user_LastName = recordsRequest.getJSONObject(i).getString("user_LastName");
                    requestMedicine.setUserName(user_FirstName + " " + user_LastName);

                    String address_name = "country: " + recordsRequest.getJSONObject(i).getString("country") + " district: " + recordsRequest.getJSONObject(i).getString("disrtrict") + " street: " + recordsRequest.getJSONObject(i).getString("street");
                    requestMedicine.setAddress(address_name);

                    listRequestMedicines.add(requestMedicine);

                    requestMedicineNameAdapter = new RequestMedicineNameAdapter(getApplicationContext(), listRequestMedicines);
                    deliverMedicineListView.setAdapter(requestMedicineNameAdapter);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener onFailure = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getBaseContext(), "No Requests Available", Toast.LENGTH_LONG).show();
        }
    };
}
