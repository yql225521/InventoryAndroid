package com.gystudio.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Environment;

/**
 * @author Administrator 对SDcard进行操作
 */
public class SDcardUtil {
	public static final String PATH = Environment.getExternalStorageDirectory()
			+ "/MobEnfoLaw/";
	public static String message;
	public static final String IMAGEPOSTFIX = ".png";
	public static final String VIDEOPOSTFIX = ".3gp";
	public static final String DOCPOSTFIX = ".doc";
	public static final String TXTPOSTFIX = ".txt";

	/**
	 * 是否存在SDcard
	 * 
	 * @return
	 */
	public static boolean isExistsSdcard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * 将文件写入sdcard中
	 * 
	 * @return
	 * @throws IOException
	 */
	public static int wirteFile(String filePath, InputStream is)
			throws IOException {
		int fileSize = 0;// 文件的大小
		if (isExistsSdcard()) {// 判断SDcard是否存在
			File directory = new File(PATH);// 目录
			if (!directory.exists()) {// 如果不存在，创建此目录
				directory.mkdirs();
			}
			FileOutputStream fileOutputStream = null;
			if (is != null) {
				File file = new File(filePath);
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				while ((ch = is.read(buf)) != -1) {
					fileSize += ch;
					fileOutputStream.write(buf, 0, ch);
				}
				fileOutputStream.flush();
			}
			if (fileOutputStream != null) {
				fileOutputStream.close();
				return fileSize;
			}
		}
		return fileSize;
	}
	/**
	 * 将文件写入sdcard中
	 * 
	 * @return
	 * @throws IOException
	 */
	public static int wirteFile(String fileName, InputStream is, String postfix)
			throws IOException {
		int fileSize = 0;// 文件的大小
		if (isExistsSdcard()) {// 判断SDcard是否存在
			File directory = new File(PATH);// 目录
			if (!directory.exists()) {// 如果不存在，创建此目录
				directory.mkdirs();
			}
			FileOutputStream fileOutputStream = null;
			if (is != null) {
				File file = new File(SDcardUtil.PATH, fileName + postfix);
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				while ((ch = is.read(buf)) != -1) {
					fileSize += ch;
					fileOutputStream.write(buf, 0, ch);
				}
				fileOutputStream.flush();
			}
			if (fileOutputStream != null) {
				fileOutputStream.close();
				return fileSize;
			}
		}
		return fileSize;
	}

	/**
	 * 读文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String fileName ,String postfix) throws IOException {
		if (isExistsSdcard()) {// 判断SDcard是否存在
			File direstory = new File(PATH + fileName+postfix);
			if (direstory.exists()) {
				FileInputStream isFile = new FileInputStream(direstory);
				StringBuffer sb = new StringBuffer();
				byte[] b = new byte[1024];
				int cu = -1;
				while ((cu = isFile.read(b)) != -1) {
					sb.append(new String(b, 0, cu, "GB2312"));
				}
				return sb.toString();
			}
		}
		return null;
	}

	/**
	 * 将图片写入SDcard中
	 * 
	 * @param fileName
	 * @param mBitmap
	 * @return
	 */
	public static boolean wirteBitmap(String fileName, Bitmap mBitmap) {
		if (isExistsSdcard()) {// 判断SDcard是否存在
			File directory = new File(PATH);// 目录
			if (!directory.exists()) {// 如果不存在，创建此目录
				directory.mkdirs();
			}
			File file = new File(PATH + fileName);// 文件
			if (!file.exists()) {
				try {
					BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(file));// 向文件中写入数据
					boolean isSuccess = mBitmap.compress(
							Bitmap.CompressFormat.PNG, 100, stream);
					if (!isSuccess) {
						message = "保存失败！";
					} else {
						message = "保存成功！";
					}
					stream.flush();
					stream.close();
					return isSuccess;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				message = "已保存！";
				return false;
			}
		} else {
			message = "SDcard不存在！";
		}
		return false;
	}

	/**
	 * 读取SDcard中的本应用目录下的所有图片或视频的路径
	 * 
	 * @return
	 */
	public static ArrayList<String> readFilesPath(String postfix) {
		if (isExistsSdcard()) {// 判断SDcard是否存在
			File directory = new File(PATH);// 目录
			if (!directory.exists()) {
				message = "目前没有图片！";
				return null;
			}
			File[] files = directory.listFiles();
			ArrayList<String> filesPath = new ArrayList<String>();
			for (int i = 0; i < files.length; i++) {
				String path = files[i].getAbsolutePath();
				if (path.endsWith(postfix)) {
					filesPath.add(path);
				}
			}
			return filesPath;
		} else {
			message = "SDcard不存在！";
		}
		return null;
	}

	/**
	 * 删除文件
	 */
	public static boolean deleteFile(String file) {
		if (isExistsSdcard()) {// 判断SDcard是否存在
			File directory = new File(PATH + file);
			if (directory.exists()) {
				boolean isSuccess = directory.delete();
				return isSuccess ;
			}
		}
		return false;
	}
	
	/**
	 * 删除文件
	 */
	public static boolean deleteFile(String file , int flag) {
		if (isExistsSdcard()) {// 判断SDcard是否存在
			File directory = new File(file );
			if (directory.exists()) {
				boolean isSuccess = directory.delete();
				return isSuccess ;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isExistsFile(String file) {
		if (isExistsSdcard()) {// 判断SDcard是否存在
			File directory = new File(file);
			if (directory.exists()) {
				return true;
			}
		}
		return false;
	}

}
