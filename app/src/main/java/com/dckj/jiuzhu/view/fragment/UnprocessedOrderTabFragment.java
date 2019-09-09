package com.dckj.jiuzhu.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dckj.jiuzhu.MainActivity;
import com.dckj.jiuzhu.R;
import com.dckj.jiuzhu.adapter.ApplyListAdapter;
import com.dckj.jiuzhu.bean.Member;
import com.dckj.jiuzhu.view.customview.RefreshListView;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单
 */

public class UnprocessedOrderTabFragment extends BaseFragment {

    @BindView(R.id.tv_titlebar_center)
    TextView tvTitlebarCenter;
    @BindView(R.id.iv_titlebar_left)
    ImageView ivTitlebarLeft;
    /*@BindView(R.id.iv_titlebar_right)
    ImageView ivTitlebarRight;*/


    @BindView(R.id.myrefreshlistview)
    RefreshListView mMyListView;
    @BindView(R.id.myrefreshrecyclerview)
    LRecyclerView mRecyclerView;


    private LRecyclerViewAdapter mRecyclerViewAdapter;
    private ApplyListAdapter mDataAdapter;


    private ArrayList<String> listDatas;
    private MyAdapter myAdapter;
    private Handler mHandler;
    private boolean isRefresh = false;
    /**
     * 服务器端一共多少条数据
     */
    private static int TOTAL_COUNTER = 64;

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
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        ((TextView) mView.findViewById(R.id.tv_titlebar_center)).setText(R.string.title_unprocessed_order_page);
        //initListListView();
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


    private void initListListView() {
        /*产生测试的数据*/
        listDatas = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            listDatas.add("这是一条ListView数据: " + i);
        }
        /*数据适配器*/
        myAdapter = new MyAdapter();
        mMyListView.setAdapter(myAdapter);
        mMyListView.setRefreshListener(mRefreshListener);

    }

    private RefreshListView.OnRefreshListener mRefreshListener = new RefreshListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    listDatas.add(0, "我是下拉刷新的数据" + Math.random() * 10 + "");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.notifyDataSetChanged();
                            mMyListView.onRefreshComplete();
                        }
                    });
                }
            }).start();
        }

        @Override
        public void onLoadMore() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    listDatas.add("我是加载更多的数据" + Math.random() * 10 + "");
                    listDatas.add("我是加载更多的数据" + Math.random() * 10 + "");
                    listDatas.add("我是加载更多的数据" + Math.random() * 10 + "");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.notifyDataSetChanged();
                            mMyListView.onRefreshComplete();
                        }
                    });
                }
            }).start();
        }
    };

    class MyAdapter extends BaseAdapter {
        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return listDatas.size();
        }


        @Override
        public Object getItem(int position) {
            return listDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(parent.getContext());
            textView.setTextSize(18f);
            textView.setTextColor(Color.BLUE);
            textView.setText(listDatas.get(position));
            return textView;
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
                isRefresh = true;
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


