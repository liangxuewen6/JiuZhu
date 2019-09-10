package com.dckj.jiuzhu.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dckj.jiuzhu.R;
import com.dckj.jiuzhu.adapter.GridAdapter;
import com.dckj.jiuzhu.adapter.NoScrollGridAdapter;
import com.dckj.jiuzhu.bean.Bimp;
import com.dckj.jiuzhu.bean.Member;
import com.dckj.jiuzhu.module.Constants;
import com.dckj.jiuzhu.module.config.ImageLoaderConfig;
import com.dckj.jiuzhu.module.util.DensityUtil;
import com.dckj.jiuzhu.module.util.FileUtils;
import com.dckj.jiuzhu.module.util.WaterUtils;
import com.dckj.jiuzhu.view.customview.NoScrollGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SurveyActivity extends BaseActivity {

    private LinearLayout mRelationMembersLayout;
    private LinearLayout mSurveyItemsLayout;
    private Button mAssignedButton;
    private int mMembersCount = 0;
    private String mAssginedname;
    private GridAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_survey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.title_bar);
        setSupportActionBar(toolbar);
        mRelationMembersLayout = (LinearLayout) findViewById(R.id.relation_member_layout);
        mSurveyItemsLayout = findViewById(R.id.survey_items_layout);
        ((TextView) findViewById(R.id.tv_titlebar_center)).setText("调查");
        ImageView ivTitlebarLeft = findViewById(R.id.iv_titlebar_left);
        ivTitlebarLeft.setVisibility(View.INVISIBLE);
        mAssignedButton = findViewById(R.id.assigned_button);
        mAssignedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoice();
            }
        });

        loadIntentExtra();
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfig.initImageLoader(this, Constants.BASE_IMAGE_CACHE);
        }

        Member member = new Member();
        member.setName("张KK");
        member.setIdnumber("000000000000000888");
        member.setsex("男");
        member.setTel("13814096930");
        member.setQu("镇江");
        member.setJd("江宁路");
        member.setSq("湖熟社区");
        member.setAddr("21世纪花园15-1603");
        member.setRelation("本人");
        member.setreasons("（一）申请农村低保待遇条件\n" +
                "申请农村低保待遇应同时具备下列条件：\n" +
                "1、持有本县农业居民户口。\n" +
                "2、居住在农村村组，家庭承包土地的农村居民。\n" +
                "3、共同生活的家庭成员人均年纯收入和实际生活水平低于当地农村低保标准（具体询问当地民政部门）。");

        //更新申请人信息
        updateApplicantInfo(member);

        Member member1 = new Member();
        member1.setName("张QQ");
        member1.setIdnumber("000000000000000888");
        member1.setsex("女");
        member1.setTel("13814096931");
        member1.setQu("镇江");
        member1.setJd("江宁路");
        member1.setSq("湖熟社区");
        member1.setAddr("21世纪花园15-1603");
        member1.setRelation("父女");

        Member member2 = new Member();
        member2.setName("王QQ");
        member2.setIdnumber("000000000000000666");
        member2.setsex("女");
        member2.setTel("13814096932");
        member2.setQu("镇江");
        member2.setJd("江宁路");
        member2.setSq("湖熟社区");
        member2.setAddr("21世纪花园15-1603");
        member2.setRelation("夫妻");

        //根据申请人提供家庭成员情况添加
        addMembersInfo(member1);
        addMembersInfo(member2);
        mMembersCount = 2;
        for (int a = 0; a < mMembersCount; a++) {
            //addView(member1);
        }

        if (mIsUpload) {
        addSurveyedItems();
        } else {
        addSurveyItems();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.update();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_survey_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.apply_menu:

                break;
            case R.id.finish_menu:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String mUserId;
    private String mUserIDCard;
    private boolean mIsUpload;
    private void loadIntentExtra() {
        Intent intent = getIntent();
        if (intent != null) {
            mUserId = intent.getStringExtra("user_UUID");
            mUserIDCard = intent.getStringExtra("user_idcard");
            mIsUpload = intent.getBooleanExtra("isUpload", false);
        }
    }

    private void updateApplicantInfo(Member member) {
        TextView applicantName = findViewById(R.id.applicant_name);
        applicantName.setText(member.getName());
        TextView applicantTel= findViewById(R.id.applicant_tel);
        applicantTel.setText(member.getTel());
        TextView applicantIdCard = findViewById(R.id.applicant_idcard);
        applicantIdCard.setText(member.getIdnumber());
        TextView applicantAddress = findViewById(R.id.applicant_address);
        applicantAddress.setText(member.getDispAddr()+" "+member.getAddr());
        TextView applicantReasons= findViewById(R.id.applicant_reasons);
        applicantReasons.setText(member.getreasons());
    }

    private void addMembersInfo(Member member) {
        mMembersCount++;
        LayoutInflater inflater = LayoutInflater.from(SurveyActivity.this);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_member_info, null);
        TextView membersNo = layout.findViewById(R.id.relationmembersno);
        membersNo.setText("成员 "+mMembersCount);

        TextView memberName = layout.findViewById(R.id.relation_member_name);
        memberName.setText(member.getName());

        TextView memberSex = layout.findViewById(R.id.relation_member_sex);
        memberSex.setText(member.getsex());

        TextView memberRelation = layout.findViewById(R.id.relation_member_relation);
        memberRelation.setText(member.getRelation());

        TextView memberIdCard = layout.findViewById(R.id.relation_member_idcard);
        memberIdCard.setText(member.getIdnumber());

        TextView memberTel = layout.findViewById(R.id.relation_member_tel);
        memberTel.setText(member.getTel());

        TextView memberAddress = layout.findViewById(R.id.relation_member_address);
        memberAddress.setText(member.getDispAddr()+" "+member.getAddr());
        mRelationMembersLayout.addView(layout);
    }

    //采集信息，未上传信息，允许编辑
    private void addSurveyItems() {
        LayoutInflater inflater = LayoutInflater.from(SurveyActivity.this);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_survery_item, null);
        EditText contentEditText = layout.findViewById(R.id.survey_content);
        contentEditText.setText("----调查内容--------调查内容--------调查内容--------调查内容--------调查内容--------调查内容--------调查内容--------调查内容-----" +
                "---调查内容--------调查内容--------调查内容--------调查内容--------调查内容----" +
                "----调查内容--------调查内容--------调查内容--------调查内容--------调查内容--------调查内容--------调查内容----" +
                "----调查内容--------调查内容--------调查内容--------调查内容--------调查内容--------调查内容--------调查内容----" +
                "----调查内容--------调查内容--------调查内容--------调查内容--------调查内容--------调查内容--------调查内容----" +
                "----调查内容--------调查内容--------调查内容--------调查内容--------调查内容--------调查内容--------调查内容----");
        GridView noScrollgridview = (GridView) layout.findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new GridAdapter(this);
        mAdapter.update();
        noScrollgridview.setAdapter(mAdapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("lxw","position = "+position+"  bimp size = "+Bimp.bmp.size());
                if ((position == 0 && Bimp.bmp.size() == 0) || position == Bimp.bmp.size()) {
                    photo();
                    //new PopupWindows(PublishedActivity.this, noScrollgridview);
                } else {
                    Intent intent = new Intent(SurveyActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });
        mSurveyItemsLayout.addView(layout);
    }


    //显示已上传信息，只许查看，不允许编辑
    private void addSurveyedItems() {
        LayoutInflater inflater = LayoutInflater.from(SurveyActivity.this);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_surveryed_item, null);
        ImageView imageView = layout.findViewById(R.id.iv_avatar);
        TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) layout.findViewById(R.id.tv_content);
        TextView tv_Time = (TextView) layout.findViewById(R.id.tv_Time);
        NoScrollGridView gridview = (NoScrollGridView) layout.findViewById(R.id.gridview);
        int colnum =  (int) (((getResources().getDisplayMetrics().widthPixels)) / DensityUtil.dip2px(100,this));
        gridview.setNumColumns(colnum);

        //tv_title.setText(Html.fromHtml("<u>"+itemEntity.getTitle()+"</u>"));
        tv_title.setText("调查信息:"+"调查信息-调查信息");
        tv_content.setText("调查内容："+"调查内容-调查内容");
        tv_Time.setText("2019-09-10 12:10:10");
        // 使用ImageLoader加载网络图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .showImageOnLoading(R.mipmap.ic_launcher) // 加载中显示的默认图片
                .showImageOnFail(R.mipmap.ic_launcher) // 设置加载失败的默认图片
                .cacheInMemory(true) // 内存缓存
                .cacheOnDisk(true) // sdcard缓存
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();
        ImageLoader.getInstance().displayImage(null, imageView, options);

        final ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("file:///storage/emulated/0/zsmz/1.jpg");
        imageUrls.add("file:///storage/emulated/0/zsmz/2.jpg");
        imageUrls.add("file:///storage/emulated/0/zsmz/3.jpg");
        imageUrls.add("file:///storage/emulated/0/zsmz/4.jpg");
        imageUrls.add("file:///storage/emulated/0/zsmz/5.jpg");
        imageUrls.add("file:///storage/emulated/0/zsmz/6.jpg");
        if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
            gridview.setVisibility(View.GONE);
        } else {
            gridview.setAdapter(new NoScrollGridAdapter(mContext, imageUrls));
        }
        // 点击回帖九宫格，查看大图
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                imageBrower(position, imageUrls);
            }
        });
        mSurveyItemsLayout.addView(layout);
    }


    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);

    }

    private void dialogChoice() {
        final String items[] = {"张三", "李四", "王五","张一","张二","张四","张五","张六","张七","张三", "李四", "王五","张一","张二","张四","张五","张六","张七"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("指派对象");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAssginedname = items[which];
                        Toast.makeText(SurveyActivity.this, mAssginedname,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(SurveyActivity.this, "确定:"+mAssginedname, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /** 拍照上传 */
    private String mPhotoPath;
    private File mPhotoFile;
    public final static int CAMERA_RESULT = 1;

    public void photo() {
        Log.v("lxw","photo");
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//开始拍照
            mPhotoPath = getSDPath()+"/"+ mUserIDCard+"_"+ getPhotoFileName();//设置图片文件路径，getSDPath()和getPhotoFileName()具体实现在下面

            mPhotoFile = new File(mPhotoPath);
            if (!mPhotoFile.exists()) {
                mPhotoFile.getParentFile().mkdirs();
                mPhotoFile.createNewFile();//创建新文件
            }

            Uri tempUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tempUri = FileProvider.getUriForFile(mContext,"com.dckj.jiuzhu", mPhotoFile);
            } else {
                tempUri = Uri.fromFile(mPhotoFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);//Intent有了图片的信息
            startActivityForResult(intent, CAMERA_RESULT);//跳转界面传回拍照所得数据
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //startActivityForResult 的返回结果
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_RESULT) {
            Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, null);
            if(bitmap != null && !bitmap.isRecycled()){//判断位图不为空
                mPhotoPath= WaterUtils.addText(bitmap,"我是水印",mPhotoPath);
                Bimp.drr.add(mPhotoPath);
            }
            bitmap.recycle();
            mAdapter.notifyDataSetChanged();
            //mImageView.setImageBitmap(bitmap);
        }
    }

    //获取sd卡路径
    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString()+ "/jiuzhu";
    }

    //获取照片文件名字
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "_yyyyMMdd_HHmmss");
        return dateFormat.format(date)  +".jpg";
    }

}
