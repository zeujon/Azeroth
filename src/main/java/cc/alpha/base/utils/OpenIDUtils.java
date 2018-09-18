package cc.alpha.base.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.sun.tools.javac.util.Pair;

public class OpenIDUtils {

	public static String createOpenId(Object userId, Object appId) {
		String o = userId.toString() + ",#,@." + appId.toString();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < o.length(); i ++) {
			char c = (char) (o.charAt(i) + 3);
			sb.append(c);
		}
		String s = encodeStr(sb.toString());
		if(StringUtils.endsWith(s, "==")){
			s = s.substring(0, s.length()-2) + "2";
		}else if(StringUtils.endsWith(s, "=")){
			s = s.substring(0, s.length()-1) + "1";
		}else{
			s = s + "0";
		}
		return s;
	}
	
	public static Pair<Object, Object> parseOpenId(String openId) {
		if(StringUtils.endsWith(openId, "2")){
			openId = openId.substring(0, openId.length()-1) + "==";
		}else if(StringUtils.endsWith(openId, "1")){
			openId = openId.substring(0, openId.length()-1) + "=";
		}else{
			openId = openId.substring(0, openId.length()-1);
		}
		openId = decodeStr(openId);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < openId.length(); i++) {
			char c = (char) (openId.charAt(i) -3);
			sb.append(c);
		}
		String s = sb.toString();
		String[] ss = s.split(",#,@.");
		if (ss.length != 2) throw new IllegalArgumentException();
		return Pair.<Object, Object>of(ss[0], ss[1]);
	}
	
	private static String encodeStr(String plainText) {
		byte[] b = plainText.getBytes();
		Base64 base64 = new Base64();
		b = base64.encode(b);
		String s = new String(b);
		return s;
	}
	
	private static String decodeStr(String encodeStr){
		byte[] b=encodeStr.getBytes();
		Base64 base64=new Base64();
		b=base64.decode(b);
		String s=new String(b);
		return s;
	}
	
}
