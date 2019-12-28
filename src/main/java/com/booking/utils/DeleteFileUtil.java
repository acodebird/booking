package com.booking.utils;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

//删除文件
public class DeleteFileUtil {
	public static void deleteFile(String filePath, HttpServletRequest req) {
		String realPath = req.getServletContext().getRealPath(filePath);
		File file = new File(realPath);
		if(file.exists()) {
			file.delete();
		}
	}
}
