package ru.tsu.hits.healmeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

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


    }
}