package com.dckj.jiuzhu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dckj.jiuzhu.R;
import com.dckj.jiuzhu.bean.Bimp;
import com.dckj.jiuzhu.module.util.FileUtils;

import java.io.IOException;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater; // 视图容器
    private int selectedPosition = -1;// 选中的位置
    private boolean shape;

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public GridAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void update() {
        loading();
    }

    public int getCount() {
        return (Bimp.bmp.size() + 1);
    }

    public Object getItem(int arg0) {

        return null;
    }

    public long getItemId(int arg0) {

        return 0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        final int coord = position;
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.layout_survey_gridview,
                    parent, false);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.item_grid_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == Bimp.bmp.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.icon_addpic_unfocused));
			/*	if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}*/
        } else {
            holder.image.setImageBitmap(Bimp.bmp.get(position));
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void loading() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (Bimp.max == Bimp.drr.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        try {
                            String path = Bimp.drr.get(Bimp.max);
                            System.out.println(path);
                            Bitmap bm = Bimp.revisionImageSize(path);//压缩图片
                            Bimp.bmp.add(bm);
                            String newStr = path.substring(
                                    path.lastIndexOf("/") + 1,
                                    path.lastIndexOf(".")) + "_S";
                            FileUtils.saveBitmap(bm, "" + newStr);
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}