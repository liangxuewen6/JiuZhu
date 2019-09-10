package com.dckj.jiuzhu.module.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WaterUtils {

	public static Bitmap createBitmap(Bitmap src, String title, String title2) {
		if (src == null) {
			return src;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		
	
		Bitmap newBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas mCanvas = new Canvas(newBitmap);
		mCanvas.drawBitmap(src, 0, 0, null);
		Paint paint = new Paint();
		//paint.setAlpha(100);
		
		if (null != title) {
			Paint textPaint = new Paint();
			textPaint.setColor(Color.WHITE);
			textPaint.setTextSize(100);
			String familyName = "monospace";
			Typeface typeface = Typeface.create(familyName,
					Typeface.BOLD_ITALIC);
			textPaint.setTypeface(typeface);
			textPaint.setTextAlign(Align.LEFT);
			mCanvas.drawText(title, 10, h-150, textPaint);
			
		}
		if (null != title2) {
			Paint textPaint = new Paint();
			textPaint.setColor(Color.WHITE);
			textPaint.setTextSize(100);
			String familyName = "monospace";
			Typeface typeface = Typeface.create(familyName,
					Typeface.BOLD_ITALIC);
			textPaint.setTypeface(typeface);
			textPaint.setTextAlign(Align.LEFT);
			mCanvas.drawText(title2, 10, h-25, textPaint);

		}
		mCanvas.save();
		mCanvas.restore();
		return newBitmap;
	}

	public static String addText(Bitmap src, String sLocation, String PhotoPath)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time = sdf.format(new Date(System.currentTimeMillis()));

		Bitmap img = WaterUtils.createBitmap(src, sLocation,time);
		String newStr = PhotoPath.substring(
				PhotoPath.lastIndexOf("/") + 1,
				PhotoPath.lastIndexOf("."))+"_W";

		String strW=FileUtils.saveBitmap(img, newStr);


		File f = new File(PhotoPath);
		if (f.exists()) {
			f.delete();
		}
		return strW;
	}
}
