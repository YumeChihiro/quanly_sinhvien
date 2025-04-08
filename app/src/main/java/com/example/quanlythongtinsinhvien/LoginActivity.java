package com.example.quanlythongtinsinhvien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlythongtinsinhvien.dao.DaoUser;
import com.example.quanlythongtinsinhvien.entities.QuanLySinhVien;
import com.example.quanlythongtinsinhvien.entities.UserEntity;

import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    Button login;
    EditText  passwordUser,emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Action();
    }

    public void init() {
         emailUser =(EditText) findViewById(R.id.emailUser);
         passwordUser =(EditText) findViewById(R.id.passwordUser);
         login =(Button) findViewById(R.id.login);
   }

    public void Action() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailUser.getText().toString();
                String password = passwordUser.getText().toString();

                Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
                Matcher matcher = emailPattern.matcher(email);

                if (emailUser.getText().toString().isEmpty()) {
                    emailUser.setError("Vui lòng nhập Email");
                    emailUser.requestFocus();
                    return;
                } else if (!matcher.matches()) { // Kiểm tra nếu không hợp lệ
                    emailUser.setError("Định dạng không hợp lệ");
                    emailUser.requestFocus();
                    return;
                }

                if (passwordUser.getText().toString().isEmpty()) {
                    passwordUser.setError("Vui lòng nhập mật khẩu");
                    passwordUser.requestFocus();
                    return;
                }

                try {
                    Executors.newSingleThreadExecutor().execute(() -> {

//                        UserDatabase userDatabase = UserDatabase.getDatabase(MainActivity.this);
//                        DaoUser daoUser = userDatabase.userDao();
//
//                        UserEntity userEntity = daoUser.getUserByEmailAndPassword(email, password);


                        QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(LoginActivity.this);
                        DaoUser daoUser = quanLySinhVien.daoUser();
                        UserEntity userEntity = daoUser.getUserByEmailAndPassword(email, password);



                        // Sau khi hoàn thành truy vấn, bạn cần quay lại luồng chính để cập nhật UI
                        runOnUiThread(() -> {
                            if (userEntity == null) {
                                Toast.makeText(getApplicationContext(), "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(LoginActivity.this, Home.class);
                                startActivity(intent);
                            }
                        });
                    });

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
