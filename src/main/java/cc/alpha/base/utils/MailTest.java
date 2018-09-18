package cc.alpha.base.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.codec.binary.Base64;

public class MailTest {
	public static void main(String args[]) {
		Socket smtpclient = null;
		DataOutputStream os = null;
		BufferedReader is = null;
		String answer = null;
		try {
			smtpclient = new Socket("mail.ztgame.com", 25);
			is = new BufferedReader(new InputStreamReader(smtpclient.getInputStream()));
			os = new DataOutputStream(smtpclient.getOutputStream());
		} catch (UnknownHostException ue) {
			System.err.println("未知主机");
		} catch (IOException io) {
			System.err.println("I/O错误");
		}
		try {
			System.out.println("正在登录邮箱服务器....");
			os.writeBytes("EHLO localhost\r\n");
			while ((answer = is.readLine()) != null) {
				System.out.println("Server:" + answer);
				if (answer.indexOf("220") != -1) {
					break;
				}
			}
			System.out.println("正在检测邮箱支持的服务....");
			os.writeBytes("AUTH LOGIN\r\n");
			while ((answer = is.readLine()) != null) {
				if (answer.indexOf("250") == -1) {
					break;
				}
				System.out.println("Server:" + answer);
			}
			System.out.println("正在验证用户名和密码....");
			System.out.println("Server:" + answer);
			os.writeBytes(Base64.decodeBase64("must@ztgame.com") + "\r\n");// 用户名的BASE64值
			os.writeBytes(Base64.decodeBase64("A@giant.123456") + "\r\n");// 密码的BASE64值，这里用*代替

			// os.writeBytes("must@17must.com"+"\r\n");//用户名的BASE64值
			// os.writeBytes("zt123@game+"+"\r\n");//密码的BASE64值，这里用*代替

			while ((answer = is.readLine()) != null) {
				System.out.println("Server:" + answer);
				if (answer.indexOf("235") != -1) {
					System.out.println("验证成功");
					break;
				} else if (answer.indexOf("334") == -1) {
					System.out.println("验证失败");
					// os.close();
					// is.close();
					// smtpclient.close();
					// System.exit(0);
				}
			}
			System.out.println("开始发送邮件....");
			os.writeBytes("MAIL From: <MUST@ztgame.com>\r\n");
			os.writeBytes("RCPT To: <yizhiqiang@ztgame.com>\r\n");
			os.writeBytes("RCPT To: <yizhiqiang@ztgame.com>\r\n");
			os.writeBytes("DATA\r\n");
			while ((answer = is.readLine()) != null) {
				if (answer.indexOf("354") != -1) {
					break;
				}
				System.out.println("Server:" + answer);
			}
			System.out.println("Server:" + answer);
			System.out.println("正在发送邮件内容....");
			os.writeBytes("From: \r\n");
			os.writeBytes("To: ,\r\n");
			os.writeBytes("Subject: test mail-21\r\n");
			os.writeBytes("Content-Type: text/html\r\n");
			os.writeBytes("This is a test-21 mail, you don't reply it.\r\n\r\n");
			os.writeBytes("\r\n.\r\n");
			while ((answer = is.readLine()) != null) {
				System.out.println("Server:" + answer);
				if (answer.indexOf("250") != -1) {
					break;
				}
			}
			os.writeBytes("QUIT\r\n");
			while ((answer = is.readLine()) != null) {
				System.out.println("Server:" + answer);
				if (answer.indexOf("221") != -1) {
					System.out.println("邮件发送成功,退出邮箱！");
					break;
				}
			}

			os.close();
			is.close();
			smtpclient.close();
		} catch (UnknownHostException ue) {
			System.err.println("无法连接主机");
		} catch (IOException io) {
			System.err.println("发送I/O错误");
		}
	}
}
