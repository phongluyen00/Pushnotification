package com.example.pushnotification.manager;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.pushnotification.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ManagerModel extends ViewModel {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private void setupFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void updatePassword(Context context, String newPassword) {
        firebaseUser.updatePassword(newPassword).addOnCompleteListener(task -> {

        });
    }

    public void updateEmail(Context context, String newEmail){
        firebaseUser.updateEmail(newEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

            }
        });
    }

    public void sendPasswordResetEmail(Context context, String emailAddress){
        firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void updateProfile(Context context, User account) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileChangeRequest =
                    new UserProfileChangeRequest.Builder()
                            .setDisplayName(account.getUsername())
                            .setPhotoUri(Uri.parse("https://scontent.fhan5-6.fna.fbcdn.net/v/t1.6435-9/125094510_3230715880488953_884088225314861862_n.jpg?_nc_cat=107&ccb=1-5&_nc_sid=e3f864&_nc_ohc=xLtGze6LdGoAX9fBS_m&_nc_ht=scontent.fhan5-6.fna&oh=1f32df66a587ef7f54000d77ba5f3c56&oe=61B5917C"))
                            .build();
            user.updateProfile(profileChangeRequest).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
