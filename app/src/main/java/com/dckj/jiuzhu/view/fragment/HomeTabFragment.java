package com.dckj.jiuzhu.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dckj.jiuzhu.MainActivity;
import com.dckj.jiuzhu.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 */

public class HomeTabFragment extends BaseFragment {

    @BindView(R.id.tv_titlebar_center)
    TextView tvTitlebarCenter;
    @BindView(R.id.iv_titlebar_left)
    ImageView ivTitlebarLeft;
    @BindView(R.id.iv_titlebar_right)
    ImageView ivTitlebarRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        ((TextView) mView.findViewById(R.id.tv_titlebar_center)).setText(R.string.title_home_page);
    }

    @Override
    protected void initLayoutRes() {
        mLayoutRes = R.layout.fragment_tab_home;
        mLayoutResV19 = R.layout.fragment_tab_home_v19;
    }

    @OnClick({R.id.iv_titlebar_left, R.id.iv_titlebar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_titlebar_left://左侧滑菜单栏
                if (MainActivity.slidingMenu != null) {
                    MainActivity.slidingMenu.showMenu();
                }
                break;
            case R.id.iv_titlebar_right://右侧滑菜单栏
                if (MainActivity.slidingMenu != null) {
                    MainActivity.slidingMenu.showSecondaryMenu();
                }
                break;
        }
    }
}
