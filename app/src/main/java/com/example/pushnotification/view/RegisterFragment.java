package com.example.pushnotification.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.pushnotification.R;
import com.example.pushnotification.activity.MainActivity;
import com.example.pushnotification.base.BaseFragment;
import com.example.pushnotification.base.Utils;
import com.example.pushnotification.databinding.FragmentLoginBinding;
import com.example.pushnotification.manager.LoginAndRegisterViewModel;
import com.example.pushnotification.model.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterFragment extends BaseFragment<FragmentLoginBinding> {

    private LoginAndRegisterViewModel viewModel;
    private Context context;
    private MainActivity mainActivity;

    public static RegisterFragment newInstance(Context context) {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
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
        mainActivity.setPageTitle("Đăng Ký");
        mainActivity.showBack(true);
        binding.tvName.setVisibility(View.VISIBLE);
        binding.submit.setText("Đăng Ký");
        mainActivity.showBottomNavigation(false);
        viewModel = new ViewModelProvider(this).get(LoginAndRegisterViewModel.class);
        viewModel.setUpFirebase();
        binding.viewLogin.setVisibility(View.GONE);
        binding.submit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(Objects.requireNonNull(binding.tvName.getText()).toString()) || TextUtils.isEmpty(Objects.requireNonNull(binding.edtUsername.getText()).toString()) || TextUtils.isEmpty(Objects.requireNonNull(binding.edtPassword.getText()).toString())) {
                Toast.makeText(context, "Nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binding.edtPassword.getText().toString().length() < 7) {
                Toast.makeText(context, "mật khẩu phải trên 7 kí tự", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.registerEmail(getContext(), new User(binding.edtUsername.getText().toString(),
                    Objects.requireNonNull(binding.edtPassword.getText()).toString(),
                    Objects.requireNonNull(binding.tvName.getText()).toString()), firebaseUser -> {
                User user = new User();
                user.setName(Objects.requireNonNull(binding.tvName.getText()).toString());
                mainActivity.progressLoader(true);
                viewModel.updateProfile(context, user, () -> {
                    mainActivity.progressLoader(false);
                    Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    mainActivity.showBack(false);
                    Utils.replaceFragment(fragmentManager.beginTransaction(), HomeViewFragment.newInstance(context));
                });
            });
        });
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard((MainActivity) context);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_login;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.showBack(false);
        mainActivity.setPageTitle("Đăng Nhập");
    }
}
