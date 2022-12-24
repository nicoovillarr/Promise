package com.nicoovillarr.promise;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.layoutId());
        this.loadToolbar();
        this.setUpFab();
        this.customActivityInit();
    }

    protected void customActivityInit() {}

    protected void onFabClick() {}

    protected void setToolbarTitle(String title) {
        this.setTitle(title);
    }

    protected abstract int layoutId();

    private void loadToolbar() {
        this.toolbar = findViewById(R.id.toolbar);
        if (this.toolbar != null) {
            setSupportActionBar(this.toolbar);
        }
    }

    private void setUpFab() {
        this.fab = findViewById(R.id.fab);
        this.fab.setOnClickListener(v -> this.onFabClick());
    }

}
