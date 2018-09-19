package cc.alpha.base.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public abstract class SupTest {

	/**
	 * 渠道号
	 */
	public static final String channelId = "";

	/**
	 * 请求地址
	 */
	public static final String uri = "";
	/**
	 * 公钥
	 */
	public static final String publicKey = "";

	/**
	 * 风险控制系统验证
	 * 
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	protected static String checkRiskSystem(String xml) throws UnsupportedEncodingException, Exception {
		String mkey = UUID.randomUUID().toString();

		// 加密报文体格式：BASE64(商户号)| BASE64(RSA(报文加密密钥))| BASE64(3DES(报文原文))
		String strKey = RSACoder.encryptByPublicKey(new String(mkey.getBytes(), "utf-8"), publicKey);

		String strxml = new String(Base64Coder.encode(DesUtil.encrypt(xml.toString().getBytes("utf-8"), mkey.getBytes())));

		String returnXml = new String(Base64Coder.encode(channelId.getBytes("utf-8"))) + "|" + strKey + "|" + strxml;
		System.out.println(returnXml);
		System.out.println();
		System.out.println();
		long start = System.currentTimeMillis();
		System.out.println("T1=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
		String reutrnResult = HttpUtil.sendXMLDataByPost(uri, returnXml);
		System.out.println("T2=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()) + ",time length:" + (System.currentTimeMillis() - start));
		System.out.println(reutrnResult);
		String xmlArr[] = reutrnResult.split("\\|");

		if (xmlArr[0].equals("0")) {
			System.out.println(new String(Base64Coder.decode(xmlArr[2]), "utf-8"));
		} else {
			byte[] b = DesUtil.decrypt(Base64Coder.decode(xmlArr[1]), mkey.getBytes());

			String tradeXml = new String(b, "utf-8");
			System.out.println(tradeXml);
			System.out.println(new String(Base64Coder.encode(Md5Utils.md5ToHexStr(tradeXml).getBytes("utf-8"))));// BASE64(MD5(报文))
			return tradeXml;
		}
		return null;
	}

	public static String readRespMsg(String xml) {
		if (StringUtils.isEmpty(xml)) {
			return "未知";
		}
		try {
			Element ele = DocumentHelper.parseText(xml).getRootElement();
			return ele.elementText("respDesc");
		} catch (DocumentException e) {
			return "未知2";
		}

	}
}
