package com.example.pushnotification.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;

import com.androidnetworking.AndroidNetworking;
import com.bumptech.glide.Glide;
import com.example.pushnotification.R;
import com.example.pushnotification.activity.MainActivity;
import com.example.pushnotification.base.BaseFragment;
import com.example.pushnotification.base.Utils;
import com.example.pushnotification.databinding.ContributeFragmentBinding;
import com.example.pushnotification.model.Product;
import com.example.pushnotification.view.view_model.ScanQRViewModel;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ContributeFragment extends BaseFragment<ContributeFragmentBinding> {

    private Context context;
    private Product product;
    private ScanQRViewModel viewModel;
    private Uri uriFile = null;

    public static ContributeFragment newInstance(Context context, Product product) {
        Bundle args = new Bundle();
        ContributeFragment fragment = new ContributeFragment();
        fragment.context = context;
        fragment.viewModel = new ViewModelProvider((MainActivity) context).get(ScanQRViewModel.class);
        fragment.product = product;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initObserver() {
        viewModel.getBitmapMutableLiveData().observe((MainActivity) context, bitmap -> {
            if (bitmap != null) {
                uriFile = bitmap;
                Glide.with(context).load(uriFile).into(binding.img);
                updateImage(bitmap);
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                Utils.hideKeyboard((MainActivity) context);
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initFragment(View view) {
        AndroidNetworking.initialize(context);
        setupUI(binding.rootView);
        ((MainActivity) context).setPageTitle("Đóng góp sản phẩm");
        ((MainActivity) context).showBottomNavigation(false);
        binding.setItem(product);
        binding.imgClose.setOnClickListener(v -> {
            ((MainActivity) context).showBottomNavigation(firebaseUser != null);
            ((MainActivity) context).setPageTitle("QR Code");
            Utils.hideKeyboard((MainActivity) context);
            fragmentManager.popBackStackImmediate();
        });

        binding.c1.setOnClickListener(v -> {
            final Calendar newCalendar = Calendar.getInstance();
            final DatePickerDialog StartTime = new DatePickerDialog(context, (view1, year, monthOfYear, dayOfMonth) -> {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                binding.c1.setText(dateFormatter.format(newDate.getTime()));
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            StartTime.show();
        });

        binding.c2.setOnClickListener(v -> {
            final Calendar newCalendar = Calendar.getInstance();
            final DatePickerDialog StartTime = new DatePickerDialog(context, (view1, year, monthOfYear, dayOfMonth) -> {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                binding.c2.setText(dateFormatter.format(newDate.getTime()));
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            StartTime.show();
        });

        binding.submit.setOnClickListener(v -> {
            updateImage(uriFile);
        });

        binding.img.setOnClickListener(v -> {

            final RxPermissions rxPermissions = new RxPermissions((MainActivity) context); // where this is an Activity or Fragment instance
            rxPermissions
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) { // Always true pre-M
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            ((MainActivity) context).startActivityForResult(intent, 200);
                        }
                    });
        });
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(input(binding.productId)) || TextUtils.isEmpty(input(binding.tvName)) ||
                TextUtils.isEmpty(input(binding.tvLoaisp)) || TextUtils.isEmpty(input(binding.tvDn)) ||
                TextUtils.isEmpty(input(binding.tvDiachi)) || TextUtils.isEmpty(input(binding.tvPhone)) ||
                TextUtils.isEmpty(input(binding.tvMathue)) || TextUtils.isEmpty((binding.c1.getText().toString()))
                || TextUtils.isEmpty((binding.c2.getText().toString())) || TextUtils.isEmpty(input(binding.tvGia)) ||
                TextUtils.isEmpty(input(binding.tvTrongluong)) || TextUtils.isEmpty(input(binding.tVMota)))
            return false;
        return true;
    }

    private String input(AppCompatEditText editText) {
        return Utils.formatString(editText);
    }

    public void updateImage(Uri uriImage) {
        if (uriFile == null || !isValid()) {
            Toast.makeText(context, "Nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        String imagepath = Utils.getRealPathFromURI(uriImage, (MainActivity) context);
        if (imagepath == null) {
            Toast.makeText(context, "Ko tìm thấy đường dẫn ảnh, Vui lòng chọn ảnh khác !", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(imagepath);
        viewModel.postContribute(context, fragmentManager, file, stringStringMap());
    }

    private Map<String, String> stringStringMap() {
        Date currentTime = Calendar.getInstance().getTime();
        String date;
        String pattern = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.format(currentTime);
        Map<String, String> map = new HashMap<>();
        map.put("MaSanPham", binding.productId.getText().toString());
        map.put("TenSanPham", binding.tvName.getText().toString());
        map.put("LoaiSanPhamId", binding.tvLoaisp.getText().toString());
        map.put("TenDoanhNghiep", binding.tvDn.getText().toString());
        map.put("DiaChi", binding.tvDiachi.getText().toString());
        map.put("SoDienThoai", binding.tvPhone.getText().toString());
        map.put("Email", firebaseUser.getEmail());
        map.put("MaSoThue", binding.tvMathue.getText().toString());
        map.put("C1", binding.c1.getText().toString());
        map.put("C2", binding.c2.getText().toString());
        map.put("GiaSanPham", binding.tvGia.getText().toString());
        map.put("TrongLuong", binding.tvTrongluong.getText().toString());
        map.put("MoTa", binding.tVMota.getText().toString());
        map.put("CreatedDate", date);
        map.put("CreatedBy", firebaseUser != null ? firebaseUser.getEmail() : "");
        return map;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.contribute_fragment;
    }
}
