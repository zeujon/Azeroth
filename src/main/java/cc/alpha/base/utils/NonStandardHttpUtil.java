package cc.alpha.base.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jodd.http.HttpRequest;

public class NonStandardHttpUtil {

	/**
	 * 从套接字中获得输出流并包装成自动刷新的打印流
	 * @param host
	 * @param port
	 * @param url
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static String socketRequest(String host, int port, String url) throws UnknownHostException, IOException {
		
		final Log logger = LogFactory.getLog(NonStandardHttpUtil.class);
		
		// 从套接字中获得输出流并包装成自动刷新的打印流
		Socket socket = new Socket(host, port);
		
		HttpRequest request = HttpRequest.get("http://" + host + ":" + port + "/" + url).queryEncoding("utf-8");
		socket.getOutputStream().write(request.toByteArray());

		BufferedInputStream streamReader = new BufferedInputStream(socket.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
		
		String line = bufferedReader.readLine();
		while (line != null) {
			logger.debug(line);
		}
		bufferedReader.close();

		socket.close();
		return line;

	}
	
	public static void main(String[] args) throws IOException {
		String host = "192.168.101.35";
		int port = 80;
		String url = "genbatch";
		
		String s = socketRequest(host, port, url);
		System.out.println(s);

	}
	
	
}
