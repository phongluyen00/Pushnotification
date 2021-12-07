package com.example.pushnotification.view.view_model;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.pushnotification.R;
import com.example.pushnotification.activity.DetailQRScan;
import com.example.pushnotification.activity.MainActivity;
import com.example.pushnotification.base.Utils;
import com.example.pushnotification.database.AppDatabase;
import com.example.pushnotification.model.CommentRequest;
import com.example.pushnotification.model.CommentResponse;
import com.example.pushnotification.model.ContributeRequest;
import com.example.pushnotification.model.MessageResponse;
import com.example.pushnotification.model.Product;
import com.example.pushnotification.view.ContributeFragment;
import com.example.pushnotification.view.LoginFragment;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class ScanQRViewModel extends ViewModel {

    private AppDatabase appDatabase;
    private MutableLiveData<List<Product>> listMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Uri> uriMutableLiveData = new MutableLiveData<>();
    private FirebaseUser firebaseUser;

    public void setupViewModel(Context context) {
        appDatabase = AppDatabase.getInMemoryDatabase(context);
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        AndroidNetworking.initialize(context, okHttpClient);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void scanProductID(Context context, FragmentManager fragmentManager, Product productS) {
        ((MainActivity) context).progressLoader(true);
        AndroidNetworking.post(Utils.SCAN_QR)
                .addBodyParameter(productS)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((MainActivity) context).progressLoader(false);
                        Product product = new Gson().fromJson(response.toString(), Product.class);
                        if (!product.isSuccess()) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            LayoutInflater inflater = ((MainActivity) context).getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.layout_donggop, null);
                            dialogBuilder.setView(dialogView);
                            AlertDialog alertDialog = dialogBuilder.create();
                            AppCompatTextView tvMessage = dialogView.findViewById(R.id.message);
                            tvMessage.setText(product.getMessage());
                            AppCompatTextView submit = dialogView.findViewById(R.id.submit);
                            AppCompatTextView cancel = dialogView.findViewById(R.id.cancel);
                            submit.setOnClickListener(v1 -> {
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (firebaseUser != null) {
                                    new Handler().postDelayed(() -> {
                                        Utils.addFragmentToBackStack(((MainActivity) context).getSupportFragmentManager().beginTransaction()
                                                , ContributeFragment.newInstance(context, productS), ContributeFragment.class.getSimpleName());
                                    }, 200);
                                }else {
                                    Utils.replaceFragment(fragmentManager.beginTransaction(), LoginFragment.newInstance(context));
                                }
                                alertDialog.dismiss();
                            });
                            cancel.setOnClickListener(v -> alertDialog.dismiss());
                            Window window = alertDialog.getWindow();
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setGravity(Gravity.CENTER);
                            alertDialog.show();
                        } else {
                            Intent intent = new Intent(new Intent(context, DetailQRScan.class));
                            intent.putExtra("product", product);
                            insertProduct(context, product);
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ((MainActivity) context).progressLoader(false);
                    }
                });
    }

    public void postComment(Context context, CommentRequest request, onPostCommentListener listener) {
        ((DetailQRScan) context).progressLoader(true);
        AndroidNetworking.post(Utils.POST_COMMENT)
                .addBodyParameter(request)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((DetailQRScan) context).progressLoader(false);
                        CommentResponse commentResponse = new Gson().fromJson(response.toString(), CommentResponse.class);
                        listener.onPostDone(commentResponse);
                    }

                    @Override
                    public void onError(ANError anError) {
                        ((DetailQRScan) context).progressLoader(false);
                        Toast.makeText(context, "Lỗi hệ thống !", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getComment(Context context, Product product, onDataListener listener) {
        ((DetailQRScan) context).progressLoader(true);
        AndroidNetworking.post(Utils.GET_ALL_COMMENT)
                .addBodyParameter(product)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((DetailQRScan) context).progressLoader(false);
                        MessageResponse messageResponse = new Gson().fromJson(response.toString(), MessageResponse.class);
                        listener.onLoadCommentDone(messageResponse);
                    }

                    @Override
                    public void onError(ANError anError) {
                        ((DetailQRScan) context).progressLoader(false);
                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void postContribute(Context context, FragmentManager fragmentManager, File file, Map<String, String> map) {
        ((MainActivity) context).progressLoader(true);
        AndroidNetworking.upload("https://qltsmds.com/truyxuatnguongoc/api/DongGopSanPham/Upload")
                .addMultipartFile("file", file)
                .addMultipartParameter(map)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                    // do anything with progress
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

    @SuppressLint("CheckResult")
    public void insertProduct(Context context, Product product) {
        Date currentTime = Calendar.getInstance().getTime();
        String pattern = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(currentTime);
        product.setTimeSave(date);
        if (appDatabase == null) {
            appDatabase = AppDatabase.getInMemoryDatabase(context);
        }
        appDatabase.productDAO().insert(product);
    }

    @SuppressLint("CheckResult")
    public void finAllHistory(Context context) {
        if (appDatabase == null) {
            appDatabase = AppDatabase.getInMemoryDatabase(context);
        }
        appDatabase.productDAO().findProduct().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    listMutableLiveData.postValue(response);
                });
    }

    public void updateImage(Context context, Uri uriImage) {
//        upload(context, uriImage);
        uriMutableLiveData.postValue(uriImage);
    }

//    public void upload(Context context, Uri uriImage){
//        String imagepath = Utils.getRealPathFromURI(uriImage, (MainActivity) context);
//        if (imagepath == null) {
//            Toast.makeText(context, "Lỗi file ảnh", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        File file = new File(imagepath);
//        AndroidNetworking.upload("https://qltsmds.com/truyxuatnguongoc/api/DongGopSanPham/Upload")
//                .addMultipartFile("file", file)
//                .setTag("test")
//                .setPriority(Priority.HIGH)
//                .build()
//                .setUploadProgressListener((bytesUploaded, totalBytes) -> {
//                    // do anything with progress
//                })
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        ((MainActivity) context).progressLoader(false);
//                        Log.d("AAAAAAAAAAA", response);
//                        Toast.makeText(context, "Tải lên thành công", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        ((MainActivity) context).progressLoader(false);
//                        Toast.makeText(context, "Tải lên lỗi", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    public MutableLiveData<Uri> getBitmapMutableLiveData() {
        return uriMutableLiveData;
    }

    public void deleteAll() {
        appDatabase.productDAO().deleteAll();
    }

    public MutableLiveData<List<Product>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public interface onDataListener {
        void onLoadCommentDone(MessageResponse messageResponse);
    }

    public interface onPostCommentListener {
        void onPostDone(CommentResponse comment);
    }
}
