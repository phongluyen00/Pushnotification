package com.example.pushnotification.base;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pushnotification.R;

public class Utils {

    public static String GET_ALL_COMMENT = "https://qltsmds.com/truyxuatnguongoc/api/DanhGiaSanPham/getListData";
    public static String POST_COMMENT = "https://qltsmds.com/truyxuatnguongoc/api/DanhGiaSanPham/Save";
    public static String SCAN_QR = "https://qltsmds.com/truyxuatnguongoc/api/DanhMucSanPham/getData";

    public static void addFragmentToBackStack(FragmentTransaction fragmentTransaction,
                                       Fragment fragment, String tag) {
        fragmentTransaction.add(R.id.container, fragment,tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public static void replaceFragment(FragmentTransaction fragmentTransaction, Fragment fragment){
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }

    public static String formatString(EditText editText){
        return editText.getText().toString();
    }
}
