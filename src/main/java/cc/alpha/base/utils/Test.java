package cc.alpha.base.utils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.rpc.ServiceException;

import org.acegisecurity.concurrent.ConcurrentLoginException;
import org.apache.http.client.ClientProtocolException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.tempuri.IParkService;
import org.tempuri.IParkServiceLocator;
import org.tempuri.IParkServiceSoap;

import com.alibaba.fastjson.JSON;
import com.itextpdf.text.DocumentException;
import com.velcro.base.security.service.webService.wsdl_passportClient;
import com.velcro.base.security.service.webService.wsdl_passportPortType;
import com.velcro.base.util.DateHelper;
import com.velcro.base.util.HttpClientUtils;

import alpha.jdbcpool.DBHelper;
import jodd.http.HttpRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;
import wsdl_passport.AdUserMainInfo;
import wsdl_passport.ReturnQueryAdUserPartInfoByStaffNum;



public class Test {

	private static final boolean String = false;
	

	public static void main(String[] args) throws org.apache.http.ParseException, UnsupportedEncodingException, IOException, URISyntaxException, DocumentException{
		
		DBHelper dbhelper = new DBHelper();
		String sql = "select * from t_user limit 5 ";
		List list = dbhelper.executeSqlForList(sql);
		for(int i=0; i<list.size(); i++) {
			Map map = (Map)list.get(i);
			Iterator mit = map.entrySet().iterator();
			while (mit.hasNext()) {
				Map.Entry resultMap =  (Entry) mit.next();
				
				String key = resultMap.getKey().toString();
				String value = resultMap.getValue().toString();
				System.out.println("### i="+i+"; key="+key+"; value="+value);
			}
		}
		
		
		
//		getMonitorurl();
		
//		getAduser();
		
//		Test.execcmd();
		
//		syncMustAttendace();
		
//		getTel();
		
//		sendTWMail();
		
//		flowcost();
		
//		String d = "402881e70ad1d990010ad1e5ec930008,4028b14f17578814011757e2bebf002a,4028a07b1fd83d57011fdad02436109b,4028a07b1d9e609f011dbe06f6633d97";
//		System.out.println(d.substring(33, 65));
//		substringstr();
//		System.out.println(System.getenv());
//		
//		System.out.println(System.getProperties());		
		
//		String curdate = DateHelper.getCurrentDate();
//		System.out.println("2017-01-01".subSequence(0,4));

//		String jsonString = "{\"payinfo\":{\"accname\":\"赵铎18\",\"bank\":\"民生银行\",\"branch\":\"民生银行松江支行3\",\"accnum\":\"6377273472472472833\",\"city\":[\"广东省\",\"从化市\"],\"chg\":\"branch|accnum|city\"},\"type\":\"individual\",\"adminuser\":\"zhaoduo\",\"dzuser\":\"\",\"moneytype\":\"CNY\",\"developtarget\":\"更新外包商\",\"providerno\":\"A0000003313\"}";
//		JSONObject jsonObject = JSONObject.fromObject(jsonString);
//		JSONArray city = jsonObject.getJSONObject("payinfo").getJSONArray("city");//银行所在地
//		System.out.println(jsonObject);
		
//		String s = "https://test.17must.com/rest/terminal/my-image-upload-list.json?companyId=4028a07b45f62f8e0145fa1dc8ff1780&offset=&limit=20";
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("companyId", "4028a07b45f62f8e0145fa1dc8ff1780");
//		map.put("offset", null);
//		map.put("limit", "20");
//		System.out.println(map.size());
//		System.out.println(toCreateUrlSign(map));
		
//		String s = "6ZmI6aG65qC5";
//		System.out.println(Base64.decode(s));
		
//		Pair p = OpenIDUtils.parseOpenId("OC8mL0MxO2Q8NDtoOzs4ZTQ2ZmRlaDM0OGU3NDgzNzQ5aDVmOmY1");
//		System.out.println(p.getLeft());
//		System.out.println(p.getRight());
		
//		SimpleHttpClient hc = new SimpleHttpClient();
//		String url = "http://192.168.124.248:8080/ServiceAction/com.velcro.interfaces.servlet.InterFacesAction";
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("action", "uploadfilenew");
//		param.put("filename", "xiandai.psd");
//		param.put("test", "拉老师讲课费拉就是地方");
//		
//		Map filemap = new HashMap();
//		File file = new File("d:/xiandai.psd");
//		String r = hc.post(url, param, file);
//		System.out.println(r);
		
//		Integer[] ints = new Integer[] { 31, 41, 59, 26, 41, 58 };
//		Integer min = minimum(ints, new Comparator<Integer>() {
//			public int compare(Integer o1, Integer o2) {
//				return o1.intValue() - o2.intValue();
//			}
//		});
//		System.out.println(min);
		
//		String fileurl = "http://dealer_zsl.ztgame.com/upload/contract/2017/10100000042_201712181024061.jpg";
//		String filename = fileurl.substring(fileurl.lastIndexOf("/")+1, fileurl.length());
//		String destfile = "c:/"+filename;
//		File file = new File(destfile);
//		try {
//			FileUtils.copyURLToFile(new URL(fileurl), file);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(file.exists());
//		System.out.println(filename);
		
//		String s = "IWG5cOyX9Pl7OjW5-c2VxX2lkPTkyNzg2NDEwNDM3JnNlbmRfdHlwZT0wJmVtYWlsPXlpemhpcWlhbmd2aXBAMTYzLmNvbSZjcm1JZD1udWxsJmFjY291bnQ9OTI3ODY0MTA0MzcmY29kZWlkPTE1MzY1NDgmc3lzdGVtSWQ9MSZpc1NlbmQ9TiZwaG9uZU51bWJlcj0=";
//		String bs = Base64.decodeToString(s);
//		System.out.println(bs);
		
//		Field[] field = AbstractProject.class.getDeclaredFields();
//		for(int i=0; i<field.length; i++){
//			String name = field[i].getName();
//			System.out.println(name);
//		}
		
		
		
//		http://test.17must.com/api/flow/send.json?content=5oKo5pyJ5p2l6Ieq5piT5b+X5by655qE5rWB56iL6ZyA5aSE55CG&sendNotif=1&title=5beo5Lq65Z+66YeR5rWB56iL5Yqo5oCB&email=liuqiang1@ztgame.com&counts=1&systemId=6&companyUuid=8a2e19595751758601577db771db280d&emercounts=0&flowId=4028a07b4dac5567014db39485ed4505
			
//			
//			5oKo5pyJ5p2l6Ieq5piT5b+X5by655qE5rWB56iL6ZyA5aSE55CG
//			5beo5Lq65Z+66YeR5rWB56iL5Yqo5oCB
		
//        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();  
//        Map map = new HashMap();
//        map.put("m", "mustBalance");
//	    map.put("user_id", "02301");
//        String html = httpProtocolHandler.execute("https://fcard.pt.ztgame.com/must_interface.php", map);
//        System.out.println(html);
		
		
//		String fileName = "C:/Users/yizhiqiang/Desktop/电话费/1802电话费发票.pdf";
//		readPDF(fileName);

		// 下载文件测试
//		FileUtils.downloadFile("http://artimg.17must.com/media/00000000647d2d3401647d403c130012", "D:");
		
		
		
//		System.out.println(System.getenv("COMPUTERNAME"));
//		Map map = System.getenv();
//		Iterator it = map.entrySet().iterator();
//		while(it.hasNext()) {
//			Map.Entry<String, String> en = (Entry<java.lang.String, java.lang.String>) it.next();
//			System.out.println(en.getKey() + " = " + en.getValue());
//		}
		
		
//		Properties props = System.getProperties();
//		Iterator it = props.entrySet().iterator();
//		
//		while(it.hasNext()) {
//			Map.Entry<String, String> en = (Entry<java.lang.String, java.lang.String>) it.next();
//			System.out.println(en.getKey() + " = " + en.getValue());
//		}
		
	}
	
	
	/**
	 * 读PDF文件，使用了pdfbox开源项目
	 * 
	 * @param fileName
	 */
	public static void readPDF(String fileName) {
		File file = new File(fileName);
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			// 新建一个PDF解析器对象
			PDFParser parser = new PDFParser(in);
			// 对PDF文件进行解析
			parser.parse();
			// 获取解析后得到的PDF文档对象
			PDDocument pdfdocument = parser.getPDDocument();
			// 新建一个PDF文本剥离器
			PDFTextStripper stripper = new PDFTextStripper();
			// 从PDF文档对象中剥离文本
			String result = stripper.getText(pdfdocument);
			System.out.println("PDF文件的文本内容如下：");
			System.out.println(result);

		} catch (Exception e) {
			System.out.println("读取PDF文件" + file.getAbsolutePath() + "失败！" + e);
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
	/**
	 * 从套接字中获得输出流并包装成自动刷新的打印流
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static String socketprint() throws UnknownHostException, IOException{
		String url = "http://192.168.101.35:80/genbatch?actid=2608&pcount=100&expiretime=";
		String host = "192.168.101.35";
		Socket socket = new Socket(host, 80);

		// 从套接字中获得输出流并包装成自动刷新的打印流

		HttpRequest request = HttpRequest.get(url);
		socket.getOutputStream().write(request.toByteArray());

		BufferedInputStream streamReader = new BufferedInputStream(socket.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
		System.out.println(bufferedReader.readLine());
		bufferedReader.close();

		socket.close();
		return bufferedReader.readLine();
	}
	
	
	
	public static <T> T minimum(T[] t, Comparator<? super T> comparator) {
		T min = t[0];
		for (int i = 1; i < t.length; i++)
			if (comparator.compare(min, t[i]) > 0)
				min = t[i];
		return min;
	}

	/**
	 * 将参数以其参数名的字典序升序进行排序
	 * @param paramsMap
	 * @return
	 */
	private static String sortedParams(Map paramsMap){
		Map<String, String[]> sortedParams = new TreeMap<String, String[]>(paramsMap);
		Set<Entry<String, String[]>> entrys = sortedParams.entrySet();
		StringBuffer dp = new StringBuffer();
		String urlSignature = "";
		boolean firstFlag = true;
		for (Entry<String, String[]> param : entrys) {
			String key = param.getKey();
			if (key.compareToIgnoreCase("urlSignature") == 0) {
				String[] values = param.getValue();
				for (String str : values) {
					urlSignature = urlSignature + str;
				}
			} else {
				if (!firstFlag) {
					dp.append("&");
				}
				dp.append(key + "=");
				StringBuffer buf = new StringBuffer();
				String[] values = param.getValue();
				for (String str : values) {
					buf.append(str);
				}
				firstFlag = false;
				dp.append(buf.toString());
			}
		}

		String serverUrlSignature = strMD5(dp.toString());
		return serverUrlSignature;
	}
	
	private static String toCreateUrlSign(Map<String, String> paramsMap){
		Map<String, String> sortedParams = new TreeMap<String, String>(
				paramsMap);
		Set<Entry<String, String>> entrys = sortedParams.entrySet();
		StringBuffer dp = new StringBuffer();
		boolean firstFlag = true;
		for (Entry<String, String> param : entrys) {
			String key = param.getKey();
			if (!firstFlag) {
				dp.append("&");
			}
			dp.append(key + "=");
			StringBuffer buf = new StringBuffer();
			String values = param.getValue();
			buf.append(values);
			firstFlag = false;
			dp.append(buf.toString());
		}
		return strMD5(dp.toString());
	}
	
	/**
	 * 将字符串Md5 加密并转换成32位小写
	 * 
	 * @ReqParam s
	 * @return
	 */
	public static final String strMD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private static Map<String, Object> getJSON(String jsonString) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			map.put("code", "SUCCESS");
			map.put("type", "jsonObject");
			map.put("data", jsonObject);
		}catch(Exception e){
			try{
				JSONArray jsonArray = JSONArray.fromObject(jsonString);
				map.put("code", "SUCCESS");
				map.put("type", "jsonArray");
				map.put("data", jsonArray);
			}catch(Exception e1){
				map.put("code", "ERROR");
				map.put("message", "不是json格式数据：String="+jsonString);
			}
		}
		return map;
	}
	
	public static void substringstr(){
		String str = "[dksjdljlakjsdf,alksdjlfajsdf,alsjd;fka]";
		if(str.startsWith("[")){
			str = str.substring(1);
		}
		System.out.println(str);
		if(str.endsWith("]")){
			str = str.substring(0,str.length()-1);
		}
		System.out.println(str);
	}
	
	
	
	/**
	 * 充值webservice调用
	 * @return
	 */
	public static String flowcost(){
		String s = "";
		IParkService ps = new IParkServiceLocator();
		try {
			IParkServiceSoap pss = ps.getIParkServiceSoap();
			s = pss.TPE_FlowCostByCertCode("", "", "", "", "");
			System.out.println(s);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return s;
	}
	
	/**
	 * 手机号码编程
	 */
	public static void getTel(){
		int[] arr  = new int[]{5,2,4,1,3,6,7,0,9};
		int[] index = new int[]{3,4,0,1,2,5,5,6,8,4,7};
		String tel = "";
		for (int i : index){
			tel += arr[i];
		}
		System.out.println(tel);
	}
	
	/**
	 * 手动调用MUST考勤数据同步
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void syncMustAttendace() { 
		String[] dates = null;
		try {
			dates = DateHelper.getDayBetween("2016-07-27", "2016-07-27", true);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0; i<dates.length; i++){
			System.out.println(dates[i]);
			String url = "http://a.17must.com/rest/attendace/get-attendance-to-amt";
			String r = "";
			Map map = new HashMap();
			map.put("companyId", "a0c217d49b8a40418dd5c5cf1325c710");
			map.put("businessTime", dates[i]);
			try {
				Thread.sleep(2000);
				r = HttpClientUtils.post(url, map);
			} catch (org.apache.http.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(r);
		}
	}
	
	public static String hmacsha1(String data, String key) {
		byte[] byteHMAC = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException ignore) {
		}
		String oauth = new BASE64Encoder().encode(byteHMAC);
		return oauth;
	}
	
	public static void execcmd() {
		Process p = null;
		Runtime rt = Runtime.getRuntime();
		String command = "net time ";
		try {
			p = rt.exec(command);
			// 获取进程的标准输入流
			final InputStream is1 = p.getInputStream();
			// 获取进城的错误流
			final InputStream is2 = p.getErrorStream();
			// 启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
			new Thread() {
				@Override
				public void run() {
					BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
					try {
						String line1 = null;
						while ((line1 = br1.readLine()) != null) {
							if (line1 != null) {
								System.out.println(line1);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is1.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

			new Thread() {
				@Override
				public void run() {
					BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
					try {
						String line2 = null;
						while ((line2 = br2.readLine()) != null) {
							if (line2 != null) {
								System.out.println(line2);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is2.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

			p.waitFor();
			p.destroy();
			System.out.println("我想被打印...");
		} catch (Exception e) {
			try {
				p.getErrorStream().close();
				p.getInputStream().close();
				p.getOutputStream().close();
			} catch (Exception ee) {
			}
		}
	}
	
	
	/**
	 * JAVA调用系统命令
	 */
	public static void testCmd(){
		
		Process process = null;
		try {
			// 执行命令
			Runtime rt = Runtime.getRuntime();
			process = rt.exec("javac ");

			// 取得命令结果的输入流
			InputStream fis = process.getInputStream();
			// 用一个读输出流类去读
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			// 逐行读取输出到控制台
			System.out.println("InputStream");
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			
			
			// 取得命令结果的输出流
			OutputStream os = process.getOutputStream();
			// 用一个读输出流类去读
			BufferedReader brout = null;
			String lineout = null;
			// 逐行读取输出到控制台
			System.out.println("OutputStream");
			while ((lineout = brout.readLine()) != null) {
				System.out.println(lineout);
			}
			
			
			// 取得命令结果的输出流
			InputStream iserr = process.getErrorStream();
			// 用一个读输出流类去读
			BufferedReader brerr = new BufferedReader(new InputStreamReader(iserr));
			String lineerr = null;
			// 逐行读取输出到控制台
			System.out.println("ErrorStream");
			while ((lineerr = brerr.readLine()) != null) {
				System.out.println(lineerr);
			}
		} catch (IOException e) {
			System.out.println("IOException:");
			e.printStackTrace();
		}
		
		int a = process.exitValue();
		System.out.println(a);
	}
	
	/**
	 * 获取广告监控链接
	 */
	public static void getMonitorurl(){
		String monitorurl = "";
		String monitorplatUrl = "counter.ztgame.com";
		String gameurl = "http://www.17must.com";
		String adid = "2782255";
		String tempurl = "";
		if(gameurl.indexOf("?")>0)
			tempurl = gameurl + "&ad=";
		else
			tempurl = gameurl + "?ad=";
		tempurl += adid;
		try {
			tempurl = java.net.URLEncoder.encode(tempurl,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		monitorurl = "http://"+monitorplatUrl+"/ref.php?ref="+adid+"&return_url="+tempurl;
		System.out.println(monitorurl);
	}
	
	/**
	 * 获取AD用户属性
	 */
	public static void getAduser(){
		String objnostr = "12049,11506,11543,11554,11555,12944,12974,12969,12968,13137,13118,13140,13271,13334,13474,13533,14286,15127,15217,02003";
		String[] objnos = objnostr.split(",");
		for(int i=0; i<objnos.length; i++){
			String objno = objnos[i];
			wsdl_passportClient client = new wsdl_passportClient();
			if (client == null) {
				System.out.println("client为空,webService接口有误！");
				throw new ConcurrentLoginException("client为空!");
			}
			wsdl_passportPortType service = client.getwsdl_passportPort();
			ReturnQueryAdUserPartInfoByStaffNum returnQueryAdUserPartInfoByStaffNum = service.queryAdUserPartInfoByStaffNum(objno);
			List<AdUserMainInfo> aduserlist = returnQueryAdUserPartInfoByStaffNum.getUserMainInfo();
			String aduser = JSON.toJSONString(aduserlist).toString();
			System.out.println("工号："+objno+"###"+aduser);
		}
	}
	
	public static void sendExtappMessage(){
		String title = "巴图帐号测试";
		title = java.net.URLEncoder.encode(title);
		String content = "体验版测试";
		content = java.net.URLEncoder.encode(content);
		String url = "http://www.17must.com/rest/extapp/send-message.json?read=0&openIds=Nzw4OC8mL0MxNzM1O2QzOmU3OGk5NWk7aDM0NzhpZDRnZjtpaTQ6O2Y1&title="+title+"&content="+content;
		System.out.println(url);
		String idstr = "368698346607,368933867236,368949532261,368987648506,368987648456,368987487775,368991520592,369046264433,369003747527,369044436166,369068021321,369045706746,369002954335,369045627302,369023565294,369102676501,369125784756,369084144539,369094810994,369126501503";
		String[] ids = idstr.split(",");
		for(int i=0; i<ids.length; i++){
			String id = "369045627302";//ids[i];
			Random r = new Random();
			double temp = r.nextDouble();
			System.out.println(id);
			url = "http://www.kuaidi100.com/query";
			Map paramsMap = new HashMap();
			paramsMap.put("type", "zhongtong");
			paramsMap.put("postid", id);
			paramsMap.put("id", "1");
			paramsMap.put("valicode", "");
			paramsMap.put("temp", java.lang.String.valueOf(temp));
			String request = null;
			try {
				request = HttpClientUtils.get(url, paramsMap);
			} catch (org.apache.http.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			String responseText = Base64.decodeToString(joddresponse.body().trim());
			System.out.println(request);
			try {
				Thread.sleep(11000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void sendTWMail(){
		String url = "http://192.168.82.248/ServiceAction/com.velcro.interfaces.servlet.InterFacesAction";
		Map paramsMap = new HashMap();
		paramsMap.put("action", "sendtwmail");
		paramsMap.put("to", "yizhiqiang@ztgame.com");
		paramsMap.put("subject", "test");
		paramsMap.put("content", "twmail");
		paramsMap.put("type", "T");
		String request = null;
		try {
			request = HttpClientUtils.get(url, paramsMap);
		} catch (org.apache.http.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//			String responseText = Base64.decodeToString(joddresponse.body().trim());
		System.out.println(request);
		try {
			Thread.sleep(11000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

