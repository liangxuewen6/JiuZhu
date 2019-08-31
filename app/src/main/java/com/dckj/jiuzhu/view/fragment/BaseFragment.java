package com.dckj.jiuzhu.view.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AndroidException;
import android.util.AndroidRuntimeException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dckj.jiuzhu.R;
import com.dckj.jiuzhu.module.config.AppConfig;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    protected Context mContext;

    protected View mView;
    protected Unbinder mUnbinder;
    protected int mLayoutResV19;
    protected int mLayoutRes;

    protected abstract void initData();
    protected abstract void initView();
    protected abstract void initLayoutRes();

    private int getViewByLayoutRes(){
        if (AppConfig.IS_LARGER_THAN_44) {
            return mLayoutResV19;
        }else{
            return mLayoutRes;
        }
    }

    private void checkLayoutRes(){
        initLayoutRes();
        if(mLayoutResV19 == 0){
            throw new AndroidRuntimeException("mLayoutResV19 can't be zero");
        }
        if(mLayoutRes == 0){
            throw new AndroidRuntimeException("mLayoutRes can't be zero");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();

        checkLayoutRes();
        mView = View.inflate(mContext, getViewByLayoutRes(), null);
        if(mView == null){
            throw new AndroidRuntimeException("View can't be null");
        }

        mUnbinder = ButterKnife.bind(this, mView);
        initView();
        initData();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mUnbinder != null){
            mUnbinder.unbind();
        }

    }
}
