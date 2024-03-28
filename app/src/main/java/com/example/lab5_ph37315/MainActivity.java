package com.example.lab5_ph37315;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lab5_ph37315.Adapter.Adapter_Product;
import com.example.lab5_ph37315.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Button btnLogout;
    private RecyclerView rcvListProduct;
    private FloatingActionButton btnAdd;
    private List<Product> list;
    private Adapter_Product adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        list = new ArrayList<>();
        btnAdd = findViewById(R.id.btnAddProduct);
        rcvListProduct = findViewById(R.id.rcvListProduct);
        btnLogout = findViewById(R.id.btnLogOut);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd();
            }
        });

        showListPro();









        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,Login_Activity.class));
                finish();

            }
        });
    }

    private void showListPro() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.104:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductsApiService apiService = retrofit.create(ProductsApiService.class);
        Call<List<Product>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.i("CALL", String.valueOf(list.size()));
                list = response.body();
                rcvListProduct.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                adapter = new Adapter_Product(MainActivity.this,list);
                rcvListProduct.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("ERR", t.getMessage());
            }
        });

    }

    private void showDialogAdd() {
        Log.i("Vao dialog","q");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add,null);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtGia = view.findViewById(R.id.edtGia);
        Button btnThem = view.findViewById(R.id.btnAdd_Update);
        Button btnExit = view.findViewById(R.id.btnQuaylai);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                Double gia = Double.parseDouble(edtGia.getText().toString().trim());
                if(name.length()==0||gia<=0){
                    Toast.makeText(MainActivity.this, "Khong de trong du lieu", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Product pro = new Product(name,gia);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.168.0.104:3000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    ProductsApiService apiService = retrofit.create(ProductsApiService.class);
                    Call<Product> call = apiService.newProduct(pro);
                    call.enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> callback, Response<Product> response) {
                            if(response.isSuccessful()){
                                response.body();
                                Toast.makeText(MainActivity.this, "Them thanh cong", Toast.LENGTH_SHORT).show();
                                list.clear();
                                showListPro();
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();

                            }

                        }

                        @Override
                        public void onFailure(Call<Product> call, Throwable t) {

                        }
                    });
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}