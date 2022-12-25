package com.nicoovillarr.promise;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseBindingActivity <T extends ViewDataBinding> extends BaseActivity {

    private T dataBinding;

    @Override
    protected void setView() {
        this.dataBinding = DataBindingUtil.setContentView(this, this.layoutId());
    }

    protected T getBinding() {
        return this.dataBinding;
    }

}
