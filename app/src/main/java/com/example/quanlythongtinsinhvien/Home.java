package com.example.quanlythongtinsinhvien;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.quanlythongtinsinhvien.diem.Them_Diem;
import com.example.quanlythongtinsinhvien.phantich.PhanTich_Khoa;
import com.example.quanlythongtinsinhvien.sinhvien_activitis.BangVang;
import com.example.quanlythongtinsinhvien.sinhvien_activitis.Sua_SinhVien;
import com.example.quanlythongtinsinhvien.sinhvien_activitis.Them_SinhVien;
import com.example.quanlythongtinsinhvien.sinhvien_activitis.Xem_SinhVien;
import com.example.quanlythongtinsinhvien.sinhvien_activitis.Xoa_SinhVien;
import com.example.quanlythongtinsinhvien.tracuu.TraCuu;
import com.example.quanlythongtinsinhvien.trichxuat.TrichXuat;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

public class Home extends AppCompatActivity {
    ImageView logout;
    CardView bangVang, quanly_sinhvien, quanly_diemso, tracuu_hoctap, phantich_hoctap, thongke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        Action();
    }

    public void init(){
        logout = (ImageView) findViewById(R.id.logout);
        bangVang = (CardView) findViewById(R.id.bangVang);
        quanly_sinhvien = (CardView) findViewById(R.id.quanly_sinhvien);
        quanly_diemso = (CardView) findViewById(R.id.quanly_diemso);
        tracuu_hoctap = (CardView) findViewById(R.id.tracuu_hoctap);
        phantich_hoctap = (CardView) findViewById(R.id.phantich_hoctap);
        thongke = (CardView) findViewById(R.id.thongke);
    }

    public void Action(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });

        bangVang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, BangVang.class);
                startActivity(intent);
            }
        });

        quanly_sinhvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        quanly_diemso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Them_Diem.class);
                startActivity(intent);
            }
        });

        tracuu_hoctap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, TraCuu.class);
                startActivity(intent);
            }
        });

        phantich_hoctap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, PhanTich_Khoa.class);
                startActivity(intent);
            }
        });

        thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, TrichXuat.class);
                startActivity(intent);
            }
        });
    }

    private void showLogoutDialog() {
        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Home.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showBottomSheet() {
        // Tạo bottom sheet dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // Inflate layout riêng của bottom sheet
        View bottomSheetView = getLayoutInflater().inflate(R.layout.qlsv_option, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Tìm các view trong bottom sheet
        MaterialCardView option1 = bottomSheetView.findViewById(R.id.option_1);
        MaterialCardView option2 = bottomSheetView.findViewById(R.id.option_2);
        MaterialCardView option3 = bottomSheetView.findViewById(R.id.option_3);
        MaterialCardView option4 = bottomSheetView.findViewById(R.id.option_4);

        TextView closeButton = bottomSheetView.findViewById(R.id.close_button);

        closeButton.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        option1.setOnClickListener(v -> {
            Intent xem = new Intent(Home.this, Xem_SinhVien.class);
            startActivity(xem);
            bottomSheetDialog.dismiss();
        });

        option2.setOnClickListener(v -> {
            Intent them = new Intent(Home.this, Them_SinhVien.class);
            startActivity(them);
            bottomSheetDialog.dismiss();
        });

        option3.setOnClickListener(v -> {
            Intent sua = new Intent(Home.this, Sua_SinhVien.class);
            startActivity(sua);
            bottomSheetDialog.dismiss();
        });

        option4.setOnClickListener(v -> {
            Intent them = new Intent(Home.this, Xoa_SinhVien.class);
            startActivity(them);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}