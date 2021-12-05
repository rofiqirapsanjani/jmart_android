package AchmadRofiqiRapsanjaniJmartRK.jmart_android;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import AchmadRofiqiRapsanjaniJmartRK.jmart_android.request.CreateProductRequest;

public class CreateProductActivity extends AppCompatActivity {
    private Button btnCreateProduct, btnCancelProduct;
    private EditText et_createProductName, et_createProductPrice, et_createProductWeight, et_createProductDiscount;
    private RadioGroup radio_conditionList;
    private boolean newProductCondition = true;
    private Spinner spinner_createCategory, spinner_createShipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        RequestQueue queue = Volley.newRequestQueue(this);

        et_createProductName = findViewById(R.id.et_createProductName);
        et_createProductPrice = findViewById(R.id.et_createProductPrice);
        et_createProductWeight = findViewById(R.id.et_createProductWeight);
        et_createProductDiscount = findViewById(R.id.et_createProductDiscount);
        spinner_createCategory = findViewById(R.id.spinner_createCategory);
        spinner_createShipment = findViewById(R.id.spinner_createShipment);
        //Handle checking value of checked radiogroup buttons
        radio_conditionList = findViewById(R.id.radio_conditionList);
        radio_conditionList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_conditionNew:
                        newProductCondition = true;
                        break;
                    case R.id.radio_conditionUsed:
                        newProductCondition = false;
                        break;
                }
            }
        });
        btnCreateProduct = findViewById(R.id.btnCreateProduct);
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountId = String.valueOf(LoginActivity.getLoggedAccount().id);
                String productName = et_createProductName.getText().toString();
                String productPrice = et_createProductPrice.getText().toString();
                String productWeight = et_createProductWeight.getText().toString();
                String productDiscount = et_createProductDiscount.getText().toString();
                String productCategory = spinner_createCategory.getSelectedItem().toString();
                String productShipment = spinner_createShipment.getSelectedItem().toString();
                //Convert productShipment string value into byte values to be stored
                switch (productShipment) {
                    case "INSTANT":
                        productShipment = String.valueOf(0);
                        break;
                    case "SAME_DAY":
                        productShipment = String.valueOf(1);
                        break;
                    case "NEXT_DAY":
                        productShipment = String.valueOf(2);
                        break;
                    case "REGULER":
                        productShipment = String.valueOf(3);
                        break;
                    case "KARGO":
                        productShipment = String.valueOf(4);
                        break;
                    default:
                        productShipment = String.valueOf(3);
                        break;
                }
                System.out.println(productCategory + "  " + productShipment);
                CreateProductRequest createProductRequest = new CreateProductRequest(accountId, productName, productWeight,
                        String.valueOf(newProductCondition), productPrice, productDiscount, productCategory, productShipment,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Toast.makeText(getApplicationContext(), "Create product successful", Toast.LENGTH_LONG).show();
                                    finish();
                                    //If succesful, go back to/and reload the Main Activity
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Create product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Create product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(createProductRequest);
            }
        });
        btnCancelProduct = findViewById(R.id.btnCancelProduct);
        btnCancelProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}