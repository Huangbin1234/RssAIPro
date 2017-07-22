package com.hb.rssai.zxing;

import android.os.Environment;
import android.util.Log;

import java.io.File;


public class FileDownloadUtil {
	/**
	 *
	 * @return
	 */
	public static String getDefaultLocalDir(String subDir) {

		String path_root = getSDcardRoot();
		// String path_root = VideoApplication.sdCardRoot;
		if (path_root == null) {
			return null;
		}

		String path_dir = path_root + subDir;

		return makeDir(path_dir);
	}
	public static String getSDcardRoot() {

		if (!StorageUtil.isExternalStorageAvailable()) {
			return null;
		}
		File root = Environment.getExternalStorageDirectory();
		String path_root = root.getAbsolutePath();

		return path_root;
	}
	public static String makeDir(String path_dir) {
		File dir = new File(path_dir);
		if (!dir.exists()) {
			boolean success = dir.mkdirs();
			if (!success) {
				return null;
			} else {
				Log.i("test", "makeDir:" + path_dir);
			}
		}

		return path_dir;
	}
}
