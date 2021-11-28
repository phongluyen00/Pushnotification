package com.example.pushnotification.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class BaseActivity<BD extends ViewDataBinding> extends AppCompatActivity {

    protected BD binding;
    protected FirebaseUser firebaseUser;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        progressDialog = new ProgressDialog(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        initView();
    }

    protected abstract void initView();

    protected abstract int getLayoutId();

    public void progressLoader(boolean isShow) {
        progressDialog.setMessage("Vui lòng chờ trong giây lát ...");
        if (isShow) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
            progressDialog.cancel();
        }
    }
}
