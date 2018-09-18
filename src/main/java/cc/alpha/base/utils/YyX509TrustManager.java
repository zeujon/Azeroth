package cc.alpha.base.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 请求认真规则(方法空表示全部认证)
 * 
 * 
 */
public class YyX509TrustManager implements X509TrustManager {

	// 检查客户端证书
	@Override
	public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
	}

	// 检查服务器证书
	@Override
	public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
	}

	// 返回信任的X509证书数组
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}
