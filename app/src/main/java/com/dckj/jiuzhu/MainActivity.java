package com.dckj.jiuzhu;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.dckj.jiuzhu.controller.MainController;
import com.dckj.jiuzhu.module.Utils;
import com.dckj.jiuzhu.module.util.DensityUtil;
import com.dckj.jiuzhu.module.util.ToastUtil;
import com.dckj.jiuzhu.view.activity.BaseActivity;
import com.dckj.jiuzhu.view.customview.RoundImageView;
import com.dckj.jiuzhu.view.menu.MainTabs;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.tabHost)
    FragmentTabHost tabHost;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    private final String TAG = "MainActivity";
    public static SlidingMenu slidingMenu;
    public static boolean slidingMenuToggleStatus = false;
    private static View mNavigationBarView;
    private static View mMenuNavigationBarView;

    private long mExitTime;
    private long mSlidingMenuTime;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    private MainController mMainController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainController = new MainController(this);
        mMainController.onCreate();
        mNavigationBarView = findViewById(R.id.navigationbarview);
        initFragmentTabHost();
        initSlidingMenu();
        updateMainLayout();
    }

    /**
     * 初始化FragmentTabHost
     */
    private void initFragmentTabHost() {
        //初始化tabHost
        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        //将tabHost和FragmentLayout关联
        tabHost.setup(mContext, getSupportFragmentManager(), R.id.fl_content);

        //去掉分割线
        if (Build.VERSION.SDK_INT > 10) {
            tabHost.getTabWidget().setShowDividers(0);
        }
        //添加tab和其对应的fragment
        MainTabs[] tabs = MainTabs.values();
        for (int i = 0; i < tabs.length; i++) {
            MainTabs mainTabs = tabs[i];
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(this.getResources().getString(mainTabs.getName()));

            View indicator = View.inflate(mContext, R.layout.tab_indicator, null);
            TextView tv_indicator = (TextView) indicator.findViewById(R.id.tv_indicator);
            Drawable drawable = mContext.getResources().getDrawable(mainTabs.getIcon());

            tv_indicator.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            tv_indicator.setText(mainTabs.getName());
            tabSpec.setIndicator(indicator);
            tabHost.addTab(tabSpec, mainTabs.getCla(), null);
        }
    }

    /**
     * 初始化SlidingMenu实现侧滑菜单栏
     */
    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        //设置呈现模式（分为左、右、左右三种）
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置侧滑界面偏移出的尺寸(剩余部分)
        slidingMenu.setBehindOffset(DensityUtil.dip2px(100, mContext));
        //设置剩余部分的阴影
        slidingMenu.setOffsetFadeDegree(0.4f);
        //设置全屏都可以触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置菜单内可以滑动（会导致菜单内的点击事件无效）
//        slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //添加到当前Activity当中
        //（SLIDING_WINDOW配合沉浸式状态栏，SLIDING_CONTENT配合非沉浸式状态栏）
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        //给侧滑界面传入View，与点击事件使用同一个View
        //如果给侧滑界面引用一个布局资源作为所展示的界面，点击事件无效，
        //因为侧滑界面和点击事件是不同的View
        View view = View.inflate(mContext, R.layout.my_sliding_menu, null);
        slidingMenu.setMenu(view);
        //slidingMenu.setSecondaryMenu(R.layout.my_sliding_menu_right);

        initSlidingMenuButton(view);

        //openListener仅在侧滑菜单栏开启的时候调用；
        //openedListener在侧滑菜单栏开启后调用（open之后）；
        //      并且在滑动且未关闭的时候再次调用。
        //      （也就是滑动未关闭然后又回到开启的状态时再次调用）
        slidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                slidingMenuToggleStatus = true;
            }
        });

        //closeListener仅在侧滑菜单栏开启的时候调用；
        //closedListener在侧滑菜单栏开启后调用（close之后）；
        //      并且在滑动且未关闭的时候再次调用。
        //      （也就是滑动未关闭然后又回到开启的状态时再次调用）
        slidingMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                slidingMenuToggleStatus = false;
            }
        });
    }

    /**
     * 初始化侧滑菜单栏中的按钮
     *
     * @param view
     */
    private void initSlidingMenuButton(View view) {
        RoundImageView userHead = (RoundImageView) view.findViewById(R.id.tv_userhead);
        TextView tvUsername = (TextView) view.findViewById(R.id.tv_username);
        TextView teUserSummary = (TextView) view.findViewById(R.id.tv_usersummary);
        LinearLayout menu_home = (LinearLayout) view.findViewById(R.id.slide_menu_home);
        LinearLayout menu_unprocessed_order = (LinearLayout) view.findViewById(R.id.slide_menu_unprocessed_order);
        LinearLayout menu_processed_order = (LinearLayout) view.findViewById(R.id.slide_menu_processed_order);
        LinearLayout menu_backlog_order = (LinearLayout)  view.findViewById(R.id.slide_menu_backlog_order);
        LinearLayout menu_statistics = (LinearLayout)  view.findViewById(R.id.slide_menu_statistics);
        LinearLayout menu_settings = (LinearLayout)  view.findViewById(R.id.slide_menu_settings);
        LinearLayout menu_refresh = (LinearLayout) view.findViewById(R.id.slide_menu_refresh);
        LinearLayout menu_logout = (LinearLayout) view.findViewById(R.id.slide_menu_logout);
        mMenuNavigationBarView = view.findViewById(R.id.menunavigationbarview);

        menu_home.setOnClickListener(this);
        menu_unprocessed_order.setOnClickListener(this);
        menu_processed_order.setOnClickListener(this);
        menu_backlog_order.setOnClickListener(this);
        menu_statistics.setOnClickListener(this);
        menu_settings.setOnClickListener(this);
        menu_refresh.setOnClickListener(this);
        menu_logout.setOnClickListener(this);
    }

    private void updateMainLayout() {
        if (tabHost != null) {
            tabHost.setVisibility(View.GONE);
        }
        Utils.getNavigationBarHeight(this);
        if (!Utils.checkDeviceHasNavigationBar(this)) {
            mNavigationBarView.setVisibility(View.GONE);
            mMenuNavigationBarView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.slide_menu_home://首页
                tabHost.setCurrentTab(0);
                slidingMenu.toggle();
                ToastUtil.makeText(mContext,"首页");
                break;
            case R.id.slide_menu_unprocessed_order://全部未处理订单
                tabHost.setCurrentTab(1);
                slidingMenu.toggle();
                ToastUtil.makeText(mContext,"全部未处理订单");
                break;
            case R.id.slide_menu_processed_order://已处理订单
                tabHost.setCurrentTab(2);
                slidingMenu.toggle();
                ToastUtil.makeText(mContext,"已处理订单");
                break;
            case R.id.slide_menu_backlog_order://今日待处理订单
                ToastUtil.makeText(mContext,"今日待处理订单");
                tabHost.setCurrentTab(3);
                slidingMenu.toggle();
                break;
            case R.id.slide_menu_statistics://统计
                ToastUtil.makeText(mContext,"统计");
                slidingMenu.toggle();
                tabHost.setCurrentTab(4);
                break;
            case R.id.slide_menu_settings://设置
                ToastUtil.makeText(mContext,"设置");
                slidingMenu.toggle();
                tabHost.setCurrentTab(5);
                break;
            case R.id.slide_menu_refresh://检查更新
                ToastUtil.makeText(mContext,"点击了检查更新");
                slidingMenu.toggle();
                break;
            case R.id.slide_menu_logout://切换用户/注销
                ToastUtil.makeText(mContext,"点击了切换用户/注销");
                slidingMenu.toggle();
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //防止频繁点击返回键，导致侧滑一直在滑动状态
            if (slidingMenuToggleStatus == true) {
                if ((System.currentTimeMillis() - mSlidingMenuTime) > 1000){
                    mSlidingMenuTime = System.currentTimeMillis();
                    slidingMenu.toggle();
                }
                return true;
            }

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.makeText(mContext, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                //模拟Home键操作
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        }

        //拦截MENU按钮点击事件，让它无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
