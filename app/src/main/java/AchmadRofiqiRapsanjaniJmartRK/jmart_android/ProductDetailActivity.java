package AchmadRofiqiRapsanjaniJmartRK.jmart_android;


/**
 * Class ProductActivity - write a description of the class here
 *
 * @author Achmad Rofiqi Rapsanjani
 * @version
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import AchmadRofiqiRapsanjaniJmartRK.jmart_android.model.Product;
import AchmadRofiqiRapsanjaniJmartRK.jmart_android.request.RequestFactory;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PRICE = "AchmadRofiqiRapsanjaniJmartRK.jmart_android.EXTRA_PRICE";
    private Product fetchedProduct;
    private static final Gson gson = new Gson();
    private Button btnPurchase, btnBack;
    private TextView tv_detailName, tv_detailPrice, tv_detailWeight, tv_detailDiscount, tv_detailCategory, tv_detailShipment,
            tv_detailCondition, tv_detailAccount;
    private ImageView image_product;
    private double productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        RequestQueue queue = Volley.newRequestQueue(this);
        //Get passed product Id variable of clicked item from MainActivity
        Intent intent = getIntent();
        int productId = intent.getIntExtra(MainActivity.EXTRA_PRODUCTID, 0);

        btnPurchase = findViewById(R.id.btnPurchase);
        btnBack = findViewById(R.id.btnBack);
        tv_detailName = findViewById(R.id.tv_detailName);
        tv_detailPrice = findViewById(R.id.tv_detailPrice);
        tv_detailWeight = findViewById(R.id.tv_detailWeight);
        tv_detailDiscount = findViewById(R.id.tv_detailDiscount);
        tv_detailCategory = findViewById(R.id.tv_detailCategory);
        tv_detailShipment = findViewById(R.id.tv_detailShipment);
        tv_detailCondition = findViewById(R.id.tv_detailCondition);
        tv_detailAccount = findViewById(R.id.tv_detailAccount);
        image_product = findViewById(R.id.image_product);

        StringRequest fetchProductDataRequest = RequestFactory.getById("product", productId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fetchedProduct = gson.fromJson(response, Product.class);
                tv_detailName.setText(fetchedProduct.name);
                if(fetchedProduct.name.length() >= 28){
                    tv_detailName.setTextSize(18.5f);
                    tv_detailName.setMaxEms(10);
                }
                tv_detailPrice.setText(String.valueOf(Math.round(fetchedProduct.price * 100.00)/100.00));
                productPrice = Double.parseDouble(tv_detailPrice.getText().toString());
                tv_detailWeight.setText(String.valueOf(fetchedProduct.weight));
                tv_detailDiscount.setText(String.valueOf(Math.round(fetchedProduct.discount * 100.00)/100.00));
                tv_detailCategory.setText(fetchedProduct.category.toString());
                image_product.setImageResource(getDrawableId(fetchedProduct.category.toString()));
                switch (fetchedProduct.shipmentPlans){
                    case 0:
                        tv_detailShipment.setText("INSTANT");
                        break;
                    case 1:
                        tv_detailShipment.setText("SAME_DAY");
                        break;
                    case 2:
                        tv_detailShipment.setText("NEXT_DAY");
                        break;
                    case 4:
                        tv_detailShipment.setText("KARGO");
                        break;
                    default:
                        tv_detailShipment.setText("REGULER");
                        break;
                }
                tv_detailCondition.setText(fetchedProduct.conditionUsed ? "Used" : "New");
                tv_detailAccount.setText(String.valueOf(fetchedProduct.accountId));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        queue.add(fetchProductDataRequest);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(MainActivity.EXTRA_PRODUCTID, productId);
                intent.putExtra(EXTRA_PRICE, productPrice);
                startActivity(intent);
            }
        });
    }
    protected static int getDrawableId(String fetchedProductCategory){
        switch(fetchedProductCategory){
            case "BOOK":
                return R.drawable.book;
            case "KITCHEN":
                return R.drawable.kitchen;
            case "ELECTRONIC":
                return R.drawable.electronic;
            case "FASHION":
                return R.drawable.fashion;
            case "GAMING":
                return R.drawable.gaming;
            case "GADGET":
                return R.drawable.gadget;
            case "MOTHERCARE":
                return R.drawable.mothercare;
            case "COSMETICS":
                return R.drawable.cosmetics;
            case "HEALTHCARE":
                return R.drawable.healthcare;
            case "FURNITURE":
                return R.drawable.furniture;
            case "JEWELRY":
                return R.drawable.jewelry;
            case "TOYS":
                return R.drawable.toys;
            case "FNB":
                return R.drawable.fnb;
            case "STATIONERY":
                return R.drawable.stationery;
            case "SPORTS":
                return R.drawable.sports;
            case "AUTOMOTIVE":
                return R.drawable.automotive;
            case "PETCARE":
                return R.drawable.petcare;
            case "ART_CRAFT":
                return R.drawable.art_craft;
            case "CARPENTRY":
                return R.drawable.carpentry;
            case "PROPERTY":
                return R.drawable.property;
            case "TRAVEL":
                return R.drawable.travel;
            case "WEDDING":
                return R.drawable.wedding;
            default:
                return R.drawable.miscellaneous;
        }
    }
}