package com.dckj.jiuzhu.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    /*@BindView(R.id.iv_titlebar_right)
    ImageView ivTitlebarRight;*/

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
        ((TextView) mView.findViewById(R.id.home_unprocessedordercount)).setText("未处理调查  ：12笔");
        ((TextView) mView.findViewById(R.id.home_processedordercount_week)).setText("本周处理调查：6笔");
        ((TextView) mView.findViewById(R.id.home_backlogordercount)).setText("今日待处理  : 6笔");

    }

    @Override
    protected void initLayoutRes() {
        mLayoutRes = R.layout.fragment_tab_home;
        mLayoutResV19 = R.layout.fragment_tab_home_v19;
    }

    @Override
    protected void initMenuRes() {
        mMenuRes = R.menu.menu_home_page;
    }

    @Override
    protected void clickMenuItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tv_test_menu:
                Toast.makeText(mContext, "click menu item", Toast.LENGTH_SHORT).show();
                break;
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
