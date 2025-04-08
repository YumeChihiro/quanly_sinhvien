package com.example.quanlythongtinsinhvien.xem_adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.entities.SinhVien;

import java.util.ArrayList;
import java.util.List;

public class SimpleFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<SinhVien> sinhvienList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xem_sinh_vien, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Lấy dữ liệu từ Bundle
        if (getArguments() != null) {
            sinhvienList = getArguments().getParcelableArrayList("sinhVienList");
        } else {
            sinhvienList = new ArrayList<>();
        }

        // Cài đặt LayoutManager cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(sinhvienList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
