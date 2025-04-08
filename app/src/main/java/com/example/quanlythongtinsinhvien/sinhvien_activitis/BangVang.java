package com.example.quanlythongtinsinhvien.sinhvien_activitis;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.adapter.SinhVienAdapter;
import com.example.quanlythongtinsinhvien.dao.DaoSinhVien;
import com.example.quanlythongtinsinhvien.dao.DaoSv_Hk;
import com.example.quanlythongtinsinhvien.entities.QuanLySinhVien;
import com.example.quanlythongtinsinhvien.entities.SinhVien;
import com.example.quanlythongtinsinhvien.entities.SinhVien_HocKi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class BangVang extends AppCompatActivity {
    private Button hk1, hk2, hk3, Back;
    private RecyclerView recyclerViewSinhVien;
    private SinhVienAdapter sinhVienAdapter;
    ImageButton btn_back;
    private String selectedHocKy = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang_vang);
        init();
        setupRecyclerView();
        setupClickListeners();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
        hk1 = findViewById(R.id.hk1);
        hk2 = findViewById(R.id.hk2);
        hk3 = findViewById(R.id.hk3);
        recyclerViewSinhVien = findViewById(R.id.recycler_view_sinhvien);
        sinhVienAdapter = new SinhVienAdapter(new ArrayList<>());
        btn_back = (ImageButton) findViewById(R.id.btn_back);
    }

    private void setupRecyclerView() {
        recyclerViewSinhVien.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSinhVien.setHasFixedSize(true);
        recyclerViewSinhVien.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewSinhVien.setAdapter(sinhVienAdapter);
    }

    private void setupClickListeners() {
        hk1.setOnClickListener(v -> updateSelectedHocKy("1", hk1));
        hk2.setOnClickListener(v -> updateSelectedHocKy("2", hk2));
        hk3.setOnClickListener(v -> updateSelectedHocKy("3", hk3));

        updateSelectedHocKy("1", hk1);
    }

    private void updateSelectedHocKy(String hocKy, Button selectedButton) {
        selectedHocKy = hocKy;

        hk1.setBackgroundTintList(getResources().getColorStateList(R.color.default_button_color));
        hk2.setBackgroundTintList(getResources().getColorStateList(R.color.default_button_color));
        hk3.setBackgroundTintList(getResources().getColorStateList(R.color.default_button_color));
        hk1.setTextColor(getResources().getColor(R.color.default_text_color));
        hk2.setTextColor(getResources().getColor(R.color.default_text_color));
        hk3.setTextColor(getResources().getColor(R.color.default_text_color));

        selectedButton.setBackgroundTintList(getResources().getColorStateList(R.color.selected_button_color));
        selectedButton.setTextColor(getResources().getColor(R.color.selected_text_color));

        loadSinhVienData(hocKy);
    }

    private void loadSinhVienData(String hocKy) {
        try {
            Executors.newSingleThreadExecutor().execute(() -> {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(this);
                DaoSv_Hk daoSv_hk = quanLySinhVien.DaoSinhVienHocKi();
                DaoSinhVien daoSV = quanLySinhVien.DaoSinhVien();

                List<SinhVien_HocKi> listAllSV_HK = daoSv_hk.getAllSinhVienXuatSacByHocKi(hocKy);
                List<String> allMaSV = new ArrayList<>();
                for (SinhVien_HocKi sv_hk : listAllSV_HK) {
                    allMaSV.add(sv_hk.getMaSinhVien());
                }

                List<SinhVien> listSV = daoSV.getByIds(allMaSV);
                Collections.sort(listSV, (sv1, sv2) -> {
                    String ten1 = sv1.getHoTen().substring(sv1.getHoTen().lastIndexOf(" ") + 1);
                    String ten2 = sv2.getHoTen().substring(sv2.getHoTen().lastIndexOf(" ") + 1);
                    return ten1.compareTo(ten2);
                });

                runOnUiThread(() -> sinhVienAdapter.setSinhVienList(listSV));
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }
}