package AchmadRofiqiRapsanjaniJmartRK.jmart_android;
/**
 * Class PaymentActivity - write a description of the class here
 *
 * @author Achmad Rofiqi Rapsanjani
 * @version
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import AchmadRofiqiRapsanjaniJmartRK.jmart_android.model.Account;
import AchmadRofiqiRapsanjaniJmartRK.jmart_android.model.Product;
import AchmadRofiqiRapsanjaniJmartRK.jmart_android.request.CreatePaymentRequest;
import AchmadRofiqiRapsanjaniJmartRK.jmart_android.request.CreateProductRequest;
import AchmadRofiqiRapsanjaniJmartRK.jmart_android.request.RequestFactory;

public class PaymentActivity extends AppCompatActivity {
    private Product fetchedProduct;
    private static final Gson gson = new Gson();
    private double productPrice;
    private byte shipmentPlans;
    private Button btnSubmitPayment, btnCancelPayment;
    private ImageView image_productPayment;
    private TextView tv_productNamePayment, tv_categoryPayment, tv_pricePayment, tv_discountPayment, tv_sellerId;
    private TextView tv_totalPrice, tv_balancePayment, tv_shipmentPayment;
    private EditText et_amountPayment, et_shipmentAddress;
    private boolean enoughBalance = false;

//    private TextView tv_finalBalance, tv_weightPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        RequestQueue queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        int productId = intent.getIntExtra(MainActivity.EXTRA_PRODUCTID, 0);
        productPrice = intent.getDoubleExtra(ProductDetailActivity.EXTRA_PRICE, 0);
        Account currentLogged = LoginActivity.getLoggedAccount();

        tv_productNamePayment = findViewById(R.id.tv_productNamePayment);
        tv_categoryPayment = findViewById(R.id.tv_categoryPayment);
        tv_pricePayment = findViewById(R.id.tv_pricePayment);
        tv_discountPayment = findViewById(R.id.tv_discountPayment);
        tv_sellerId = findViewById(R.id.tv_sellerId);
        tv_totalPrice = findViewById(R.id.tv_totalPrice);
        tv_balancePayment = findViewById(R.id.tv_balancePayment);
//
        tv_shipmentPayment = findViewById(R.id.tv_shipmentPayment);
        image_productPayment = findViewById(R.id.image_productPayment);
        et_shipmentAddress = findViewById(R.id.et_shipmentAddress);
        et_amountPayment = findViewById(R.id.et_amountPayment);
        et_amountPayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int getNewAmount;
                try{
                    getNewAmount = Integer.parseInt(et_amountPayment.getText().toString());
                }catch(NumberFormatException e){
                    getNewAmount = 0;
                }
                if (!(getNewAmount > 0)) {
                    et_amountPayment.setText(String.valueOf(1));
                    tv_totalPrice.setText(String.valueOf(productPrice * 1));
                }else{
                    tv_totalPrice.setText(String.valueOf(getNewAmount * productPrice));
                }
            }
        });
        StringRequest fetchProductDataRequest = RequestFactory.getById("product", productId, new Response.Listener<String>() {
            int amount = Integer.parseInt(et_amountPayment.getText().toString());
            @Override
            public void onResponse(String response) {
                fetchedProduct = gson.fromJson(response, Product.class);
                tv_productNamePayment.setText(fetchedProduct.name);
                tv_categoryPayment.setText(fetchedProduct.category.toString());
                image_productPayment.setImageResource(ProductDetailActivity.getDrawableId(fetchedProduct.category.toString()));
                double productPrice = Math.round((fetchedProduct.price * 100.00)/100.00);
                double productDiscount = Math.round((fetchedProduct.discount * 100.00)/100.0);
                tv_pricePayment.setText(String.valueOf(productPrice));
                tv_discountPayment.setText(String.valueOf(productDiscount));
                tv_sellerId.setText(String.valueOf(fetchedProduct.accountId));
                tv_totalPrice.setText(String.valueOf(amount * (productPrice - productDiscount)));
                tv_balancePayment.setText(String.valueOf(currentLogged.balance));
                if(Double.parseDouble(tv_totalPrice.getText().toString()) > Double.parseDouble(tv_balancePayment.getText().toString())){
                    tv_balancePayment.setTextColor(Color.RED);
                    enoughBalance = false;
                }else{
                    enoughBalance = true;
                }
                shipmentPlans = fetchedProduct.shipmentPlans;
                switch (shipmentPlans){
                    case 0:
                        tv_shipmentPayment.setText("INSTANT");
                        break;
                    case 1:
                        tv_shipmentPayment.setText("SAME_DAY");
                        break;
                    case 2:
                        tv_shipmentPayment.setText("NEXT_DAY");
                        break;
                    case 4:
                        tv_shipmentPayment.setText("KARGO");
                        break;
                    default:
                        tv_shipmentPayment.setText("REGULER");
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        queue.add(fetchProductDataRequest);
        btnSubmitPayment = findViewById(R.id.btnSubmitPayment);
        btnCancelPayment = findViewById(R.id.btnCancelPayment);
        btnSubmitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InvoiceActivity.class);
                if(enoughBalance){
                    if(isEmpty(et_shipmentAddress)){
                        Toast.makeText(getApplicationContext(), "Shipment address can't be empty.", Toast.LENGTH_LONG).show();
                    }else{
                        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest(String.valueOf(LoginActivity.getLoggedAccount().id), String.valueOf(productId), et_amountPayment.getText().toString(), et_shipmentAddress.getText().toString(), String.valueOf(shipmentPlans),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            Toast.makeText(getApplicationContext(), "Payment has been submitted. Waiting seller's response", Toast.LENGTH_LONG).show();
                                            //startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "Create payment unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Create payment unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                            }
                        });
                        queue.add(createPaymentRequest);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Your balance is not enough for this purchase!", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancelPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}