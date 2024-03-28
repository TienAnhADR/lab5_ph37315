package com.example.lab5_ph37315.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_ph37315.ProductsApiService;
import com.example.lab5_ph37315.R;
import com.example.lab5_ph37315.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Adapter_Product extends RecyclerView.Adapter<Adapter_Product.ViewHolder> {
    private Context context;
    private List<Product> list;

    public Adapter_Product(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(position);
        holder.txtPriceProduct.setText("Price: "+product.getGia());
        holder.txtNameProduct.setText("Name: "+product.getName());
        holder.btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delPro(product.getId());
                list.remove(position);
                notifyDataSetChanged();

            }
        });
        holder.btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProduct(product.getId(),position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameProduct,txtPriceProduct;
         Button btnDeleteProduct,btnUpdateProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDeleteProduct = itemView.findViewById(R.id.btnDeleteProduct);
            btnUpdateProduct = itemView.findViewById(R.id.btnUpdateProduct);
            txtNameProduct = itemView.findViewById(R.id.txtNameProduct);
            txtPriceProduct = itemView.findViewById(R.id.txtPriceProduct);
        }
    }
    private void delPro(String id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.104:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductsApiService apiService = retrofit.create(ProductsApiService.class);
        Call<Void> call = apiService.deletePro(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "Xoa thanh cong", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("errr", t.getMessage().toString());

            }
        });
    }
    private void UpdateProduct (String id, int index){
        Log.i("Vao dialog","q");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add,null);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtGia = view.findViewById(R.id.edtGia);
        Button btnThem = view.findViewById(R.id.btnAdd_Update);
        Button btnExit = view.findViewById(R.id.btnQuaylai);
        TextView txtTitle = view.findViewById(R.id.titleAdd_Update);
        txtTitle.setText("Update Product");
        btnThem.setText("Update");
        builder.setView(view);
        AlertDialog dialog = builder.create();


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                Double gia = Double.parseDouble(edtGia.getText().toString().trim());
                if(name.length()==0||gia<=0){
                    Toast.makeText(context, "Khong de trong du lieu", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Product updatePro = new Product(name,gia);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.168.0.104:3000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    ProductsApiService apiService = retrofit.create(ProductsApiService.class);
                    Call<Product> call = apiService.updatePro(id,updatePro);
                    call.enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {
                            if(response.isSuccessful()){
                                response.body();
                                Toast.makeText(context, "update thanh cong", Toast.LENGTH_SHORT).show();
                                list.set(index,updatePro);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Product> call, Throwable t) {
                            Toast.makeText(context, "update that bai", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        dialog.show();
    }

}
