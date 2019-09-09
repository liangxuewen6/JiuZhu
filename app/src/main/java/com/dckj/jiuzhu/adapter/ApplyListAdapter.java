package com.dckj.jiuzhu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dckj.jiuzhu.R;
import com.dckj.jiuzhu.bean.Member;
import com.dckj.jiuzhu.view.activity.SurveyActivity;


public class ApplyListAdapter extends ListBaseAdapter<Member> {
    private Context mContext;

    public ApplyListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        View contentView = holder.getView(R.id.swipe_content);
        ImageView useriamge = holder.getView(R.id.icon);
        TextView memberName = holder.getView(R.id.member_name);
        TextView memberSex = holder.getView(R.id.member_sex);
        TextView memberTel = holder.getView(R.id.member_tel);
        TextView memberIdCard = holder.getView(R.id.member_idcard);
        TextView memberAddress = holder.getView(R.id.member_address);


        final Member cm = (Member) getDataList().get(position);
        if ("男".equals(cm.getsex())) {
            useriamge.setImageResource(R.mipmap.user_male);
        } else {
            useriamge.setImageResource(R.mipmap.user_female);
        }

        memberName.setText(cm.getName());
        memberSex.setText(cm.getsex());
        memberTel.setText(cm.getTel());
        memberIdCard.setText(cm.getIdnumber());
        memberAddress.setText(cm.getDispAddr() +" "+cm.getAddr());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, cm.getId() + "-----", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, SurveyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_UUID", cm.getId());
                bundle.putString("user_name", cm.getName());
                bundle.putString("user_id", cm.getIdnumber());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }


}

