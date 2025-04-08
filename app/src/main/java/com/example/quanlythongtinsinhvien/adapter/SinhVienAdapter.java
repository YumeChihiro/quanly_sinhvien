package com.example.quanlythongtinsinhvien.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.entities.SinhVien;

import java.util.List;

public class SinhVienAdapter extends RecyclerView.Adapter<SinhVienAdapter.SinhVienViewHolder> {
    private List<SinhVien> sinhVienList;

    public SinhVienAdapter(List<SinhVien> sinhVienList) {
        this.sinhVienList = sinhVienList;
    }

    @NonNull
    @Override
    public SinhVienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sinhvien, parent, false);
        return new SinhVienViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SinhVienViewHolder holder, int position) {
        if (sinhVienList != null) {
            SinhVien currentSv = sinhVienList.get(position);

            holder.textViewTen.setText(currentSv.getHoTen());
            holder.textViewMasv.setText(currentSv.getMsSV());
            holder.textViewLop.setText(currentSv.getMaLop());
        } else {
            holder.textViewTen.setText("N/A");
            holder.textViewMasv.setText("N/A");
            holder.textViewLop.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return sinhVienList.size();
    }

    public void setSinhVienList(List<SinhVien> sinhVienList) {
        this.sinhVienList = sinhVienList;
        notifyDataSetChanged();
    }

    static class SinhVienViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTen;
        private final TextView textViewMasv;
        private final TextView textViewLop;

        public SinhVienViewHolder(View itemView) {
            super(itemView);
            textViewTen = itemView.findViewById(R.id.text_view_ten);
            textViewMasv = itemView.findViewById(R.id.text_view_masv);
            textViewLop = itemView.findViewById(R.id.text_view_lop);
        }
    }
}
