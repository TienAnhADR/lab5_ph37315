package com.example.lab5_ph37315;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DangKy_Activity extends AppCompatActivity {
    private Button btnSignUP,btnQuayLai;
    private EditText edtEmail,edtPass,edtPass2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnQuayLai = findViewById(R.id.btnQuayLaiLogin);
        btnSignUP = findViewById(R.id.btnSignUp);
        edtEmail = findViewById(R.id.edtEmailSignUp);
        edtPass = findViewById(R.id.edtPassSignUp);
        edtPass2 = findViewById(R.id.edtPassSignUp2);
        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPass.getText().toString().trim();
                String pass = edtPass2.getText().toString().trim();
                if(email.isEmpty()||pass.isEmpty()||password.isEmpty()){
                    Toast.makeText(DangKy_Activity.this, "Không để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.length()<6){
                    Toast.makeText(DangKy_Activity.this, "Mat khau phai it nhat 6 ky tu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass.equals(password)){
                    Toast.makeText(DangKy_Activity.this, "Mật khẩu không giống nhau", Toast.LENGTH_SHORT).show();
                    return;
                }

                signUp(email,password);

            }
        });
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangKy_Activity.this,Login_Activity.class));
            }
        });
    }
    private void signUp(String email,String password) {
        Log.i("Vao day: ",email);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("Vao DK: ","thanh cong");
                            Intent intent = new Intent(DangKy_Activity.this,MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i( "createUserWithEmail:failure", task.getException().toString());
                            Toast.makeText(DangKy_Activity.this, "Đăng ký fall", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}