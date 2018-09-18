package cc.alpha.base.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class FileUtils {
	/**
	 * 
	 * @param urlPath
	 *            下载路径
	 * @param downloadDir
	 *            下载存放目录
	 * @return 返回下载文件
	 */
	@SuppressWarnings("finally")
	public static File downloadFile(String urlPath, String downloadDir) {
		File file = null;
		try {
			// 统一资源
			URL url = new URL(urlPath);
			// 连接类的父类，抽象类
			URLConnection urlConnection = url.openConnection();
			// http的连接类
			HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
			// 设定请求的方法，默认是GET
			httpURLConnection.setRequestMethod("GET");
			// 设置字符编码
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			// 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
			httpURLConnection.connect();

			// 文件大小
			int fileLength = httpURLConnection.getContentLength();
			
			// 获取文件名和扩展名
			// 先连接一次，解决跳转下载
			httpURLConnection.getResponseCode();
			String filePathUrl = httpURLConnection.getURL().getFile();//httpURLConnection.getURL().toString();
			// 第一种方式，针对 img.png
			String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);//File.separatorChar="/"
			String extName = "";
			if (fileFullName.lastIndexOf(".") > 0) {
				extName = fileFullName.substring(fileFullName.lastIndexOf(".") + 1);
			}
			if (extName == "" || extName == null || extName.length() > 4 || extName.indexOf("?") > -1) {
				// 第二种方式，获取header 确定文件名和扩展名
				fileFullName = new String(httpURLConnection.getHeaderField("Content-Disposition").getBytes("iso-8859-1"),"utf-8");
				if (fileFullName == null || fileFullName.indexOf("filename") < 0) {
					fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);//File.separatorChar="/"
					extName = "jpg";
				} else {
					fileFullName = URLDecoder.decode(fileFullName.substring(fileFullName.indexOf("filename") + 10, fileFullName.length() - 1), "UTF-8");
					extName = fileFullName.substring(fileFullName.lastIndexOf(".") + 1);
				}
			}

			System.out.println("file length---->" + fileLength);
			
			System.out.println(new String(httpURLConnection.getHeaderField("x-oss-hash-crc64ecma").getBytes("iso-8859-1"),"utf-8"));

			BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());

			OutputStream out = new FileOutputStream(new File(downloadDir+File.separator+fileFullName));
			int size = 0;
			int len = 0;
			byte[] buf = new byte[1024];
			while ((size = bin.read(buf)) != -1) {
				len += size;
				out.write(buf, 0, size);
				// 打印下载百分比
				System.out.println("下载了-------> " + len * 100 / fileLength +"%\n");
			}
			bin.close();
			out.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return file;
		}
	}
}
