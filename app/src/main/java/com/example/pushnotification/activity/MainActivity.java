package com.example.pushnotification.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.pushnotification.R;
import com.example.pushnotification.base.BaseActivity;
import com.example.pushnotification.base.Utils;
import com.example.pushnotification.databinding.ActivityMainBinding;
import com.example.pushnotification.model.Product;
import com.example.pushnotification.view.HistoryScanQRFragment;
import com.example.pushnotification.view.HomeViewFragment;
import com.example.pushnotification.view.LoginFragment;
import com.example.pushnotification.view.MyProFileFragment;
import com.example.pushnotification.view.RegisterFragment;
import com.example.pushnotification.view.view_model.ScanQRViewModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public LoginFragment loginFragment = LoginFragment.newInstance(this);
    public RegisterFragment registerFragment = RegisterFragment.newInstance(this);
    public HomeViewFragment homeViewFragment = HomeViewFragment.newInstance(this);
    public HistoryScanQRFragment historyScanQRFragment = HistoryScanQRFragment.newInstance(this);
    private ScanQRViewModel viewModel;
    public static String comment = "";
    public static String productId = "";

    @Override
    protected void initView() {
        viewModel = new ViewModelProvider(this).get(ScanQRViewModel.class);
        FragmentManager fragmentManager = getSupportFragmentManager();

        binding.home.setOnClickListener(v -> {
            Utils.replaceFragment(fragmentManager.beginTransaction(),
                    homeViewFragment);
            binding.title.setText("Quét QR");
        });

        binding.login.setVisibility(firebaseUser == null ? View.VISIBLE : View.GONE);
        binding.login.setOnClickListener(v -> Utils.replaceFragment(fragmentManager.beginTransaction(),
                loginFragment));

        binding.qrCode.setOnClickListener(v -> Utils.replaceFragment(fragmentManager.beginTransaction(),
                homeViewFragment));

        binding.profile.setOnClickListener(v -> {
            Utils.replaceFragment(fragmentManager.beginTransaction(),
                    MyProFileFragment.newInstance(MainActivity.this));
            binding.title.setText("Cá Nhân");
        });

        binding.imgBack.setOnClickListener(v -> fragmentManager.popBackStackImmediate());
        binding.home.setVisibility(firebaseUser != null ? View.VISIBLE : View.GONE);
        binding.profile.setVisibility(firebaseUser != null ? View.VISIBLE : View.GONE);
        if (getIntent() != null && getIntent().getStringExtra("comment") != null) {
            comment = getIntent().getStringExtra("comment");
            productId = getIntent().getStringExtra("productId");
            Utils.addFragmentToBackStack(fragmentManager.beginTransaction(),
                    loginFragment, LoginFragment.class.getSimpleName());
        } else {
            Utils.addFragmentToBackStack(fragmentManager.beginTransaction(),
                    homeViewFragment, HomeViewFragment.class.getSimpleName());
        }

        binding.history.setOnClickListener(v -> Utils.replaceFragment(fragmentManager.beginTransaction(),
                historyScanQRFragment));

    }

    public void showBottomNavigation(boolean isShow) {
        binding.group.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setPageTitle(String title) {
        binding.title.setText(title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                homeViewFragment.setDataScanQR(result.getContents());
                if (result.getContents().contains("http")) {
                    Intent myActivity = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                    startActivity(myActivity);
                } else {
                    viewModel.scanProductID(this,getSupportFragmentManager(), new Product(result.getContents()));
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 200) {
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    Uri uri = data.getData();
                    if (uri != null) {
                        viewModel.updateImage(this,uri);
                    }
                }
            }
        }
    }

    public void showBack(boolean isShow) {
        binding.imgBack.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {

//        super.onBackPressed();
    }

    public void showViewLoginAndScanQR(int visibility) {
        binding.qrCode.setVisibility(visibility);
        binding.history.setVisibility(visibility);
        binding.login.setVisibility(visibility);
    }
}