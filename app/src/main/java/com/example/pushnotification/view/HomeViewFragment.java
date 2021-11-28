package com.example.pushnotification.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.pushnotification.R;
import com.example.pushnotification.activity.CaptureAct;
import com.example.pushnotification.activity.MainActivity;
import com.example.pushnotification.base.BaseFragment;
import com.example.pushnotification.base.Utils;
import com.example.pushnotification.databinding.FragmentHomeBinding;
import com.example.pushnotification.model.Product;
import com.example.pushnotification.view.view_model.ScanQRViewModel;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.Objects;

public class HomeViewFragment extends BaseFragment<FragmentHomeBinding> {

    private Context context;
    private String dataScanQR = "";
    private ScanQRViewModel viewModel;

    public void setDataScanQR(String dataScanQR) {
        this.dataScanQR = dataScanQR;
    }

    public static HomeViewFragment newInstance(Context context) {
        Bundle args = new Bundle();
        HomeViewFragment fragment = new HomeViewFragment();
        fragment.context = context;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initObserver() {
        ((MainActivity) context).showBottomNavigation(true);
    }

    @Override
    protected void initFragment(View view) {
        ((MainActivity) context).setPageTitle("QR Code");
        setupUI(binding.rootView);
        viewModel = new ViewModelProvider(this).get(ScanQRViewModel.class);
        viewModel.setupViewModel(context);

        if (!MainActivity.productId.equals("")  && !MainActivity.comment.equals("")){
            viewModel.scanProductID(context, new Product(MainActivity.productId));
        }

        binding.scanQr.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator((MainActivity) context);
            integrator.setCaptureActivity(CaptureAct.class);
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Loading.....");
            integrator.initiateScan();
        });

        binding.flash.setOnClickListener(v -> {
            Camera mycam = Camera.open();
            Camera.Parameters p = mycam.getParameters();// = mycam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mycam.setParameters(p); //time passes
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mycam.release();
        });

        binding.search.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(Objects.requireNonNull(binding.qrId.getText()).toString())) {
                viewModel.scanProductID(context, new Product(binding.qrId.getText().toString().trim()));
            } else {
                Toast.makeText(context, "Nhập mã cần tìm", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).showBottomNavigation(firebaseUser != null);
        ((MainActivity) context).showViewLoginAndScanQR(firebaseUser != null ? View.GONE : View.VISIBLE);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }
}
