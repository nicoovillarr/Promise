package com.nicoovillarr.promise;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setView();
        this.loadToolbar();
        this.setUpFab();
        this.customActivityInit();
    }

    protected void setView() {
        setContentView(this.layoutId());
    }

    protected void customActivityInit() {}

    protected void onFabClick() {}

    protected void setToolbarTitle(String title) {
        this.setTitle(title);
    }

    protected abstract int layoutId();

    private void loadToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void setUpFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> this.onFabClick());
    }

}
