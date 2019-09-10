package com.dckj.jiuzhu.view.menu;


import com.dckj.jiuzhu.view.fragment.HomeTabFragment;
import com.dckj.jiuzhu.view.fragment.UnprocessedOrderTabFragment;
import com.dckj.jiuzhu.view.fragment.ProcessedOrderTabFragment;
import com.dckj.jiuzhu.view.fragment.BacklogTabFragment;
import com.dckj.jiuzhu.view.fragment.StatisticsTabFragment;
import com.dckj.jiuzhu.view.fragment.SettingsTabFragment;
import com.dckj.jiuzhu.view.fragment.ApplyFragment;
import com.dckj.jiuzhu.R;

public enum MainTabs {
    //首页
    Home(0, R.string.menu_home_page, R.drawable.selector_tab_home, HomeTabFragment.class),
    //全部未处理订单
    UnprocessedOrder(1, R.string.menu_unprocessed_order_page, R.drawable.selector_tab_home, UnprocessedOrderTabFragment.class),
    //已处理订单
    ProcessedOrder(2, R.string.menu_processed_order_page, R.drawable.selector_tab_home, ProcessedOrderTabFragment.class),
    //今日待办订单
    Backlog(3, R.string.menu_backlog_order_page, R.drawable.selector_tab_home, BacklogTabFragment.class),
    //统计
    Statistics(4, R.string.menu_statistics_page, R.drawable.selector_tab_home, StatisticsTabFragment.class),
    //设置
    Settings(5, R.string.settings, R.drawable.selector_tab_home, SettingsTabFragment.class),
    //申请
    Apply(6, R.string.apply, R.drawable.selector_tab_home, ApplyFragment.class);

    private int i;
    private int name;
    private int icon;
    private Class<?> cla;

    MainTabs(int i, int name, int icon, Class<?> cla) {
        this.i = i;
        this.name = name;
        this.icon = icon;
        this.cla = cla;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class<?> getCla() {
        return cla;
    }

    public void setCla(Class<?> cla) {
        this.cla = cla;
    }
}
