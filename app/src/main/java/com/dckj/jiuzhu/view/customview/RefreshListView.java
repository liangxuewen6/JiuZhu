package com.dckj.jiuzhu.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dckj.jiuzhu.R;

import java.text.SimpleDateFormat;

/**
 * 包含下拉刷新功能的listView
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {


    private View mHeaderView;
    private int mHeaderViewHeight;
    private int mFooterViewHeight;
    private float dowY;//按下的Y轴的距离
    private float moveY;//移动的Y轴的距离
    public static final int PULL_TO_REFRESH = 0;// 下拉刷新
    public static final int RELEASE_REFRESH = 1;// 释放刷新
    public static final int REFRESHING = 2; // 刷新中
    private int currentState = PULL_TO_REFRESH; // 当前刷新模式
    private RotateAnimation rotateUpAnim; // 箭头向上动画
    private RotateAnimation rotateDownAnim; // 箭头向下动画
    private ImageView ivArrow;//箭头图片
    private ProgressBar pbProgress;//滚动条
    private TextView tvTitle;//头部局标题
    private TextView tvDescLastRefresh;//时间刷新内容
    private OnRefreshListener mListener; // 刷新监听
    private View mFooterView;
    public static int SCROLL_STATE_IDLE = 0; // 空闲
    public static int SCROLL_STATE_TOUCH_SCROLL = 1; // 触摸滑动
    public static int SCROLL_STATE_FLING = 2; // 滑翔
    private boolean isLoadingMore; // 是否正在加载更多

    /**
     * 代码里面
     *
     * @param context
     */
    public RefreshListView(Context context) {
        super(context);
        init();

    }


    /**
     * 包含属性
     *
     * @param context
     * @param attrs
     */
    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 包含样式
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化头布局, 脚布局
     * 滚动监听
     */
    private void init() {
        initHeaderView();
        initAnimation();

        initFooterView();
        setOnScrollListener(this);
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.layout_listview_header_list, null);

        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvDescLastRefresh = (TextView) mHeaderView.findViewById(R.id.tv_desc_last_refresh);

        /*提前手动测量宽高*/
        mHeaderView.measure(0, 0);
        /*获取自身高度*/
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        /*设置内边距，可以隐藏布局  -自身高度*/
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        addHeaderView(mHeaderView);
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.layout_listview_footer_list, null);
        /*提前手动测量宽高*/
        mFooterView.measure(0, 0);
        /*获取自身高度*/
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        /*设置内边距，可以隐藏布局  -自身高度*/
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        addFooterView(mFooterView);
    }

    /**
     * 初始化头布局的动画
     */
    private void initAnimation() {
        // 向上转, 围绕着自己的中心, 逆时针旋转0 -> -180.
        rotateUpAnim = new RotateAnimation(0f, -180f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUpAnim.setDuration(300);
        // 动画停留在结束位置
        rotateUpAnim.setFillAfter(true);
        // 向下转, 围绕着自己的中心, 逆时针旋转 -180 -> -360
        rotateDownAnim = new RotateAnimation(-180f, -360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDownAnim.setDuration(300);
        rotateDownAnim.setFillAfter(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        /*判断滑动距离，给Header设置paddingTop*/
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dowY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = ev.getY();
                // 如果是正在刷新中, 就执行父类的处理
                if (currentState == REFRESHING) {
                    return super.onTouchEvent(ev);
                }

                // 移动的偏移量
                float offset = moveY - dowY;

                if (offset > 200) {
                    offset = 200;
                }
                // 只有 偏移量>0, 并且当前第一个可见条目索引是0, 才放大头部
                if (offset > 0 && getFirstVisiblePosition() == 0) {
                    //int paddingTop = -自身高度 + 偏移量
                    int paddingTop = (int) (-mHeaderViewHeight + offset);
                    mHeaderView.setPadding(0, paddingTop, 0, 0);
                    if (paddingTop >= 0 && currentState != RELEASE_REFRESH) {// 头布局完全显示
                        // 切换成释放刷新模式
                        currentState = RELEASE_REFRESH;
                        Log.d("RefreshListView", "释放刷新");
                        updateHeader();
                    } else if (paddingTop < 0 && currentState != PULL_TO_REFRESH) {// 头布局不完全显示
                        // 切换下拉刷新模式
                        Log.d("RefreshListView", "下拉刷新");
                        currentState = PULL_TO_REFRESH;
                        updateHeader();
                    }
                    return true; // 当前事件被我们处理并消费
                }

                break;
            case MotionEvent.ACTION_UP:
                // 根据刚刚设置状态
                if (currentState == PULL_TO_REFRESH) {
                    //- paddingTop < 0 不完全显示, 恢复
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                } else {
                    //- paddingTop >= 0 完全显示, 执行正在刷新...
                    mHeaderView.setPadding(0, 0, 0, 0);
                    currentState = REFRESHING;
                    updateHeader();
                }
                break;

        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据状态更新头布局内容
     */
    private void updateHeader() {
        switch (currentState) {
            case PULL_TO_REFRESH://切换成下拉刷新
                //切换动画，改标题
                ivArrow.startAnimation(rotateDownAnim);
                tvTitle.setText("下拉刷新");
                break;
            case RELEASE_REFRESH://切换成释放刷新
                //切换动画，改标题
                ivArrow.startAnimation(rotateUpAnim);
                tvTitle.setText("释放刷新");
                break;
            case REFRESHING://切换成正在刷新
                //切换动画改标题
                ivArrow.clearAnimation();
                ivArrow.setVisibility(INVISIBLE);
                pbProgress.setVisibility(VISIBLE);
                tvTitle.setText("正在刷新中");
                if (mListener != null) {
                    mListener.onRefresh();
                }
                break;
        }
    }

    /**
     * 刷新结束恢复界面效果
     */
    public void onRefreshComplete() {
        /*是否加载更多*/
        if (isLoadingMore) {
            // 加载更多
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
            isLoadingMore = false;
        } else {
            // 下拉刷新
            currentState = PULL_TO_REFRESH;
            tvTitle.setText("下拉刷新");
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            ivArrow.setVisibility(VISIBLE);
            pbProgress.setVisibility(INVISIBLE);
            String time = getTime();
            tvDescLastRefresh.setText("最后刷新时间:" + time);
        }

    }

    private String getTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(currentTimeMillis);
    }

    public void setRefreshListener(OnRefreshListener mListener) {
        this.mListener = mListener;
    }

    /**
     * *  public static int SCROLL_STATE_IDLE = 0; // 空闲
     * public static int SCROLL_STATE_TOUCH_SCROLL = 1; // 触摸滑动
     * public static int SCROLL_STATE_FLING = 2; // 滑翔
     *
     * @param view        The view whose scroll state is being reported
     * @param scrollState The current scroll state. One of
     *                    {@link #SCROLL_STATE_TOUCH_SCROLL} or {@link #SCROLL_STATE_IDLE}.
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 状态更新的时候
        if (isLoadingMore) {
            return; // 已经在加载更多.返回
        }
        // 最新状态是空闲状态, 并且当前界面显示了所有数据的最后一条. 加载更多
        if (scrollState == SCROLL_STATE_IDLE && getLastVisiblePosition() >= (getCount() - 1)) {
            isLoadingMore = true;
            Log.d("RefreshListView", "显示加载更多");
            mFooterView.setPadding(0, 0, 0, 0);
            setSelection(getCount());
            if (mListener != null) {
                mListener.onLoadMore();
            }
        }
    }

    /**
     * Callback method to be invoked when the list or grid has been scrolled. This will be
     * called after the scroll has completed
     *
     * @param view             The view whose scroll state is being reported
     * @param firstVisibleItem the index of the first visible cell (ignore if
     *                         visibleItemCount == 0)
     * @param visibleItemCount the number of visible cells
     * @param totalItemCount   the number of items in the list adaptor
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


    }

    public interface OnRefreshListener {
        void onRefresh(); // 下拉刷新

        void onLoadMore();// 加载更多
    }
}
