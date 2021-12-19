package AchmadRofiqiRapsanjaniJmartRK.jmart_android;
/**
 * Class LoginActivity - write a description of the class here
 *
 * @author Achmad Rofiqi Rapsanjani
 * @version
 */

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

import AchmadRofiqiRapsanjaniJmartRK.jmart_android.model.Account;
import AchmadRofiqiRapsanjaniJmartRK.jmart_android.model.Store;
import AchmadRofiqiRapsanjaniJmartRK.jmart_android.request.LoginRequest;

public class LoginActivity extends AppCompatActivity{
    private static final Gson gson = new Gson();
    private static Account loggedAccount = null;
    private TextView tv_registerNow;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    //Method to get the currently logged account
    public static Account getLoggedAccount(){
        return loggedAccount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tv_registerNow = findViewById(R.id.tv_registerNow);


        RequestQueue queue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                LoginRequest loginRequest = new LoginRequest(email, password, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            loggedAccount = gson.fromJson(response, Account.class);
                            Toast.makeText(getApplicationContext(), "Login berhasil", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Login gagal, error terjadi", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error terjadi.", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(loginRequest);
            }
        });
        tv_registerNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRegisterActivity();
            }
        });
    }
    public void openRegisterActivity(){
        startActivity(new Intent(this, RegisterActivity.class));
    }
    //Method to reload the currently logged account after it's modified
    public static void reloadLoggedAccount(String response){
        loggedAccount = gson.fromJson(response, Account.class);
    }
    //Method to insert newly created Store data to a logged account;
    public static void insertLoggedAccountStore(String response){
        Store newStore = gson.fromJson(response, Store.class);
        loggedAccount.store = newStore;
    }
}