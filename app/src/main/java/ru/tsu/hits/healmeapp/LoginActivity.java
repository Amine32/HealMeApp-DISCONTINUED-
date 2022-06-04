package ru.tsu.hits.healmeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "HealMe";
    private Button btnToRegister;
    private Button btnLogin;
    private EditText txtEmail;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnToRegister = (Button) findViewById(R.id.btnToRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtEmail = (EditText) findViewById(R.id.txtEmailLogin);
        txtPassword = (EditText) findViewById(R.id.txtPasswordLogin);

        txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "apis/api/users/login.php";

                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_Email", txtEmail.getText().toString());
                    jsonObject.put("user_Password", txtPassword.getText().toString());
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
                                    @NonNull final String token = response.getString("token");
                                    @NonNull final String message = response.getString("message");
                                    @NonNull final String user_ID = response.getString("user_ID");

                                    Log.d(TAG, "Token: " + token);
                                    Log.d(TAG, "Message: " + message);
                                    Log.d(TAG, "User_ID: " + user_ID);
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

                                    /*if (token.equals("healMeSuccess")) {
                                        SaveSharedPreference.setUserID(LoginActivity.this,user_ID);

                                        Intent intent = new Intent(LoginActivity.this, ToHome.class);
                                        startActivity(intent);
                                    }*/

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(@NonNull VolleyError error) {
                                Toast.makeText(getBaseContext(), "Failed to Login", Toast.LENGTH_LONG).show();
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
                Volley.newRequestQueue(LoginActivity.this).add(jsonObjectRequest);
            }
        });
    }
}