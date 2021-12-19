package AchmadRofiqiRapsanjaniJmartRK.jmart_android;

/**
 * Class RegisterActivity - write a description of the class here
 *
 * @author Achmad Rofiqi Rapsanjani
 * @version
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.FileNotFoundException;

import AchmadRofiqiRapsanjaniJmartRK.jmart_android.request.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_loginHere;
    private EditText etEmail;
    private EditText etName;
    private EditText etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        btnRegister = findViewById(R.id.btnRegister);

        RequestQueue queue = Volley.newRequestQueue(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String name = etName.getText().toString();
                RegisterRequest registerRequest = new RegisterRequest(name, email, password, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj != null){
                                Toast.makeText(getApplicationContext(), "Register berhasil", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Register tidak berhasil", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Register tidak berhasil, error terjadi", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Register tidak berhasil, error terjadi", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(registerRequest);
            }
        });

        tv_loginHere = (TextView) findViewById(R.id.tv_loginHere);
        tv_loginHere.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });
    }


    public void openLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
    }
}