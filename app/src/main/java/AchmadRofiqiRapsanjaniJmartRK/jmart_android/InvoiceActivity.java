package AchmadRofiqiRapsanjaniJmartRK.jmart_android;
/**
 * Class InvoiceActivity - write a description of the class here
 *
 * @author Achmad Rofiqi Rapsanjani
 * @version
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import AchmadRofiqiRapsanjaniJmartRK.jmart_android.model.Payment;
import AchmadRofiqiRapsanjaniJmartRK.jmart_android.model.Product;

public class InvoiceActivity extends AppCompatActivity implements MyRecyclerViewInvoicesAdapter.ItemClickListener, MyRecyclerViewTransactionsAdapter.ItemClickListener {
    private static final Gson gson = new Gson();
    MyRecyclerViewInvoicesAdapter adapterInvoices;
    MyRecyclerViewTransactionsAdapter adapterTransactions;
    //TabSelector
    private TabLayout invoiceTabLayout;
    private CardView cv_myTransactions, cv_storeInvoices;

    private int pageT, pageS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue2 = Volley.newRequestQueue(this);

        invoiceTabLayout = findViewById(R.id.invoiceTabLayout);
        cv_myTransactions = findViewById(R.id.cv_myTransactions);
        cv_storeInvoices = findViewById(R.id.cv_storeInvoices);
        //Tab Selector Listener
        invoiceTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        cv_myTransactions.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        cv_storeInvoices.setVisibility(View.VISIBLE);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        cv_myTransactions.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        cv_storeInvoices.setVisibility(View.INVISIBLE);
                        break;
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { } //Reselect Tab unused.
        });
        try{
            Thread.sleep(500);
            //Request to fetch Store Invoices
            List<Payment> storeInvoices = new ArrayList<>();
            List<Product> invoiceProducts = new ArrayList<>();
            pageS = 0;
            fetchInvoices(storeInvoices, pageS, queue, true);
            fetchInvoiceProducts(invoiceProducts, pageS, queue2, true);
            RecyclerView recyclerInvoices = findViewById(R.id.rv_storeInvoices);
            recyclerInvoices.setLayoutManager(new LinearLayoutManager(this));
            adapterInvoices = new MyRecyclerViewInvoicesAdapter(this, storeInvoices, invoiceProducts);
            adapterInvoices.setClickListener(this);
            recyclerInvoices.setAdapter(adapterInvoices);
            recyclerInvoices.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }catch (Exception e){
            e.printStackTrace();
            finish();
        }

        try{
            Thread.sleep(500);
            //Request to fetch My Transactions
            List<Payment> myTransactions = new ArrayList<>();
            List<Product> transactionProducts = new ArrayList<>();
            pageT = 0;
            fetchTransactions(myTransactions, pageT, queue, true);
            fetchTransactionProducts(transactionProducts, pageT, queue, true);
            RecyclerView recyclerTransactions = findViewById(R.id.rv_transactions);
            recyclerTransactions.setLayoutManager(new LinearLayoutManager(this));
            adapterTransactions = new MyRecyclerViewTransactionsAdapter(this, myTransactions, transactionProducts);
            adapterTransactions.setClickListener(this);
            recyclerTransactions.setAdapter(adapterTransactions);
            recyclerTransactions.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //Add divider to each row
        }catch (Exception e){
            e.printStackTrace();
            finish();
        }

    }

    //Fetch Invoice Request Method
    public void fetchTransactions(List<Payment> myTransactions, int page, RequestQueue queue, boolean refreshAdapter){
        StringRequest fetchProductsRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/payment/"+LoginActivity.getLoggedAccount().id+"/purchases/page?page="+page+"&pageSize=10", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonReader reader = new JsonReader(new StringReader(response));
                try {
                    myTransactions.clear();
                    reader.beginArray();
                    while(reader.hasNext()){
                        myTransactions.add(gson.fromJson(reader, Payment.class));
                    }
                    Toast.makeText(getApplicationContext(), "Fetch transactions succesful!", Toast.LENGTH_LONG).show();
                    if(refreshAdapter){
                        adapterTransactions.refresh(myTransactions);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fetch transactions unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch transactions unsuccessful, error occurred", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(fetchProductsRequest);
    }

    //Fetch Invoice Request Method
    public void fetchInvoices(List<Payment> storeInvoices, int page, RequestQueue queue, boolean refreshAdapter){
        StringRequest fetchProductsRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/payment/"+LoginActivity.getLoggedAccount().id+"/page?page="+page+"&pageSize=10", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonReader reader = new JsonReader(new StringReader(response));
                try {
                    storeInvoices.clear();
                    reader.beginArray();
                    while(reader.hasNext()){
                        storeInvoices.add(gson.fromJson(reader, Payment.class));
                    }
                    Toast.makeText(getApplicationContext(), "Fetch invoices succesful!", Toast.LENGTH_LONG).show();
                    if(refreshAdapter){
                        adapterInvoices.refresh(storeInvoices);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fetch invoices unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch invoices unsuccessful, error occurred", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(fetchProductsRequest);
    }
    //Fetch Transactions Product Method
    public void fetchInvoiceProducts(List<Product> invoiceProducts, int page, RequestQueue queue, boolean refreshAdapter){
        StringRequest fetchInvoiceProductsRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/product/"+LoginActivity.getLoggedAccount().id+"/page?page="+page+"&pageSize=10", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonReader reader = new JsonReader(new StringReader(response));
                try {
                    invoiceProducts.clear();
                    reader.beginArray();
                    while(reader.hasNext()){
                        invoiceProducts.add(gson.fromJson(reader, Product.class));
                    }
                    if(refreshAdapter){
                        adapterTransactions.refreshProducts(invoiceProducts);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(fetchInvoiceProductsRequest);
    }

    //Fetch Transactions Product Method
    public void fetchTransactionProducts(List<Product> transactionProducts, int page, RequestQueue queue2, boolean refreshAdapter){
        StringRequest fetchTransactionProductsRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/product/"+LoginActivity.getLoggedAccount().id+"/purchases/page?page="+page+"&pageSize=10", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonReader reader = new JsonReader(new StringReader(response));
                try {
                    transactionProducts.clear();
                    reader.beginArray();
                    while(reader.hasNext()){
                        transactionProducts.add(gson.fromJson(reader, Product.class));
                    }
                    if(refreshAdapter){
                        adapterTransactions.refreshProducts(transactionProducts);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
            }
        });
        queue2.add(fetchTransactionProductsRequest);
    }


    //RecycleView Item ClickListener
    @Override
    public void onItemClick(View view, int position) {
        int clickedItemId = adapterTransactions.getClickedItemId(position);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
    }
}