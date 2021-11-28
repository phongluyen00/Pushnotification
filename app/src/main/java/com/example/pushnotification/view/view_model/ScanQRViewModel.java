package com.example.pushnotification.view.view_model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class ScanQRViewModel extends ViewModel {

    private AppDatabase appDatabase;
    private MutableLiveData<List<Product>> listMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Uri> uriMutableLiveData = new MutableLiveData<>();

    public void setupViewModel(Context context) {
        appDatabase = AppDatabase.getInMemoryDatabase(context);
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        AndroidNetworking.initialize(context, okHttpClient);
    }

    public void scanProductID(Context context, Product product) {
        ((MainActivity) context).progressLoader(true);
        AndroidNetworking.post(Utils.SCAN_QR)
                .addBodyParameter(product)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((MainActivity) context).progressLoader(false);
                        Product product = new Gson().fromJson(response.toString(), Product.class);
                        if (!product.isSuccess()) {
                            Toast.makeText(context, product.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(() -> {
                                Utils.addFragmentToBackStack(((MainActivity) context).getSupportFragmentManager().beginTransaction()
                                        , ContributeFragment.newInstance(context,product), ContributeFragment.class.getSimpleName());
                            }, 1000);
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

    public void postContribute(Context context, ContributeRequest contributeRequest) {
        ((DetailQRScan) context).progressLoader(true);
        AndroidNetworking.post("https://qltsmds.com/truyxuatnguongoc/api/DongGopSanPham/Save")
                .addBodyParameter(contributeRequest)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((DetailQRScan) context).progressLoader(false);
                        MessageResponse messageResponse = new Gson().fromJson(response.toString(), MessageResponse.class);
//                        listener.onLoadCommentDone(messageResponse);
                    }

                    @Override
                    public void onError(ANError anError) {
                        ((DetailQRScan) context).progressLoader(false);
                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
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

    public void updateImage(Uri uriImage){
        uriMutableLiveData.postValue(uriImage);
    }

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
