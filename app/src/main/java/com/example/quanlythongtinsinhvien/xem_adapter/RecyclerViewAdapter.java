package com.example.quanlythongtinsinhvien.xem_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.entities.SinhVien;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<SinhVien> listSinhVien;

    public RecyclerViewAdapter(List<SinhVien> listSinhVien) {
        this.listSinhVien = listSinhVien;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.xem_sinhvien_adapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SinhVien sinhVien = listSinhVien.get(position);
        if(sinhVien != null){
            holder.txtTenSinhVien.setText(sinhVien.getHoTen());
            holder.txtMssv.setText(sinhVien.getMsSV());
            holder.email.setText(sinhVien.getEmail());
            holder.diachi.setText(sinhVien.getDiaChi());
            if(sinhVien.isGioiTinh()){
                holder.gioitinh.setText("Nam");
            }else{
                holder.gioitinh.setText("Nữ");
            }

            holder.arrowIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.linearLayoutSV.getVisibility() == view.GONE){
                        holder.arrowIcon.setImageResource(R.drawable.ic_up);
                        holder.linearLayoutSV.setVisibility(View.VISIBLE);
                    }else{
                        holder.arrowIcon.setImageResource(R.drawable.ic_arrow_down);
                        holder.linearLayoutSV.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listSinhVien.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSinhVien, txtMssv;
        EditText email,gioitinh, diachi;
        ImageView arrowIcon;
        LinearLayout linearLayoutSV;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTenSinhVien = itemView.findViewById(R.id.txtTenSinhVien);
            txtMssv = itemView.findViewById(R.id.txtMssv);
            email = itemView.findViewById(R.id.email);
            gioitinh = itemView.findViewById(R.id.gioitinh);
            diachi = itemView.findViewById(R.id.diachi);
            arrowIcon = itemView.findViewById(R.id.arrowIcon);
            linearLayoutSV = itemView.findViewById(R.id.linearLayoutSV);
        }
    }
}
