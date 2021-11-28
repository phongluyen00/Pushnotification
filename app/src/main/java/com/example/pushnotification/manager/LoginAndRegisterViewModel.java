package com.example.pushnotification.manager;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pushnotification.activity.MainActivity;
import com.example.pushnotification.base.Utils;
import com.example.pushnotification.model.User;
import com.example.pushnotification.view.HomeViewFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginAndRegisterViewModel extends ViewModel {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private MutableLiveData<Boolean> updateProfile = new MutableLiveData<>();

    public void setUpFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void registerEmail(Context context, User user, registerListener listener) {
        mAuth.createUserWithEmailAndPassword(user.getUsername(), user.getPassword())
                .addOnCompleteListener((MainActivity) context, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user1 = mAuth.getCurrentUser();
                        listener.registerSuccess(user1);
                    } else {
                        Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateProfile(Context context, User account, updateDataListener listener) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileChangeRequest =
                    new UserProfileChangeRequest.Builder()
                            .setDisplayName(account.getName())
                            .build();
            user.updateProfile(profileChangeRequest).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    listener.onUpdateSuccess();
                }
            });
        }

    }

    public void signIn(Context context, User user, signInListener listener) {
        mAuth.signInWithEmailAndPassword(user.getUsername(), user.getPassword())
                .addOnCompleteListener((MainActivity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ((MainActivity)context).progressLoader(false);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            listener.onSignInSuccess(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signOut(){
        mAuth.signOut();
    }

    public interface updateDataListener{
        void onUpdateSuccess();
    }

    public interface signInListener{
        void onSignInSuccess(FirebaseUser firebaseUser);
    }

    public interface registerListener{
        void registerSuccess(FirebaseUser firebaseUser);
    }
}
