package ru.tsu.hits.healmeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.tsu.hits.healmeapp.misc.CustomStringRequest;
import ru.tsu.hits.healmeapp.misc.SaveSharedPreference;
import ru.tsu.hits.healmeapp.misc.SimpleRequestQueueFactory;

public class RegisterFragment extends AppCompatActivity {

    private static final String TAG = "HealMe";
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtFirstName = (EditText) findViewById(R.id.txtFirstnameRegister);
        txtLastName = (EditText) findViewById(R.id.txtLastNameRegister);
        txtEmail = (EditText) findViewById(R.id.txtEmailRegister);
        txtPassword = (EditText) findViewById(R.id.txtPasswordRegister);
        btnRegister = (Button) findViewById(R.id.btnSignUpRegister);

        txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtFirstName.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill the first name", Toast.LENGTH_LONG).show();
                } else if (txtLastName.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill the last name", Toast.LENGTH_LONG).show();
                } else if (txtEmail.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill the email", Toast.LENGTH_LONG).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString().trim()).matches()) {
                    Toast.makeText(getBaseContext(), "Please enter a valid email address", Toast.LENGTH_LONG).show();
                } else if (txtPassword.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill the password", Toast.LENGTH_LONG).show();
                } else if (txtPassword.getText().toString().trim().length() < 6) {
                    Toast.makeText(getBaseContext(), "Password should be at least 6 characters", Toast.LENGTH_LONG).show();
                } else {
                    String url = "apis/api/users/create.php";

                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_FirstName", txtFirstName.getText().toString());
                        jsonObject.put("user_LastName", txtLastName.getText().toString());
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

                                        Log.d(TAG, "Token: " + token);
                                        Log.d(TAG, "Message: " + message);


                                        if (token.equals("healMeSuccess")) {

                                            String urlGetID = "apis/api/users/read_one_email.php?email=" + txtEmail.getText().toString();

                                            CustomStringRequest postRequest = new CustomStringRequest(Request.Method.GET, urlGetID, token, onSuccess, onFailure);

                                            try {
                                                SimpleRequestQueueFactory factory = SimpleRequestQueueFactory.getInstance(getApplicationContext());
                                                RequestQueue queue = factory.getQueueInstance();
                                                queue.add(postRequest);

                                            } catch (Exception e) {
                                                Log.e("Volley", e.getMessage());
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(@NonNull VolleyError error) {
                                    Toast.makeText(getBaseContext(), "Failed to Register", Toast.LENGTH_LONG).show();
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
                    Volley.newRequestQueue(RegisterFragment.this).add(jsonObjectRequest);
                }
            }
        });
    }

    Response.Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                String user_ID = jsonObject.getString("user_ID");
                Log.e("Volley", user_ID);
                Toast.makeText(getBaseContext(), "Account Created", Toast.LENGTH_LONG).show();

                SaveSharedPreference.setUserID(RegisterFragment.this, user_ID);

                Intent intent = new Intent(RegisterFragment.this, ToHomeActivity.class);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };

    Response.ErrorListener onFailure = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getBaseContext(), "Failed to Register", Toast.LENGTH_LONG).show();
        }
    };
}
