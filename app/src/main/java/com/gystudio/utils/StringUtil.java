package com.gystudio.utils;
 
import java.io.IOException;

import org.gystudio.kobjects.base64.Base64;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;


public class StringUtil {

	public static final String INTENT_ACTION = "android.intent.action.VIEW";
	public static final String URL = "url";
	public static final String FLAG = "flag";
	public static final String TITLE = "title";

	/**
	 * 格式化时间（精确到日）
	 * 
	 * @return
	 */
	public static String formatDate(String date) {
		if (date != null && !"".equals(date)) {
			date = date.split(" ")[0];
		}
		return date;
	}

	/**
	 * 格式化期限时间
	 * 
	 * @return
	 */
	public static String formatLimitDate(String start, String end) {
		String limit = "";
		if (start != null && !"".equals(start)) {
			limit = start + " 至 ";
		}
		if (end != null && !"".equals(end)) {
			limit += end;
		} else {
			limit += "今";
		}

		if (start == null || "".equals(start)) {
			limit = "";
		}
		return limit;
	}

	/**
	 * 过滤Json中的特殊字符
	 * 
	 * @param s
	 * @return
	 */
	public static String FilterJsonChar(String s) {
		String[] list = new String[] { "\r", "\n", "\r\n", "\t" };
		String[] listvalue = new String[] { "{@#$%^}", "{@#$%^}", "{@#$%^}", "" };
		for (int i = 0; i <= list.length - 1; i++) {
			s = s.replace(list[i], listvalue[i]);
		}
		return s;
	}

	/**
	 * 过滤Json中的指定字符
	 * 
	 * @param s
	 * @param oldChar
	 * @param newChar
	 * @return
	 */
	public static String ReplaceChar(String s, String oldChar, String newChar) {
		s = s.replace(oldChar, newChar);
		return s;
	}

	/**
	 * 将字符串中的换行符换回来
	 * 
	 * @param s
	 * @return
	 */
	public static String ReplaceEnter(String s) {
		s = ReplaceChar(s, "{@#$%^}", "\n");
		return s;
	}

	/**
	 * 剪辑字符串
	 * 
	 * @param s
	 *            原字符串内容
	 * @param Length
	 *            长度
	 * @return
	 */
	public static String CutString(String s, int Length) {
		if (s == null) {
			return "";
		}
		if (s.trim() == "") {
			return s;
		}
		if (s.trim().length() <= Length) {
			return s;
		} else {
			return s.trim().substring(0, Length) + "...";
		}
	}

	/**
	 *将字节数组转换成Base64String
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String ConvertByteArrayToBase64(byte[] encryptedData) {
		String encryptedtext = Base64.encode(encryptedData);
		return encryptedtext;
	}

	/**
	 * 设置字符串为打电话、发短信、发邮件
	 * 
	 * @param str
	 *            目标字符串
	 * @param action
	 *            点击字符串执行的操作
	 * @param view
	 *            用于显示字符串的视图
	 */
	public static void setSpanUrlString(String str, int action, TextView view) {
		String url = "";
		switch (action) {
		case 1:// 打电话
			url = "tel:" + str;
			break;
		case 2:// 发短信
			url = "smsto:" + str;
			break;
		case 3:// 发邮件
			url = "mailto:" + str;
			break;
		case 4://
			url = str;
			break;
		}
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new ForegroundColorSpan(Color.GREEN), 0, str.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置颜色
		ss.setSpan(new URLSpan(url), 0, str.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置为打电话
		ss.setSpan(new UnderlineSpan(), 0, str.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置下划线
		view.setText(ss);
		view.setMovementMethod(LinkMovementMethod.getInstance());
	}
 
 
}
