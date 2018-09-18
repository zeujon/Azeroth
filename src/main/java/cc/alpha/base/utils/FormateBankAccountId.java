package cc.alpha.base.utils;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.velcro.base.util.NumberHelper;

public class FormateBankAccountId {
	List<String> standardBankAccountIdFormat;

	public FormateBankAccountId(String propertiesFileName) throws FileNotFoundException, IOException { // 加载资源文件
		try{
			Properties properties = new Properties();
			properties.load(new FileReader(propertiesFileName));
			this.standardBankAccountIdFormat = new ArrayList<String>();
			int keyNumber = 1;
			String value = null;
			// 读取键值对，键的格式是: formate_1 ,..., formate_10,...,formate_100 等
			while ((value = properties.getProperty("formate_" + keyNumber++)) != null) {
				value = value.trim();
				this.standardBankAccountIdFormat.add(value);
			}
		}catch(Exception e){
			this.standardBankAccountIdFormat = new ArrayList<String>();
			this.standardBankAccountIdFormat.add("###-######-###");
			this.standardBankAccountIdFormat.add("##-####-###-##");
			this.standardBankAccountIdFormat.add("####-#-###-###");
			this.standardBankAccountIdFormat.add("#-########-###");
			this.standardBankAccountIdFormat.add("###-########-#");
			this.standardBankAccountIdFormat.add("### ####");
		}
	}

	public List<String> formate(String orginalBankAccountId) {
		List<String> proceededlBankAccountIds = new ArrayList<String>();
		orginalBankAccountId = orginalBankAccountId.trim();
		for (String formateString : this.standardBankAccountIdFormat) { // 格式字符串中的数字个数,
			int formateStringDigitNum = formateString.replace("-", "").length();
			// 原字符串过长，截取后面部分
			if (orginalBankAccountId.length() > formateStringDigitNum) {
				orginalBankAccountId = orginalBankAccountId.substring(orginalBankAccountId.length() - formateStringDigitNum);
			}
			// 原字符串过短，前面补零
			else if (orginalBankAccountId.length() < formateStringDigitNum) {
				String pre = "";
				for (int i = 0; i < formateStringDigitNum - orginalBankAccountId.length(); i++)
					pre += "0";
				orginalBankAccountId = pre + orginalBankAccountId;
			}
			// 格式化字符串中分隔符位置
			int delimiterPosition = 0;
			String temp = orginalBankAccountId;
			while (-1 != (delimiterPosition = formateString.indexOf('-', delimiterPosition))) {
				// 添加分割符号
				temp = temp.substring(0, delimiterPosition) + '-' + temp.substring(delimiterPosition);
				delimiterPosition++;
			}
			proceededlBankAccountIds.add(temp);
		}
		return proceededlBankAccountIds;
	}
	
	/** * .... * @param str 原字符串 * @param len 不参与格式化的长度 * @return 格式化后字符串 */
	public static String format(String str, int len) {
		if (len <= 0) {
			len = 1;
		}
		String patternStr = "^(.{" + len + "})";
		String headStr = "";
		Pattern pattern = Pattern.compile("^(.{" + len + "}).*$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			int g = matcher.groupCount();
			if (g > 0) {
				headStr = matcher.group(1);
			}
		} else {
			return str;
		}
		return headStr + str.replaceFirst(patternStr, "").replaceAll("", "~").replaceAll("~*$", "");
	}

	/**
	 * * @param args
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		String orginalBankAccountId = "62 25882 1040236 95235";
//		FormateBankAccountId formateTool = new FormateBankAccountId("bankAccountId.properties");
//		for (String proceededBankAccountId : formateTool.formate(orginalBankAccountId)) {
//			System.out.println(proceededBankAccountId);
//		}
		
		String s = NumberHelper.formatBankAccount(orginalBankAccountId);
		System.out.println(s);
		
		Map m = new HashMap();
		Map m2 = new HashMap();
		m2.put("content", "巨人");
		m2.put("counts", 1);
		m.put("message", m2);
		m.put("type", "9");
		System.out.println(m.toString());
	}
}
/**
 * bankAccountId.properties： 
 * formate_1 ###-######-### 
 * formate_2 ##-####-###-##
 * formate_3 ####-#-###-### 
 * formate_4 #-########-### 
 * formate_5 ###-########-#
 */

/**
 * output
 * :000-111111-222
 * :00-1111-112-22
 * :0011-1-111-222
 * :0-00111111-222
 * :000-11111122-2
 */;

