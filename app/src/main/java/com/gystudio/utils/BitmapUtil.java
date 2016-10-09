package com.gystudio.utils;
 

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * 对图片进行处理
 * 
 * @author Administrator
 * 
 */
public class BitmapUtil {

	/**
	 * 将图片转换成byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 从服务器取图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setConnectTimeout(0);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 加载本地图片 
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 改变图片的尺寸
	 * 
	 * @param d
	 * @param scaleX
	 * @param scaleY
	 * @return
	 */
	public static Bitmap changeImageSize(Bitmap d, int x, int y) {
		Bitmap bmp = null;
		float scaleX = (float) x / (float) d.getWidth();
		float scaleY = (float) y / (float) d.getHeight();
		Matrix m = new Matrix();
		m.postScale(scaleX, scaleY);
		bmp = Bitmap
				.createBitmap(d, 0, 0, d.getWidth(), d.getHeight(), m, true);
		return bmp;
	}

	/**
	 * 旋转图片
	 * 
	 * @return
	 */
	public static Bitmap rotateImage(Bitmap b) {
		Matrix m = new Matrix();
		m.setRotate(90);// 旋转90度
		b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
		return b;
	}
}

