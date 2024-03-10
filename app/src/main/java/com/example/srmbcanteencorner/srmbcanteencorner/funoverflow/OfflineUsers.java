package com.example.srmbcanteencorner.srmbcanteencorner.funoverflow;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class OfflineUsers extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
