package com.dckj.jiuzhu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;


public class Member implements Parcelable {
    private String id;      //id
    private String name;    //姓名
    private String idnumber; //身份证
    private String tel; //电话
    private String relation; //关系
    private String appId;
    private String qu;  //区
    private String jd;  //街道
    private String sq;  //社区
    private String addr;//地址
    private String sex; //性别
    private String reasons;//原因

    public Member() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getDispAddr() {
        return qu + " " + jd + " " + sq;
    }

    public String getDispDt() {
        return sex;
    }

    public String getDisp() {
        return name + " " + idnumber;
    }

    public boolean isOwner() {

        if (relation != null && relation.equals("本人")) {
            return true;
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public Member setName(String name) {
        this.name = name;
        return this;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public Member setIdnumber(String idnumber) {
        this.idnumber = idnumber;
        return this;
    }

    public String getTel() {
        return tel;
    }

    public Member setTel(String tel) {
        this.tel = tel;
        return this;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getQu() {
        return qu;
    }

    public void setQu(String qu) {
        this.qu = qu;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getSq() {
        return sq;
    }

    public void setSq(String sq) {
        this.sq = sq;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getsex() {
        return sex;
    }

    public void setsex(String sex) {
        this.sex = sex;
    }

    public String getreasons() {
        return reasons;
    }

    public void setreasons(String reasons) {
        this.reasons = reasons;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.idnumber);
        dest.writeString(this.tel);
        dest.writeString(this.relation);
        dest.writeString(this.appId);
        dest.writeString(this.qu);
        dest.writeString(this.jd);
        dest.writeString(this.sq);
        dest.writeString(this.addr);
        dest.writeString(this.sex);
    }

    protected Member(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.idnumber = in.readString();
        this.tel = in.readString();
        this.relation = in.readString();
        this.appId = in.readString();
        this.qu = in.readString();
        this.jd = in.readString();
        this.sq = in.readString();
        this.addr = in.readString();
        this.sex = in.readString();
    }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
