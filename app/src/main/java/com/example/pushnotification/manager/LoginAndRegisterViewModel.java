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
//                            .setPhotoUri(Uri.parse("https://scontent.fhan5-6.fna.fbcdn.net/v/t1.6435-9/125094510_3230715880488953_884088225314861862_n.jpg?_nc_cat=107&ccb=1-5&_nc_sid=e3f864&_nc_ohc=xLtGze6LdGoAX9fBS_m&_nc_ht=scontent.fhan5-6.fna&oh=1f32df66a587ef7f54000d77ba5f3c56&oe=61B5917C"))
                            .build();
            user.updateProfile(profileChangeRequest).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    listener.onUpdateSuccess();
                }
            });
        }

    }

    public void signIn(Context context, User user, signInListener listener) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(user.getUsername(), user.getPassword())
                .addOnCompleteListener((MainActivity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ((MainActivity)context).progressLoader(false);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            listener.onSignInSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
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
