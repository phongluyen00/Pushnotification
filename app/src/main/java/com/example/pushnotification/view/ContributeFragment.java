package com.example.pushnotification.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
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
import java.util.HashMap;
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

    @Override
    protected void initFragment(View view) {
        AndroidNetworking.initialize(context);
        ((MainActivity) context).showBottomNavigation(false);
        binding.setItem(product);
        binding.imgClose.setOnClickListener(v -> {
            ((MainActivity) context).showBottomNavigation(firebaseUser != null);
            ((MainActivity) context).setPageTitle("QR Code");
            Utils.hideKeyboard((MainActivity) context);
            fragmentManager.popBackStackImmediate();
        });

        binding.submit.setOnClickListener(v -> {
            updateImage(uriFile);
        });

        binding.img.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {

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
            }
        });
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(input(binding.productId)) || TextUtils.isEmpty(input(binding.tvName)) ||
                TextUtils.isEmpty(input(binding.tvLoaisp)) || TextUtils.isEmpty(input(binding.tvDn)) ||
                TextUtils.isEmpty(input(binding.tvDiachi)) || TextUtils.isEmpty(input(binding.tvPhone)) ||
                TextUtils.isEmpty(input(binding.tvEmail)) || TextUtils.isEmpty(input(binding.tvMathue)) ||
                TextUtils.isEmpty(input(binding.c1)) || TextUtils.isEmpty(input(binding.c2)) || TextUtils.isEmpty(input(binding.tvGia)) ||
                TextUtils.isEmpty(input(binding.tvTrongluong)) || TextUtils.isEmpty(input(binding.tVMota)) ||
                TextUtils.isEmpty(input(binding.tvCreateDate)) || TextUtils.isEmpty(input(binding.createBy)))
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
            Toast.makeText(context, "Lỗi file ảnh", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(imagepath);
        Map<String, String> map = new HashMap<>();
        map.put("MaSanPham", binding.productId.getText().toString());
        map.put("TenSanPham", "Sản Phẩm tét");
        map.put("LoaiSanPhamId", "1900");
        map.put("TenDoanhNghiep", binding.tvDn.getText().toString());
        map.put("DiaChi", binding.tvDiachi.getText().toString());
        map.put("SoDienThoai", binding.tvPhone.getText().toString());
        map.put("Email", binding.tvEmail.getText().toString());
        map.put("MaSoThue", binding.tvMathue.getText().toString());
        map.put("C1", binding.c1.getText().toString());
        map.put("C2", binding.c2.getText().toString());
        map.put("GiaSanPham", binding.tvGia.getText().toString());
        map.put("TrongLuong", binding.tvTrongluong.getText().toString());
        map.put("MoTa", binding.tVMota.getText().toString());
        map.put("CreatedDate", binding.tvCreateDate.getText().toString());
        map.put("CreatedBy", binding.createBy.getText().toString());
        ((MainActivity) context).progressLoader(true);
        AndroidNetworking.upload("http://68.183.226.200:3000/api/swu86/post")
                .addMultipartFile("file", file)
                .addMultipartParameter(map)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ((MainActivity) context).progressLoader(false);
                        Toast.makeText(context, "Tải lên thành công", Toast.LENGTH_SHORT).show();
                        Utils.hideKeyboard((MainActivity) context);
                        fragmentManager.popBackStackImmediate();
                    }

                    @Override
                    public void onError(ANError anError) {
                        ((MainActivity) context).progressLoader(false);
                        Toast.makeText(context, "Tải lên lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.contribute_fragment;
    }
}
