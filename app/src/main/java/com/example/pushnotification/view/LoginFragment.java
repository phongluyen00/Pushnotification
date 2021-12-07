package com.example.pushnotification.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.pushnotification.R;
import com.example.pushnotification.activity.DetailQRScan;
import com.example.pushnotification.activity.MainActivity;
import com.example.pushnotification.base.BaseFragment;
import com.example.pushnotification.base.Utils;
import com.example.pushnotification.databinding.FragmentLoginBinding;
import com.example.pushnotification.manager.LoginAndRegisterViewModel;
import com.example.pushnotification.model.User;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends BaseFragment<FragmentLoginBinding> {

    private LoginAndRegisterViewModel viewModel;
    private Context context;
    private MainActivity mainActivity;

    public static LoginFragment newInstance(Context context) {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.context = context;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initObserver() {

    }

    @Override
    protected void initFragment(View view) {
        setupUI(binding.rootView);
        mainActivity = ((MainActivity) context);
        mainActivity.showBottomNavigation(false);
        viewModel = new ViewModelProvider(this).get(LoginAndRegisterViewModel.class);
        viewModel.setUpFirebase();

        binding.submit.setOnClickListener(v -> {
            ((MainActivity)context).progressLoader(true);
            if (binding.edtPassword.getText().toString().length() < 7){
                Toast.makeText(context, "mật khẩu phải trên 7 kí tự", Toast.LENGTH_SHORT).show();
            }
            mainActivity.progressLoader(true);
            viewModel.signIn(getContext(), new User(binding.edtUsername.getText().toString(), binding.edtPassword.getText().toString()), new LoginAndRegisterViewModel.signInListener() {
                @Override
                public void onSignInSuccess(FirebaseUser firebaseUser) {
                    mainActivity.progressLoader(false);
                    Utils.replaceFragment(fragmentManager.beginTransaction(),
                            HomeViewFragment.newInstance(context));
                }
            });
        });

        binding.viewLogin.setOnClickListener(v -> {
            Utils.addFragmentToBackStack(fragmentManager.beginTransaction(),
                    RegisterFragment.newInstance(context), RegisterFragment.class.getSimpleName());
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
    protected int getLayoutID() {
        return R.layout.fragment_login;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setPageTitle("Đăng Nhập");
        mainActivity.showBottomNavigation(false);
        mainActivity.showViewLoginAndScanQR(View.VISIBLE);
    }
}
