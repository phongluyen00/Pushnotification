package com.example.pushnotification.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.pushnotification.R;
import com.example.pushnotification.base.BaseActivity;
import com.example.pushnotification.base.Utils;
import com.example.pushnotification.databinding.ActivityDetailBinding;
import com.example.pushnotification.model.CommentRequest;
import com.example.pushnotification.model.MessageResponse;
import com.example.pushnotification.model.Product;
import com.example.pushnotification.view.adapter.CommentAdapter;
import com.example.pushnotification.view.view_model.ScanQRViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DetailQRScan extends BaseActivity<ActivityDetailBinding> {

    private Product product;
    private CommentAdapter adapter;
    private ScanQRViewModel viewModel;
    private List<MessageResponse.Value> commentList = new ArrayList<>();
    Date currentTime = Calendar.getInstance().getTime();

    @Override
    protected void initView() {
        String pattern = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(currentTime);
        setupUI(binding.rootView);
        viewModel = new ViewModelProvider(this).get(ScanQRViewModel.class);
        product = getIntent().getParcelableExtra("product");
        if (product != null) binding.setItem(product);
        Glide.with(this).load(product.getImage()).error(R.drawable.bg).into(binding.img);

        setupAdapter();
        binding.imgBack.setOnClickListener(v -> finish());

        binding.info.setOnClickListener(v -> binding.llDetail.setVisibility(View.VISIBLE));

        if (!MainActivity.comment.equals("")) {
            binding.postComment.setText(MainActivity.comment);
            MainActivity.comment = "";
            MainActivity.productId = "";
        }

        binding.closeview.setOnClickListener(v -> binding.llDetail.setVisibility(View.GONE));
        binding.sendComment.setOnClickListener(v -> {
            String comment = Objects.requireNonNull(binding.postComment.getText()).toString();
            Utils.hideKeyboard(DetailQRScan.this);
            if (TextUtils.isEmpty(comment)) return;
            if (firebaseUser == null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("comment", comment);
                intent.putExtra("productId", product.getProductId());
                startActivity(intent);
                return;
            }

            viewModel.postComment(DetailQRScan.this,
                    new CommentRequest(firebaseUser.getEmail(),
                            String.valueOf(binding.ratingBar21.getRating()),
                            comment, product.getProductId(), "1",
                            firebaseUser.getDisplayName(), date), comment1 -> {
                        commentList.add(new MessageResponse.Value(firebaseUser.getDisplayName(),
                                comment,
                                String.valueOf(binding.ratingBar21.getRating()),date));
                        binding.postComment.setText("");
                        Toast.makeText(DetailQRScan.this, comment1.getMessage(), Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    });
        });
    }

    private void setupAdapter() {
        viewModel.getComment(DetailQRScan.this, new Product(product.getProductId()), messageResponse -> {
            commentList = messageResponse.getCommentList().getValues();
            adapter = new CommentAdapter(this, commentList);
            binding.rclComment.setAdapter(adapter);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                Utils.hideKeyboard(DetailQRScan.this);
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
}
