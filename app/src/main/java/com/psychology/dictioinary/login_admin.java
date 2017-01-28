package com.psychology.dictioinary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login_admin extends AppCompatActivity {


    String username, pass;
    private EditText editUsername;
    private EditText editPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        editUsername = (EditText) findViewById(R.id.edtUsername);
        editUsername.getText();
        editPassword = (EditText) findViewById(R.id.edtPassword);
        editPassword.getText();
        btnLogin = (Button) findViewById(R.id.btnLogin);

        username = "uas";
        pass = "ppb";

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (view == btnLogin) {
                    if (editUsername.getText().toString().equals(username) && editPassword.getText().toString().equals(pass)) {
                        Toast.makeText(getApplicationContext(), "Login Sukses", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(login_admin.this, MainActivity_2.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_LONG).show();
                        editUsername.setText("");
                        editPassword.setText("");
                        editUsername.requestFocus();
                    }
                }
            }
        });
    }
}
