package com.example.pushnotification.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.pushnotification.R;
import com.example.pushnotification.activity.MainActivity;
import com.example.pushnotification.base.BaseFragment;
import com.example.pushnotification.base.Utils;
import com.example.pushnotification.databinding.MyProfileFragmentBinding;
import com.example.pushnotification.manager.LoginAndRegisterViewModel;

import java.util.Objects;

public class MyProFileFragment extends BaseFragment<MyProfileFragmentBinding> {

    private Context context;
    private LoginAndRegisterViewModel viewModel;

    public static MyProFileFragment newInstance(Context context) {
        Bundle args = new Bundle();
        MyProFileFragment fragment = new MyProFileFragment();
        fragment.context = context;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initObserver() {

    }

    @Override
    protected void initFragment(View view) {
        viewModel = new ViewModelProvider(this).get(LoginAndRegisterViewModel.class);
        if (firebaseUser.getPhotoUrl() != null){
            Glide.with(this).load(firebaseUser.getPhotoUrl()).into(binding.imgInfo);
        }
        viewModel.setUpFirebase();

        binding.tVUpdateEmail.setOnClickListener(v -> Utils.addFragmentToBackStack(fragmentManager.beginTransaction(),
                HistoryScanQRFragment.newInstance(context), HistoryScanQRFragment.class.getSimpleName()));

        binding.logOut.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Bạn có muốn đăng xuất không")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        Utils.replaceFragment(fragmentManager.beginTransaction(),
                                LoginFragment.newInstance(context));
                        viewModel.signOut();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        binding.tVUpdatePassword.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_label_editor, null);
            dialogBuilder.setView(dialogView);
            AlertDialog alertDialog = dialogBuilder.create();

            AppCompatTextView appCompatTextView = dialogView.findViewById(R.id.submit);
            AppCompatEditText edtPasswordNew = dialogView.findViewById(R.id.password_new);
            appCompatTextView.setOnClickListener(v1 -> firebaseUser.updatePassword(Objects.requireNonNull(edtPasswordNew.getText()).toString().trim()).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }));
            Window window = alertDialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            alertDialog.show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.setAccount(firebaseUser);
        ((MainActivity) context).showBottomNavigation(true);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.my_profile_fragment;
    }
}
