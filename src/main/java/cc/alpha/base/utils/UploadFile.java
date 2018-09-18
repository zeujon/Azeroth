package cc.alpha.base.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.http.client.ClientProtocolException;


public class UploadFile {

	public static void uploadFile(String fileName) {
		try {
			// 换行符
			final String newLine = "\r\n";
			final String boundaryPrefix = "--";
			// 定义数据分隔线
			String BOUNDARY = "========7d4a6d158c9";
			// 服务器的域名
			URL url = new URL("http://192.168.82.248/ServiceAction/com.velcro.interfaces.servlet.InterFacesAction?action=uploadfile");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置为POST情
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求头参数
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// 上传文件
			File file = new File(fileName);
			StringBuilder sb = new StringBuilder();
			sb.append(boundaryPrefix);
			sb.append(BOUNDARY);
			sb.append(newLine);
			// 文件参数,photo参数名可以随意修改
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"" + newLine);
			sb.append("Content-Type:application/octet-stream");
			// 参数头设置完以后需要两个换行，然后才是参数内容
			sb.append(newLine);
			sb.append(newLine);
			// 将参数头的数据写入到输出流中
			out.write(sb.toString().getBytes());
			// 数据输入流,用于读取文件数据
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			byte[] bufferOut = new byte[1024];
			int bytes = 0;
			// 每次读1KB数据,并且将文件数据写入到输出流中
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			// 最后添加换行
			out.write(newLine.getBytes());
			in.close();
			// 定义最后数据分隔线，即--加上BOUNDARY再加上--。
			byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine).getBytes();
			// 写上结尾标识
			out.write(end_data);
			out.flush();
			out.close();
			// 定义BufferedReader输入流来读取URL的响应
			// BufferedReader reader = new BufferedReader(new InputStreamReader(
			// conn.getInputStream()));
			// String line = null;
			// while ((line = reader.readLine()) != null) {
			// System.out.println(line);
			// }
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
	}
	
	public static void main1(String[] args) throws ClientProtocolException, IOException {
		String url = "http://192.168.82.248/ServiceAction/com.velcro.interfaces.servlet.InterFacesAction?action=uploadfile";
		// String url =
		// ProPertyUtils.getAmtAttendanceHost()+"/ServiceAction/com.velcro.interfaces.servlet.InterFacesAction?action=uploadfile";
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		File f = new File("../doc/attendacneData/" );
		// FileInputStream fi = new FileInputStream(f);
		// InputStreamRequestEntity fr = new InputStreamRequestEntity(fi);
		// method.setRequestEntity((RequestEntity) fr);
		// method.setRequestHeader("Content-Type", "multipart/form-data");
		// method.setRequestHeader("Content-Disposition",
		// "form-data;name=\"file\";filename=\"" + fileName + "\"" );
		// client.executeMethod(method);
		// method.releaseConnection();
		FilePart filePart = new FilePart("file", f.getName(), f, new MimetypesFileTypeMap().getContentType(f), "UTF-8");
		Part[] parts = new Part[1];
		parts[parts.length - 1] = filePart;
		filePart.setTransferEncoding("binary");
		method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
		client.executeMethod(method);

	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		String url = "http://download.ztgame.com:8088/j_acegi_security_check";
		

	}
}
