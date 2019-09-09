package com.dckj.jiuzhu.module;

import android.os.Environment;

public class Constants {

    // SDCard路径
    public static final String SD_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();

    // 图片存储路径
    public static final String BASE_PATH = SD_PATH + "/dckj/jiuzhu/";

    // 缓存图片路径
    public static final String BASE_IMAGE_CACHE = BASE_PATH + "cache/images/";
}
