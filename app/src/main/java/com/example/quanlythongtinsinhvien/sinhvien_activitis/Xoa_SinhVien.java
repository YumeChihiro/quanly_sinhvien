package com.example.quanlythongtinsinhvien.sinhvien_activitis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.dao.DaoKhoa;
import com.example.quanlythongtinsinhvien.dao.DaoLop;
import com.example.quanlythongtinsinhvien.dao.DaoSinhVien;
import com.example.quanlythongtinsinhvien.entities.QuanLySinhVien;
import com.example.quanlythongtinsinhvien.entities.SinhVien;

import java.util.concurrent.Executors;

public class Xoa_SinhVien extends AppCompatActivity {
    Button button_delete;
    EditText edit_text_search_masv;
    ImageButton btn_search, btn_back;
    TextView text_view_khoa, text_view_lop, text_view_masv, text_view_ten, text_view_email;

    private SinhVien sinhVienXoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xoa_sinh_vien);

        LottieAnimationView animationView = findViewById(R.id.animation);
        animationView.playAnimation();
        init();
        Action();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void init() {
        btn_search = findViewById(R.id.btn_search);
        button_delete = findViewById(R.id.button_delete);
        edit_text_search_masv = findViewById(R.id.edit_text_search_masv);
        text_view_email = findViewById(R.id.text_view_email);
        text_view_khoa = findViewById(R.id.text_view_khoa);
        text_view_lop = findViewById(R.id.text_view_lop);
        text_view_masv = findViewById(R.id.text_view_masv);
        text_view_ten = findViewById(R.id.text_view_ten);
        btn_back = (ImageButton) findViewById(R.id.btn_back);

        // Disable delete button initially
        button_delete.setEnabled(false);
    }

    public void Action() {
        sinhVienXoa = null;

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mssv = edit_text_search_masv.getText().toString().trim();
                if (mssv.isEmpty()) {
                    edit_text_search_masv.setError("Vui lòng nhập mã sinh viên");
                    edit_text_search_masv.requestFocus();
                    return;
                }

                clearFields();
                button_delete.setEnabled(false);

                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Xoa_SinhVien.this);
                        DaoSinhVien daoSinhVien = quanLySinhVien.DaoSinhVien();

                        SinhVien sv = daoSinhVien.getById(mssv);

                        if (sv == null) {
                            runOnUiThread(() -> {
                                Toast.makeText(Xoa_SinhVien.this,
                                        "Không tìm thấy sinh viên với mã " + mssv,
                                        Toast.LENGTH_LONG).show();
                                edit_text_search_masv.setError("Mã sinh viên không tồn tại");
                                edit_text_search_masv.requestFocus();
                            });
                            return;
                        }

                        DaoLop daoLop = quanLySinhVien.DaoLop();
                        DaoKhoa daoKhoa = quanLySinhVien.DaoKhoa();

                        String maLop = sv.getMaLop();
                        String tenLop = daoLop.getTenLopById(maLop);
                        String khoaId = daoLop.getKhoaByMaLop(maLop);
                        String tenKhoa = daoKhoa.getTenKhoaByMaKhoa(khoaId);

                        runOnUiThread(() -> {
                            text_view_khoa.setText(tenKhoa);
                            text_view_lop.setText(tenLop);
                            text_view_masv.setText(sv.getMsSV());
                            text_view_email.setText(sv.getEmail());
                            text_view_ten.setText(sv.getHoTen());

                            // Enable delete button after successful search
                            button_delete.setEnabled(true);
                            sinhVienXoa = sv;
                        });

                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(Xoa_SinhVien.this,
                                    "Lỗi tìm kiếm: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            clearFields();
                        });
                    }
                });
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sinhVienXoa != null) {
                    showDeleteConfirmationDialog();
                }
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        // Tạo dialog từ layout XML tùy chỉnh
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_student, null);

        AlertDialog dialog = builder.setView(dialogView).create();
        dialog.show();

        // Ánh xạ các view từ layout
        ImageView dialogIcon = dialogView.findViewById(R.id.dialog_icon);
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
        Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);
        Button deleteButton = dialogView.findViewById(R.id.dialog_delete_button);

        // Tùy chỉnh nội dung
        dialogMessage.setText("Bạn có chắc chắn muốn xóa sinh viên " + sinhVienXoa.getHoTen() + " không?");

        // Xử lý sự kiện nút Hủy
        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // Xử lý sự kiện nút Xóa
        deleteButton.setOnClickListener(v -> {
            dialog.dismiss();
            deleteStudent();
        });
    }

    private void deleteStudent() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Xoa_SinhVien.this);
                DaoSinhVien daoSinhVien = quanLySinhVien.DaoSinhVien();

                int result = daoSinhVien.delete(sinhVienXoa);

                runOnUiThread(() -> {
                    if (result > 0) {
                        Toast.makeText(getApplicationContext(),
                                "Đã xóa sinh viên " + sinhVienXoa.getHoTen(),
                                Toast.LENGTH_SHORT).show();
                        clearFields();
                        edit_text_search_masv.setText("");
                        button_delete.setEnabled(false);
                        sinhVienXoa = null;
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Không thể xóa sinh viên. Vui lòng thử lại!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(Xoa_SinhVien.this,
                            "Lỗi xóa sinh viên: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    public void clearFields() {
        text_view_khoa.setText("");
        text_view_lop.setText("");
        text_view_masv.setText("");
        text_view_email.setText("");
        text_view_ten.setText("");
    }
}