package com.example.pushnotification.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.pushnotification.R;
import com.example.pushnotification.activity.DetailQRScan;
import com.example.pushnotification.activity.MainActivity;
import com.example.pushnotification.base.BaseFragment;
import com.example.pushnotification.base.Utils;
import com.example.pushnotification.databinding.FragmentHistoryBinding;
import com.example.pushnotification.model.Product;
import com.example.pushnotification.view.adapter.HistoryAdapter;
import com.example.pushnotification.view.view_model.ScanQRViewModel;

import java.util.ArrayList;

public class HistoryScanQRFragment extends BaseFragment<FragmentHistoryBinding> {

    private Context context;
    private HistoryAdapter adapter;
    private ScanQRViewModel viewModel;

    public static HistoryScanQRFragment newInstance(Context context) {
        Bundle args = new Bundle();
        HistoryScanQRFragment fragment = new HistoryScanQRFragment();
        fragment.setArguments(args);
        fragment.context = context;
        return fragment;
    }

    @Override
    protected void initObserver() {
        Utils.hideKeyboard((MainActivity) context);
        viewModel.getListMutableLiveData().observe(this, products -> {
            if (products.size() == 0){
                binding.nodata.setVisibility(View.VISIBLE);
                binding.rclComment.setVisibility(View.GONE);
                binding.delete.setVisibility(View.GONE);
                return;
            }
            binding.nodata.setVisibility(View.GONE);
            binding.delete.setVisibility(View.VISIBLE);
            adapter = new HistoryAdapter(context, (ArrayList<Product>) products, product -> {
                Intent intent = new Intent(new Intent(context, DetailQRScan.class));
                intent.putExtra("product", product);
                context.startActivity(intent);
            });
            binding.rclComment.setAdapter(adapter);
        });
    }

    @Override
    protected void initFragment(View view) {
        ((MainActivity) context).setPageTitle("Lịch sử quyét");
        ((MainActivity) context).showBottomNavigation(false);
        viewModel = new ViewModelProvider(this).get(ScanQRViewModel.class);
        viewModel.setupViewModel(context);
        viewModel.finAllHistory(context);

        binding.imgClose.setOnClickListener(v -> {
            ((MainActivity) context).setPageTitle("Cá Nhân");
            ((MainActivity) context).showBottomNavigation(firebaseUser != null);
            fragmentManager.popBackStackImmediate();
        });

        binding.delete.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setMessage("Xóa tất cả?")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    viewModel.deleteAll();
                    viewModel.finAllHistory(context);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_delete)
                .show());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_history;
    }
}
