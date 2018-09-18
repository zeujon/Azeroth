package cc.alpha.base.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.velcro.base.util.HttpServiceUtil;

/**
 * 
 * @author 简爱微萌
 * @Email zyw205@gmail.com 接口权限中设置OAuth2.0网页授权 域名 如：www.wechat68.com
 *        授权访问的URL：https://open.weixin.qq.com/connect/oauth2/authorize?appid=
 *        wx614c453e0d1dcd12
 *        &redirect_uri=http://www.wechat68.com/Javen/OauthTest
 *        &response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 */
public class Oauth2Servlet extends HttpServlet {
	private String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=APPID" + "&secret=SECRET&" + "code=CODE&grant_type=authorization_code";
	private String get_userinfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	private static final long serialVersionUID = -644518508267758016L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String code = request.getParameter("code");

		get_access_token_url = get_access_token_url.replace("APPID", "wx614c453e0d1dcd12");
		get_access_token_url = get_access_token_url.replace("SECRET", "fd00642f7a2fea32c5a7b060d9c37db1");
		get_access_token_url = get_access_token_url.replace("CODE", code);

//		String json = HttpUtil.getUrl(get_access_token_url);
		HttpServiceUtil s = new HttpServiceUtil();
		String json = null;
		try {
			json = s.send(get_access_token_url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		JSONObject jsonObject = JSONObject.fromObject(json);
		String access_token = jsonObject.getString("access_token");
		String openid = jsonObject.getString("openid");

		get_userinfo = get_userinfo.replace("ACCESS_TOKEN", access_token);
		get_userinfo = get_userinfo.replace("OPENID", openid);
		//TODO
		String userInfoJson = "";//HttpUtil.getUrl(get_userinfo);

		JSONObject userInfoJO = JSONObject.fromObject(userInfoJson);

		String user_openid = userInfoJO.getString("openid");
		String user_nickname = userInfoJO.getString("nickname");
		String user_sex = userInfoJO.getString("sex");
		String user_province = userInfoJO.getString("province");
		String user_city = userInfoJO.getString("city");
		String user_country = userInfoJO.getString("country");
		String user_headimgurl = userInfoJO.getString("headimgurl");

		// UserInfo_weixin userInfo=new UserInfo_weixin(user_openid,
		// user_nickname, user_sex, user_province, user_city, user_country,
		// user_headimgurl);
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println("");
		out.println("");
		out.println("  ");
		out.println("  ");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method \n");
		out.println("openid:" + user_openid + "\n\n");
		out.println("nickname:" + user_nickname + "\n\n");
		out.println("sex:" + user_sex + "\n\n");
		out.println("province:" + user_province + "\n\n");
		out.println("city:" + user_city + "\n\n");
		out.println("country:" + user_country + "\n\n");
		out.println("");
		out.println(">");
		out.println("  ");
		out.println("");
		out.flush();
		out.close();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("");
		out.println("");
		out.println("  ");
		out.println("  ");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  ");
		out.println("");
		out.flush();
		out.close();
	}

}