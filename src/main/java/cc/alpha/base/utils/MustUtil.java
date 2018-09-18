package cc.alpha.base.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

/**
 * @author 乌格德乐 2014-11-25下午6:47:31
 * 
 */
public class MustUtil {
	public static String downloadPage(final String strUrl) {
		try {
			final URL pageUrl = new URL(strUrl);
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(pageUrl.openStream(),"UTF-8"));
			String line;
			final StringBuffer pageBuffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				pageBuffer.append(line);
			}
			System.out.println(pageBuffer.toString());
			return pageBuffer.toString();

		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void main(final String[] args) {
		 final String url =
		 "http://test.17must.com/rest/extapp/send-message.json?";
		 final String str =
		 "openIds=OC8mL0MxNzg3OTM6aTw0NTkzNzo2Zjs3ZmY7PGRnOmZpZjg2ODc1&title=测试test&content=乌格德&pushText=乌格德";
		 System.out.println(str);
		 downloadPage(url + str);

	}

	/**
	 * @param openId
	 *            openId
	 * @param title
	 *            消息标题
	 * @param content
	 *            消息内容
	 */
	public static void sendMustMsg(final String openId, final String title,
			String content) {
		try {
			System.out
					.println("http://musttest.ztgame.com/rest/extapp/send-message.json?read=0&openIds="
							+ openId
							+ "&title="
							+ title
							+ "&content="
							+ content);
			if (StringUtils.isBlank(content)) {
				content = "空内容！";
			}
			downloadPage("http://musttest.ztgame.com/rest/extapp/send-message.json?read=0&openIds="
					+ openId + "&title=" + title + "&content=" + content);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
