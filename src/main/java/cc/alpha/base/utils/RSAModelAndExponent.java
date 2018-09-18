package cc.alpha.base.utils;

public class RSAModelAndExponent {

  public static void main(String[] args) throws Exception {
    String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJJX7jDIXCyuEx+f0kS8CkrRsxOslH5rEP1+/6z06nh+k5+z9jFMRm/wsurcCRxDCbySq+91pALLEchu0V7j8SECAwEAAQ==";
    String[] mps = RSACoder.readModulusAndPublicExponent(publicKey);
    System.out.println("model:"+mps[0]);
    System.out.println("publicExponent:"+mps[1]);
    
    String data = "11111111111111111111111111";
//    System.out.print(Base64.toBase64String(DesUtil.encrypt(data.getBytes(), data.getBytes())));
  }
}
