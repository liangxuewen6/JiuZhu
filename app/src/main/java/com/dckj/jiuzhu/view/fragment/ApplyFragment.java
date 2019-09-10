package com.dckj.jiuzhu.view.fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dckj.jiuzhu.MainActivity;
import com.dckj.jiuzhu.R;
import com.dckj.jiuzhu.view.activity.SurveyActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ApplyFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.tv_titlebar_center)
    TextView tvTitlebarCenter;
    @BindView(R.id.iv_titlebar_left)
    ImageView ivTitlebarLeft;
    /*@BindView(R.id.iv_titlebar_right)
    ImageView ivTitlebarRight;*/
    @BindView(R.id.apply_member_info_container)
    LinearLayout mApplyMemberInfoContainer;
    @BindView(R.id.apply_add_members)
    Button mAddMembersBtn;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        ((TextView) mView.findViewById(R.id.tv_titlebar_center)).setText(R.string.apply);
        mAddMembersBtn.setOnClickListener(this);
    }

    @Override
    protected void initLayoutRes() {
        mLayoutRes = R.layout.fragment_tab_apply;
        mLayoutResV19 = R.layout.fragment_tab_apply;
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

    private void addMembersInfo() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_apply_member_info, null);
        mApplyMemberInfoContainer.addView(layout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_add_members:
                addMembersInfo();
                break;
        }
    }
}
