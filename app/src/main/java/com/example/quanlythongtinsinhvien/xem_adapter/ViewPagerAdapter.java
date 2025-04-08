package com.example.quanlythongtinsinhvien.xem_adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quanlythongtinsinhvien.entities.SinhVien;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<String> allMaLop;  // Danh sách mã lớp
    private List<List<SinhVien>> allSinhVienList; // Danh sách sinh viên cho mỗi lớp

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> allMaLop, List<List<SinhVien>> allSinhVienList) {
        super(fragmentActivity);
        this.allMaLop = allMaLop;
        this.allSinhVienList = allSinhVienList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Tạo một Fragment cho mỗi tab
        Fragment fragment = new SimpleFragment();

        // Lấy danh sách sinh viên của lớp tại vị trí `position`
        List<SinhVien> sinhVienList = allSinhVienList.get(position);

        // Truyền dữ liệu vào Bundle
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("sinhVienList", new ArrayList<>(sinhVienList)); // Truyền danh sách sinh viên vào Fragment
        bundle.putString("maLop", allMaLop.get(position)); // Truyền mã lớp vào Fragment
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return allMaLop.size(); // Trả về số lượng tab từ danh sách lớp
    }
}
