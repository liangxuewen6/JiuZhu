package com.dckj.jiuzhu.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dckj.jiuzhu.MainActivity;
import com.dckj.jiuzhu.R;
import com.dckj.jiuzhu.adapter.ApplyListAdapter;
import com.dckj.jiuzhu.bean.Member;
import com.dckj.jiuzhu.module.config.AppConfig;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 首页
 */

public class BacklogTabFragment extends BaseFragment {

    @BindView(R.id.tv_titlebar_center)
    TextView tvTitlebarCenter;
    @BindView(R.id.iv_titlebar_left)
    ImageView ivTitlebarLeft;
    /*@BindView(R.id.iv_titlebar_right)
    ImageView ivTitlebarRight;*/
    @BindView(R.id.myrefreshrecyclerview)
    LRecyclerView mRecyclerView;


    private LRecyclerViewAdapter mRecyclerViewAdapter;
    private ApplyListAdapter mDataAdapter;
    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;

    /**
     * 已经获取到多少条数据了
     */
    private static int mCurrentCounter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        ((TextView) mView.findViewById(R.id.tv_titlebar_center)).setText(R.string.title_backlog_order_page);
        initRecyclerView();
    }

    @Override
    protected void initLayoutRes() {
        mLayoutRes = R.layout.fragment_tab_order;
        mLayoutResV19 = R.layout.fragment_tab_order_v19;
    }

    @Override
    protected void initMenuRes() {
        mMenuRes = R.menu.menu_home_page;
    }

    @Override
    protected void clickMenuItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tv_test_menu:
            Toast.makeText(mContext,"click menu item",Toast.LENGTH_SHORT).show();
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

    private void initRecyclerView() {
        ArrayList<Member> dataList;
        dataList = loadData(15);
        mDataAdapter = new ApplyListAdapter(mContext);
        mDataAdapter.setDataList(dataList);

//      mDataAdapter.setDataList(list);//重新添加一个集合数据
//      mDataAdapter.addAll(allList);//在之前数据的基础上增加一个集合数据
//      mDataAdapter.add("数据");//在之前数据的基础上增加一条数据
//      mDataAdapter.remove(2);//删除指定下标的一条数据
//      mDataAdapter.clear();//清除所有数据

        mRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        //mRecyclerView.setLScrollListener(mLScrollListenernew);


        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v("lxw","----onRefresh----");
                mDataAdapter.clear();
                mRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                mDataAdapter.setDataList(loadData(30));
                //requestData();
                mRecyclerView.refreshComplete(REQUEST_COUNT);

            }
        });

        //mRecyclerView.refresh();
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(true);

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                Log.v("lxw","----onLoadMore----");
                mRecyclerView.setNoMore(true);
                /*if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    //requestData();

                } else {
                    //the end
                    mRecyclerView.setNoMore(true);
                }*/
                mDataAdapter.clear();
                mDataAdapter.setDataList(loadData(60));
                //requestData();
                //mRecyclerView.refreshComplete(REQUEST_COUNT);
            }
        });
        mRecyclerView.refreshComplete(REQUEST_COUNT);

    }


    private LRecyclerView.LScrollListener mLScrollListenernew = new LRecyclerView.LScrollListener() {
        //scroll down to up
        @Override
        public void onScrollUp() {
            Log.v("lxw","----onScrollUp----");
        }

        //scroll from up to down
        @Override
        public void onScrollDown(){
            Log.v("lxw","----onScrollDown----");

        }

        // moving state,you can get the move distance
        @Override
        public void onScrolled(int distanceX, int distanceY) {
            Log.v("lxw","----onScrolled----");
        }

        @Override
        public void onScrollStateChanged(int state) {
            Log.v("lxw","----onScrollStateChanged----");
        }

    };

    public ArrayList<Member> loadData(int count) {
        ArrayList<Member> list = new ArrayList<>();

        for (int a = 0; a < count; a++) {
            Member member = new Member();
            member.setName("张"+a);
            member.setIdnumber("000000000000000"+a);
            String sex;
            if (a%2 ==0) {
                sex = "男";
            } else {
                sex = "女";
            }
            member.setsex(sex);
            member.setTel("1381409693"+a);
            member.setQu("镇江");
            member.setJd("江宁路");
            member.setSq("湖熟社区");
            member.setAddr("21世纪花园15-1603");
            member.isOwner();
            list.add(member);
        }
        return list;
    }
}
