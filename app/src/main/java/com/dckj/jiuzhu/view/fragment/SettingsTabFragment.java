package com.dckj.jiuzhu.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dckj.jiuzhu.MainActivity;
import com.dckj.jiuzhu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 首页
 */

public class SettingsTabFragment extends PreferenceFragmentCompat {

    @BindView(R.id.tv_titlebar_center)
    TextView tvTitlebarCenter;
    @BindView(R.id.iv_titlebar_left)
    ImageView ivTitlebarLeft;
    /*@BindView(R.id.iv_titlebar_right)
    ImageView ivTitlebarRight;*/

    protected Unbinder mUnbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_setting);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
        //return inflater.inflate(R.layout.fragment_tab_settings, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        ((TextView) view.findViewById(R.id.tv_titlebar_center)).setText(R.string.settings);
        Toolbar toolbar = view.findViewById(R.id.title_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
    }

    @OnClick({R.id.iv_titlebar_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_titlebar_left://左侧滑菜单栏
                if (MainActivity.slidingMenu != null) {
                    MainActivity.slidingMenu.showMenu();
                }
                break;
            /*case R.id.iv_titlebar_right://右侧滑菜单栏
                if (MainActivity.slidingMenu != null) {
                    MainActivity.slidingMenu.showSecondaryMenu();
                }
                break;*/
        }
    }
}
