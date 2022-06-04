package ru.tsu.hits.healmeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.tsu.hits.healmeapp.misc.MedicineName;
import ru.tsu.hits.healmeapp.misc.SaveSharedPreference;

public class AddAddress extends AppCompatActivity {

    private ArrayList<MedicineName> listMedicineItemsAdded;
    private String user_ID;

    private EditText address_Country, address_District, address_Building, address_Street, address_HouseName, address_Floor;
    private Button btnAddAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        listMedicineItemsAdded = (ArrayList<MedicineName>) getIntent().getSerializableExtra("MedicineList");
        user_ID = SaveSharedPreference.getUserID(getApplicationContext());

        address_Country = (EditText) findViewById(R.id.address_Country);
        address_District = (EditText) findViewById(R.id.address_District);
        address_Building = (EditText) findViewById(R.id.address_Building);
        address_Street = (EditText) findViewById(R.id.address_Street);
        address_HouseName = (EditText) findViewById(R.id.address_HouseName);
        address_Floor = (EditText) findViewById(R.id.address_Floor);
        btnAddAddress = (Button) findViewById(R.id.btnAddAddress);

        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address_Country.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill the Country", Toast.LENGTH_LONG).show();
                } else if (address_District.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill the District", Toast.LENGTH_LONG).show();
                } else if (address_Building.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill the Building", Toast.LENGTH_LONG).show();
                } else if (address_Street.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill the Street", Toast.LENGTH_LONG).show();
                } else if (address_HouseName.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill the House Name", Toast.LENGTH_LONG).show();
                } else if (address_Floor.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill the Floor", Toast.LENGTH_LONG).show();
                } else {

                    String url = "apis/api/address/create.php";

                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_ID", user_ID);
                        jsonObject.put("country", address_Country.getText().toString());
                        jsonObject.put("district", address_District.getText().toString());
                        jsonObject.put("building", address_Building.getText().toString());
                        jsonObject.put("houseName", address_HouseName.getText().toString());
                        jsonObject.put("street", address_Street.getText().toString());
                        jsonObject.put("floor", address_Floor.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final JsonObjectRequest jsonObjectRequest = new
                            JsonObjectRequest(Request.Method.POST, url,
                                    jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(@NonNull JSONObject response) {
                                    Intent intent = new Intent(AddAddress.this, RequestMedicineDescriptionFragment.class);
                                    intent.putExtra("MedicineList", listMedicineItemsAdded);
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(@NonNull VolleyError error) {
                                    Toast.makeText(getBaseContext(), "Failed to Add Address", Toast.LENGTH_LONG).show();
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
                    Volley.newRequestQueue(AddAddress.this).add(jsonObjectRequest);
                }
            }
        });
    }
}
