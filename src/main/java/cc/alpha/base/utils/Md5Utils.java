package cc.alpha.base.utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.util.encoders.Base64;

/**
 * MD5摘要工具类
 * 
 * @author xiaoding
 *
 */
public class Md5Utils {
  private Md5Utils() {
  }

  public static String md5ToBas64(String data) {
    MessageDigest md = null;
    String str = null;
    try {
      md = MessageDigest.getInstance("MD5");
      str = new String(Base64.encode(md.digest(data.getBytes("UTF-8"))), "UTF-8");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return str;
  }

  public static String md5ToHexStr(String data) {
    MessageDigest md = null;
    String str = null;
    try {
      md = MessageDigest.getInstance("MD5");
      str = Hex.encodeHexString(md.digest(data.getBytes("UTF-8")));//toHexString(md.digest(data.getBytes("UTF-8")));
      
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return str;
  }

}
