package cc.alpha.base.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import bsh.EvalError;
import bsh.Interpreter;

public final class StringHelper {
	private static final char[] CHARS = "abcdehjkmnpqrsuvwxyzhs0123456789ABCDEFGHIJKMLNOPQRSTUVWXYZ".toCharArray();
	private static final String[] SQLCHARS = {"%20insert%20", "%20delete%20", "%20update%20", "%20alter%20", "%20xp_cmdshell%20"};
	private static final String[] SQLCHARS_V = {"%20select%20", "%20and%20", "%20from%20", "'", "%20*%20"};
	private static String[] CH = {"", "", "拾", "佰", "仟"};
	private static String[] CHS_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
	private static String[] CHS = {"万", "亿", "兆"};
	private static DecimalFormat df = new DecimalFormat("#########################0.0#");

	public static String null2String(String strIn) {
		if(strIn == null){
			return "";
		}else if(strIn.equalsIgnoreCase("null")){
			return "";
		}else{
			return strIn;
		}
//		return strIn == null ? "" : strIn;
	}

	/**
	 * 根据长度返回随机数
	 * 
	 * @param length
	 *            随机数长度
	 * @return
	 */
	public static String getRand(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(CHARS[new Random().nextInt(CHARS.length)]);
		}
		return sb.toString();
	}

	public static String[] split(String srcString, String regex) throws Exception {
		if (!(regex != null && regex.length() > 0)) {
			throw new Exception("请输入分隔符！");
		}
		StringTokenizer stk = null;
		if (srcString.length() == 0) {
			return new String[]{srcString};
		} else {
			if (srcString.lastIndexOf(regex) > 0) {
				if (srcString.lastIndexOf(regex) == (srcString.length() - 1)) {
					stk = new StringTokenizer(srcString + " ", regex);
				} else {
					stk = new StringTokenizer(srcString, regex);
				}
			} else {
				return new String[]{srcString};
			}
		}
		String[] array = new String[stk.countTokens()];
		int index = 0;
		while (stk.hasMoreTokens()) {
			array[index++] = stk.nextToken();
		}
		if (srcString.lastIndexOf(regex) > 0) {
			if (srcString.lastIndexOf(regex) == (srcString.length() - 1)) {
				array[array.length - 1] = "";
			}
		}
		try {
			return array;
		} finally {
			stk = null;
			array = null;
		}
	}

	/**
	 * 格式化字符串，
	 * 
	 * @param temp
	 *            要格式化的原字符串
	 * @param format
	 *            格式代码 如#,##0.00
	 * @return
	 */
	public static String format(String temp, String format) {
		if (StringHelper.isEmpty(temp) || StringHelper.isEmpty(format)) {
			return "";
		}
		return new java.text.DecimalFormat(format).format(Double.parseDouble(temp)).replace(",", ".");
	}

	/**
	 * 格式化字符串，产生10位流水号 00.000.000
	 * 
	 * @param temp
	 *            要格式化的原字符串
	 * @return
	 */
	public static String format(String temp) {
		if (StringHelper.isEmpty(temp)) {
			return "";
		} else if (temp.length() == 8) {
			return temp.substring(0, 2) + "." + temp.substring(2, 5) + "." + temp.substring(5, 8);
		} else {
			return temp;
		}
	}

	public static String null2String(Object strIn) {
		return strIn == null ? "" : "" + strIn;
	}

	public static String null2String(Object strIn, String defstr) {
		return strIn == null ? defstr : "" + strIn;
	}

	public static String GBK2ISO(String s) {
		try {
			return new String(s.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}

	public static String ISO2GBK(String s) {
		try {
			return new String(s.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}

	public static ArrayList string2ArrayList2(String strIn, String strDim) {
		strIn = null2String(strIn);
		strDim = null2String(strDim);
		ArrayList strList = new ArrayList();

		String[] sr2 = strIn.split(strDim);
		for (int i = 0; i < sr2.length; i++) {
			strList.add(null2String(sr2[i]));
		}
		if (strIn.endsWith(strDim))
			strList.add("");

		return strList;
	}

	public static ArrayList string2ArrayList(String strIn, String strDim) {
		return string2ArrayList(strIn, strDim, false);
	}

	public static ArrayList string2ArrayList(String strIn, String strDim, boolean bReturndim) {
		strIn = null2String(strIn);
		strDim = null2String(strDim);
		ArrayList strList = new ArrayList();
		StringTokenizer strtoken = new StringTokenizer(strIn, strDim, bReturndim);
		while (strtoken.hasMoreTokens()) {
			strList.add(strtoken.nextToken());
		}
		return strList;
	}

	public static String[] string2Array(String strIn, String strDim) {
		return string2Array(strIn, strDim, false);
	}

	public static String[] string2Array(String strIn, String strDim, boolean bReturndim) {
		ArrayList strlist = string2ArrayList(strIn, strDim, bReturndim);
		int strcount = strlist.size();
		String[] strarray = new String[strcount];
		for (int i = 0; i < strcount; i++) {
			strarray[i] = (String) strlist.get(i);
		}
		return strarray;
	}

	public static boolean contains(Object a[], Object s) {
		if (a == null || s == null) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i] != null && a[i].equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static String replaceChar(String s, char c1, char c2) {
		if (s == null) {
			return s;
		}

		char buf[] = s.toCharArray();
		for (int i = 0; i < buf.length; i++) {
			if (buf[i] == c1) {
				buf[i] = c2;
			}
		}
		return String.valueOf(buf);
	}
	
	/**
	 * 去除字符串中的空格、回车、换行符、制表符
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String afterStr = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			afterStr = m.replaceAll(""); 
		}
		return afterStr;
	}
	
	
	/**
	 * 过滤特殊字符
	 * 只允许字母和数字
	 * String regEx = "[^a-zA-Z0-9]";
	 * 清除掉所有特殊字符
	 * @param str
	 * @return
	 */
	public static String StringFilter(String str) {
		String regEx="[`~!@#$%^&*()--+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	
	
	public static String replaceString(String strSource, String strFrom, String strTo) {
		if (strSource == null) {
			return null;
		}
		int i = 0;
		if ((i = strSource.indexOf(strFrom, i)) >= 0) {
			char[] cSrc = strSource.toCharArray();
			char[] cTo = strTo.toCharArray();
			int len = strFrom.length();
			StringBuffer buf = new StringBuffer(cSrc.length);
			buf.append(cSrc, 0, i).append(cTo);
			i += len;
			int j = i;
			while ((i = strSource.indexOf(strFrom, i)) > 0) {
				buf.append(cSrc, j, i - j).append(cTo);
				i += len;
				j = i;
			}
			buf.append(cSrc, j, cSrc.length - j);
			return buf.toString();
		}
		return strSource;
	}

	public static String replaceStringFirst(String sou, String s1, String s2) {
		int idx = sou.indexOf(s1);
		if (idx < 0) {
			return sou;
		}
		return sou.substring(0, idx) + s2 + sou.substring(idx + s1.length());
	}

	public static String replaceRange(String sentence, String oStart, String oEnd, String rWord, boolean matchCase) {
		int sIndex = -1;
		int eIndex = -1;
		if (matchCase) {
			sIndex = sentence.indexOf(oStart);
		} else {
			sIndex = sentence.toLowerCase().indexOf(oStart.toLowerCase());
		}
		if (sIndex == -1 || sentence == null || oStart == null || oEnd == null || rWord == null) {
			return sentence;
		} else {
			if (matchCase) {
				eIndex = sentence.indexOf(oEnd, sIndex);
			} else {
				eIndex = sentence.toLowerCase().indexOf(oEnd.toLowerCase(), sIndex);
			}
			String newStr = null;
			if (eIndex > -1) {
				newStr = sentence.substring(0, sIndex) + rWord + sentence.substring(eIndex + oEnd.length());
			} else {
				newStr = sentence.substring(0, sIndex) + rWord + sentence.substring(sIndex + oStart.length());
			}
			return replaceRange(newStr, oStart, oEnd, rWord, matchCase);
		}
	}

	// add from org.apache.commons.lang.StringUtils by steven.yu
	// Empty checks
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if a String is empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 *  StringUtils.isEmpty(null)      = true
	 *  StringUtils.isEmpty(&quot;&quot;)        = true
	 *  StringUtils.isEmpty(&quot; &quot;)       = false
	 *  StringUtils.isEmpty(&quot;bob&quot;)     = false
	 *  StringUtils.isEmpty(&quot;  bob  &quot;) = false
	 * </pre>
	 * 
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the
	 * String. That functionality is available in isBlank().
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is empty or null
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.equalsIgnoreCase("null") || str.length() == 0;
	}

	public static boolean isID(String str) {
		return !isEmpty(str) && str.length() == 32;
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String
	 * returning <code>null</code> if the String is empty ("") after the trim or
	 * if it is <code>null</code>.
	 * 
	 * <p>
	 * The String is trimmed using {@link String#trim()}. Trim removes start and
	 * end characters &lt;= 32. To strip whitespace use
	 * {@link #stripToNull(String)}.
	 * </p>
	 * 
	 * <pre>
	 *  StringUtils.trimToNull(null)          = null
	 *  StringUtils.trimToNull(&quot;&quot;)            = null
	 *  StringUtils.trimToNull(&quot;     &quot;)       = null
	 *  StringUtils.trimToNull(&quot;abc&quot;)         = &quot;abc&quot;
	 *  StringUtils.trimToNull(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to be trimmed, may be null
	 * @return the trimmed String, <code>null</code> if only chars &lt;= 32,
	 *         empty or null String input
	 * @since 2.0
	 */
	public static String trimToNull(String str) {
		String ts = trim(str);
		return isEmpty(ts) ? null : ts;
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String,
	 * handling <code>null</code> by returning <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * The String is trimmed using {@link String#trim()}. Trim removes start and
	 * end characters &lt;= 32. To strip whitespace use {@link #strip(String)}.
	 * </p>
	 * 
	 * <p>
	 * To trim your choice of characters, use the {@link #strip(String, String)}
	 * methods.
	 * </p>
	 * 
	 * <pre>
	 *  StringUtils.trim(null)          = null
	 *  StringUtils.trim(&quot;&quot;)            = &quot;&quot;
	 *  StringUtils.trim(&quot;     &quot;)       = &quot;&quot;
	 *  StringUtils.trim(&quot;abc&quot;)         = &quot;abc&quot;
	 *  StringUtils.trim(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to be trimmed, may be null
	 * @return the trimmed string, <code>null</code> if null String input
	 */
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	public static boolean parseBoolean(String param) {
		if (isEmpty(param)) {
			return false;
		}
		switch (param.charAt(0)) {
			case '1' :
			case 'y' :
			case 'Y' :
			case 't' :
			case 'T' :
				return true;
		}
		return false;
	}

	public static String getRandomStr(int length) {
		String psd = "";
		char c;
		int i;
		int isnum = 0;
		for (int j = 0; j < length; j++) {
			if (isnum == 0) {
				isnum = 1;
				c = (char) (Math.random() * 26 + 'a');
				psd += c;
			} else {
				isnum = 0;
				c = (char) (Math.random() * 10 + '0');
				psd += c;
			}
		}
		return psd;
	}

	public static String fromDB(String s) {
		char c[] = s.toCharArray();
		char ch;
		int i = 0;
		StringBuffer buf = new StringBuffer();

		while (i < c.length) {
			ch = c[i++];
			if (ch == '\"') {
				buf.append("\\\"");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	public static String Array2String(String[] strIn, String strDim) {
		StringBuffer strOut = new StringBuffer();
		for (String tempStr : strIn) {
			strOut.append(tempStr).append(strDim);
		}
		if (strOut.length() > 0)
			strOut.delete(strOut.lastIndexOf(strDim), strOut.length());
		return strOut.toString();
	}

	public static String Array2String(Object[] strIn, String strDim) {

		StringBuffer strOut = new StringBuffer();
		for (Object tempStr : strIn) {
			strOut.append(tempStr).append(strDim);
		}
		if (strOut.length() > 0)
			strOut.delete(strOut.lastIndexOf(strDim), strOut.length());
		return strOut.toString();
	}

	public static String ArrayList2String(ArrayList strIn, String strDim) {
		StringBuffer strOut = new StringBuffer();
		for (Object o : strIn) {
			strOut.append(o.toString()).append(strDim);
		}
		if (strOut.length() > 0)
			strOut.delete(strOut.lastIndexOf(strDim), strOut.length());
		return strOut.toString();
	}

	public static String fillValuesToString(String str, Hashtable ht) {
		return fillValuesToString(str, ht, '$');
	}

	public static String fillValuesToString(String str, Hashtable ht, char VARIABLE_PREFIX) {

		char TERMINATOR = '\\';

		if (str == null || str.length() == 0 || ht == null) {
			return str;
		}

		char s[] = str.toCharArray();
		char ch, i = 0;
		String vname;
		StringBuffer buf = new StringBuffer();

		ch = s[i];
		while (true) {
			if (ch == VARIABLE_PREFIX) {
				vname = "";
				if (++i < s.length) {
					ch = s[i];
				} else {
					break;
				}
				while (true) {
					if (ch != '_' && ch != '-' && !Character.isLetterOrDigit(ch)) {
						break;
					}
					vname += ch;
					if (++i < s.length) {
						ch = s[i];
					} else {
						break;
					}
				}

				if (vname.length() != 0) {
					String vval = (String) ht.get(vname);
					if (vval != null) {
						buf.append(vval);
					}
				}
				if (vname.length() != 0 && ch == VARIABLE_PREFIX) {
					continue;
				}
				if (ch == TERMINATOR) {
					if (++i < s.length) {
						ch = s[i];
					} else {
						break;
					}
					continue;
				}
				if (i >= s.length) {
					break;
				}
			}

			buf.append(ch);
			if (++i < s.length) {
				ch = s[i];
			} else {
				break;
			}
		}
		return buf.toString();
	}

	public static String formatMutiIDs(String ids) {
		StringBuffer ret = new StringBuffer("''");
		ArrayList arrayids = string2ArrayList(ids, ",");
		for (int i = 0; i < arrayids.size(); i++) {
			String _id = null2String(String.valueOf(arrayids.get(i))).trim();
			if (!StringHelper.isEmpty(_id)) {
				ret.append(",'").append(_id).append("'");
			}
		}
		String returnstr = ret.toString();
		int tindex = returnstr.indexOf(",");
		if (tindex > 0) {
			return returnstr.substring(tindex + 1);
		}
		return returnstr;
	}

	public static String lift(String arg, int length) {
		int sLength = arg.trim().length();
		if (length < 1 || length > sLength) {
			return arg.trim();
		} else {
			return arg.trim().substring(0, length);
		}

	}

	public static String getDecodeStr(String strIn) {
		if (strIn == null)
			return "";
		String strTemp = "";
		for (int i = 0; i < strIn.length(); i++) {
			char charTemp = strIn.charAt(i);
			switch (charTemp) {
				case 124 : // '~'
					String strTemp2 = strIn.substring(i + 1, i + 3);
					strTemp = strTemp + (char) Integer.parseInt(strTemp2, 16);
					i += 2;
					break;

				case 94 : // '^'
					String strTemp3 = strIn.substring(i + 1, i + 5);
					strTemp = strTemp + (char) Integer.parseInt(strTemp3, 16);
					i += 4;
					break;

				default :
					strTemp = strTemp + charTemp;
					break;
			}
		}

		return strTemp;
	}

	public static String getEncodeStr(String strIn) {
		if (strIn == null)
			return "";
		String strOut = "";
		for (int i = 0; i < strIn.length(); i++) {
			int iTemp = strIn.charAt(i);
			if (iTemp > 255) {
				String strTemp2 = Integer.toString(iTemp, 16);
				for (int iTemp2 = strTemp2.length(); iTemp2 < 4; iTemp2++)
					strTemp2 = "0" + strTemp2;

				strOut = strOut + "^" + strTemp2;
			} else {
				if (iTemp < 48 || iTemp > 57 && iTemp < 65 || iTemp > 90 && iTemp < 97 || iTemp > 122) {
					String strTemp2 = Integer.toString(iTemp, 16);
					for (int iTemp2 = strTemp2.length(); iTemp2 < 2; iTemp2++)
						strTemp2 = "0" + strTemp2;

					strOut = strOut + "|" + strTemp2;
				} else {
					strOut = strOut + strIn.charAt(i);
				}
			}
		}

		return strOut;
	}

	public static String getMoneyStr(String src) {
		return Money.getChnmoney(getfloatToString2(src));
	}

	public static String getfloatToString(String value) {
		int index = value.indexOf("E");
		if (index == -1) {
			return value;
		}

		int num = Integer.parseInt(value.substring(index + 1, value.length()));
		value = value.substring(0, index);
		index = value.indexOf(".");
		value = value.substring(0, index) + value.substring(index + 1, value.length());
		String number = value;
		if (value.length() <= num) {
			for (int i = 0; i < num - value.length(); i++) {
				number += "0";
			}
		} else {
			number = number.substring(0, num + 1) + "." + number.substring(num + 1) + "0";
		}
		return number;
	}

	public static String getfloatToString2(String value) { // 保留两位小数
		value = getfloatToString(value);
		int index = value.indexOf(".");
		if (index == -1)
			return value;
		String value1 = value.substring(0, index);
		String value2 = value.substring(index + 1, value.length()) + "00";
		if (Integer.parseInt(value2.substring(0, 2)) == 0)
			return value1;
		else
			return value1 + "." + value2.substring(0, 2);
	}

	public static String numberFormat2(String value) { // 保留两位小数
		double num = NumberHelper.string2Double(StringHelper.null2String(value), 0.0);
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(num);
	}

	public static String numberFormat2(Double value) { // 保留两位小数
		double num = NumberHelper.string2Double(StringHelper.null2String(value), 0.0);
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(num);
	}

	public static String numberFormat2(double value) { // 保留两位小数
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(value);
	}

	public static String getRequestHost(HttpServletRequest request) {
		if (request == null) {
			return "";
		} else {
			return (null2String(request.getHeader("Host"))).trim();
		}
	}

	// 以 java 实现 escape unescape
	private final static String[] hex = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"1A", "1B", "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31", "32", "33", "34", "35", "36", "37", "38",
			"39", "3A", "3B", "3C", "3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54", "55", "56", "57",
			"58", "59", "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73", "74", "75", "76",
			"77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95",
			"96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F", "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4",
			"B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3",
			"D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2",
			"F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF"};
	private final static byte[] val = {0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
			0x09, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F};

	public static String escape(String s) {
		if (isEmpty(s))
			return "";
		StringBuffer sbuf = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; i++) {
			int ch = s.charAt(i);
			if (ch == ' ') { // space : map to '+'
				sbuf.append('+');
			} else if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' : as it was
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') { // 'a'..'z' : as it was
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') { // '0'..'9' : as it was
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' // unreserved : as it was
					|| ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '\'' || ch == '(' || ch == ')') {
				sbuf.append((char) ch);
			} else if (ch <= 0x007F) { // other ASCII : map to %XX
				sbuf.append('%');
				sbuf.append(hex[ch]);
			} else { // unicode : map to %uXXXX
				sbuf.append('%');
				sbuf.append('u');
				sbuf.append(hex[(ch >>> 8)]);
				sbuf.append(hex[(0x00FF & ch)]);
			}
		}
		return sbuf.toString();
	}

	public static String unescape(String s) {
		if (isEmpty(s))
			return "";
		StringBuffer sbuf = new StringBuffer();
		int i = 0;
		int len = s.length();
		while (i < len) {
			int ch = s.charAt(i);
			if (ch == '+') { // + : map to ' '
				sbuf.append(' ');
			} else if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' : as it was
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') { // 'a'..'z' : as it was
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') { // '0'..'9' : as it was
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' // unreserved : as it was
					|| ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '\'' || ch == '(' || ch == ')') {
				sbuf.append((char) ch);
			} else if (ch == '%') {
				int cint = 0;
				if ('u' != s.charAt(i + 1)) { // %XX : map to ascii(XX)
					cint = (cint << 4) | val[s.charAt(i + 1)];
					cint = (cint << 4) | val[s.charAt(i + 2)];
					i += 2;
				} else { // %uXXXX : map to unicode(XXXX)
					cint = (cint << 4) | val[s.charAt(i + 2)];
					cint = (cint << 4) | val[s.charAt(i + 3)];
					cint = (cint << 4) | val[s.charAt(i + 4)];
					cint = (cint << 4) | val[s.charAt(i + 5)];
					i += 5;
				}
				sbuf.append((char) cint);
			}
			i++;
		}
		return sbuf.toString();
	}

	/**
	 * 转换第一个字母为大写
	 * */
	public static String changeFirstLetter(String string) {
		if (string == null) {
			return null;
		} else {
			String c = string.substring(0, 1);
			String d = c.toUpperCase();
			String returnString = string.replaceFirst(c, d);
			return returnString;
		}

	}

	/**
	 * Converts some important chars (int) to the corresponding html string
	 */
	public static String conv2Html(int i) {
		if (i == '&')
			return "&amp;";
		else if (i == '<')
			return "&lt;";
		else if (i == '>')
			return "&gt;";
		else if (i == '"')
			return "&quot;";
		else if (i == '\'')
			return "&#39;";
		else
			return "" + (char) i;
	}

	/**
	 * Converts a normal string to a html conform string
	 */
	public static String conv2Html(String st) {
		if (StringHelper.isEmpty(st)) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < st.length(); i++) {
			buf.append(conv2Html(st.charAt(i)));
		}
		return buf.toString();
	}

	/**
	 * 移除开头，结尾，或中间的逗号
	 * 
	 * @param oldstationids
	 * @return
	 */
	public static String removeComma(String oldstationids) {
		if (!isEmpty(oldstationids)) {

			oldstationids = oldstationids.replaceAll(",+", ",");

			if (oldstationids.lastIndexOf(",") == oldstationids.length() - 1) {
				oldstationids = oldstationids.substring(0, oldstationids.lastIndexOf(","));
			}
			if (oldstationids.indexOf(",") == 0) {
				oldstationids = oldstationids.substring(1);
			}
		}
		return oldstationids;
	}

	/**
	 * 生成令牌
	 * 
	 * @param request
	 * @return
	 */
	public static String generateToken(HttpServletRequest request) {
		HttpSession session = request.getSession();
		try {
			byte id[] = session.getId().getBytes();
			byte now[] = new Long(System.currentTimeMillis()).toString().getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(id);
			md.update(now);
			return (md.digest().toString());
		} catch (Exception e) {
			return (null);
		}
	}

	/**
	 * 将string中的回车、换行、空格转化为html代码
	 * 
	 * @param sStr
	 * @return
	 */
	public static String convertHtmlString(String sStr) {
		if (isEmpty(sStr)) {
			return "";
		}

		StringBuffer sTmp = new StringBuffer();
		int i = 0;
		while (i <= sStr.length() - 1) {
			// if (sStr.charAt(i) == '\n' || sStr.charAt(i) == '\r') {
			if (sStr.charAt(i) == '\n') {
				sTmp = sTmp.append("<br>");
			} else if (sStr.charAt(i) == ' ') {
				sTmp = sTmp.append(" ");
			} else {
				sTmp = sTmp.append(sStr.substring(i, i + 1));
			}
			i++;
		}
		return sTmp.toString();
	}

	// 打印信息
	public static void p(String printname, String printvalue) {
		System.out.println(StringHelper.null2String(printname) + "=======" + StringHelper.null2String(printvalue));
	}

	public static void p(String msg) {
		System.out.println(StringHelper.null2String(msg));
	}

	/**
	 * 实现JS中的eval功能
	 * 
	 * @param str
	 * @return
	 */
	public static boolean eval(String str) {
		boolean bl = false;
		if (str.indexOf("==") != -1) {
			String[] cons = str.split("==");
			if (cons[0].equals(cons[1])) {
				bl = true;
			}
		} else {
			Interpreter interpreter = new Interpreter();
			try {
				interpreter.set("boolean", interpreter.eval(str));
				bl = Boolean.parseBoolean(interpreter.get("boolean").toString());
			} catch (EvalError e) {
				e.printStackTrace();
			}
		}
		return bl;
	}

	public static String getRandString(int length) {
		char[] CHARS = "abcdehjkmnpqrsuvwxyzhs0123456789ABCDEFGHIJKMLNOPQRSTUVWXYZ".toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(CHARS[new Random().nextInt(CHARS.length)]);
		}
		return sb.toString();
	}

	/**
	 * @param str
	 * @return 返回格式化的输入sql的字符，防止sql注入攻击。
	 */
	public static String FormatInputString(String str) {
		String strReturn = str;
		String word = "and|exec|insert|select|delete|update|chr|mid|master|or|truncate|char|drop|create|declare|join|'|<|>|--|Or|OR|oR";
		String[] keys = word.split("|");
		for (int i = 0; i < keys.length; i++) {
			if (str.indexOf(keys[i].toString()) > -1) {
				strReturn = strReturn.replace(keys[i].toString(), "");
			}
		}
		return strReturn;
	}

	/**
	 * 截取字符串长度 用于中文标题显示截取长度和英文显示相同长度，需支持字符gb2312
	 * 
	 * @param s
	 *            :待截取的字符串
	 * @param length
	 *            :截取的长度(等同于全英文长度,一个汉字长度为2,英文和数字为1)
	 * @param ends
	 *            :如果字节长度超出指定长度，将添加的后缀
	 */
	public static String subLength(String s, int length, String ends) {
		if (s == null) {
			return "";
		}
		try {
			if (s.getBytes("GBK").length < length) {
				return s;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String compilestr = "[\\x00-\\xff]";
		Pattern p = Pattern.compile(compilestr);
		StringBuffer sb = new StringBuffer();
		int max = length;
		int n = 0;
		for (int i = 0; i < s.length(); i++) {
			String str = String.valueOf(s.charAt(i));
			if (p.matcher(str).matches()) {
				n++;
			} else {
				n += 2;
			}
			if (n > max) {
				break;
			}
			sb.append(str);
		}
		return sb.toString() + ends;
	}

	/**
	 * 将文件名转换为utf8字符串，用于文件下载时文件名称报头
	 * 
	 * @param s
	 * @return
	 */

	public static String toUTF8String(String s) {
		String strTitle = s.substring(0, s.lastIndexOf("."));
		String strExct = s.substring(s.lastIndexOf("."), s.length());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strTitle.length(); i++) {
			char c = strTitle.charAt(i);
			if (c >= 0 && c <= '\377') {
				if (sb.toString().length() < 147) {
					sb.append(c);
					continue;
				}
				sb.append("[1]");
				break;
			}
			byte b[];
			try {
				b = (new Character(c)).toString().getBytes("UTF-8");
			} catch (Exception ex) {
				System.out.println(ex);
				b = new byte[0];
			}
			if (sb.toString().length() < 140) {
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}

				continue;
			}
			sb.append("[1]");
			break;
		}

		return sb.toString() + strExct;

	}

	/**
	 * 获取
	 * 
	 * @param str
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	public static List getContainStr(String str, String prefix, String suffix) throws ArrayIndexOutOfBoundsException {
		int pre = 0;
		int suf = 0;
		List list = new ArrayList();
		try {
			while ((pre = str.indexOf(prefix)) != -1 && (suf = str.indexOf(suffix)) != -1) {
				list.add(str.substring(pre + 1, suf));
				str = str.substring(suf + 1);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw e;
		}
		return list;
	}

	/**
	 * 把字符串参数转化为Map
	 * 
	 * @param str
	 * @return
	 */
	public static Map string2Map(String str, String regex) {
		Map tmap = new HashMap();
		if (!StringHelper.isEmpty(str)) {
			String[] strs = str.split(regex);
			for (String tstr : strs) {
				if (!StringHelper.isEmpty(tstr)) {
					int ieq = tstr.indexOf("=");
					if (ieq != -1) {
						tmap.put(tstr.substring(0, ieq), tstr.substring(ieq + 1));
					}
				}
			}
		}
		return tmap;
	}

	public static boolean filterSql(String querystr) {
		String qstr = querystr.toLowerCase();
		for (String tstr : SQLCHARS) {
			if (querystr.indexOf(" " + tstr + " ") != -1) {
				System.out.println("非法字符:" + tstr);
				return true;
			}
		}
		if (querystr.indexOf("sqlwhere") == -1) {
			if (querystr.indexOf("'") != -1) {
				System.out.println("非法字符:'" + querystr);
				return true;
			}
			for (String tstr : SQLCHARS_V) {
				if (querystr.indexOf(" " + tstr + " ") != -1) {
					System.out.println("非法字符:" + tstr);
					return true;
				}
			}
		}
		return false;
	}

	// 返回值为二维数组
	public static String[][] transpose(String array[][]) {
		String array2[][] = new String[array[0].length][array.length];
		// 倒置矩阵
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				// 行列互换
				array2[j][i] = array[i][j];
			}
		}
		return array2; // 返回二维数组
	}

	public static String formatStr(String str, int len, String c) {
		StringBuffer retstr = new StringBuffer();
		int strlen = str.length();
		if (strlen >= len) {
			return str;
		} else {
			for (int i = 0; i < len - strlen; i++) {
				retstr.append(c);
			}
			retstr.append(str);
			return retstr.toString();
		}
	}

	/**
	 * . 组装insert语句
	 * 
	 * @param id
	 * @param allupdatevalues
	 * @return
	 */
	public static Map getInsertValuesMap(String id, String allupdatevalues) {
		Map valueMap = new HashMap();
		StringBuffer allupdateparams = new StringBuffer();
		String[] allvalues = allupdatevalues.split("&&&");
		int count = allvalues.length;
		Object[] objs = new Object[count];
		String tstr = "";
		for (int i = 0; i < count; i++) {
			String tvalue = allvalues[i];
			if (tvalue.startsWith("'")) {
				tvalue = tvalue.substring(1);
			}
			if (tvalue.endsWith("'")) {
				tvalue = tvalue.substring(0, tvalue.length() - 1);
			}
			tstr = tvalue;
			if (i > 0) {
				allupdateparams.append(",?");
			}
			if ("null".equals(tstr)) {
				objs[i] = null;
			} else {
				objs[i] = tstr;
			}
		}
		valueMap.put(id + "_str", "?" + allupdateparams);
		valueMap.put(id + "_obj", objs);
		return valueMap;
	}

	/**
	 * 组装 update 语句
	 * 
	 * @param id
	 * @param allupdatevalues
	 * @return
	 */
	public static Map getUpdateValuesMap(String id, String allupdatevalues) {
		Map valueMap = new HashMap();
		StringBuffer allupdateparams = new StringBuffer();
		String[] allvalues = allupdatevalues.split("&&&");
		int count = allvalues.length;
		Object[] objs = new Object[count];
		String tstr = "";
		for (int i = 0; i < count; i++) {
			tstr = StringHelper.null2String(allvalues[i]);
			String fieldname = "";
			String fieldvalue = "";
			int tempindex = tstr.indexOf("=");
			if (tempindex > 0) {
				fieldname = tstr.substring(0, tempindex);
				fieldvalue = tstr.substring(tempindex + 1);
			} else {
				fieldname = tstr;
			}
			allupdateparams.append(",").append(fieldname).append("=?");
			String tvalue = StringHelper.null2String(fieldvalue).trim();
			if ("null".equals(tvalue)) {
				objs[i] = null;
			} else {
				if (tvalue.startsWith("'")) {
					tvalue = tvalue.substring(1);
				}
				if (tvalue.endsWith("'")) {
					tvalue = tvalue.substring(0, tvalue.length() - 1);
				}
				objs[i] = tvalue;
			}
		}
		valueMap.put(id + "_str", allupdateparams.toString().substring(1));
		valueMap.put(id + "_obj", objs);
		return valueMap;
	}

	/**
	 * 组装 delete 语句
	 * 
	 * @param id
	 * @param allupdatevalues
	 * @return
	 */
	public static Map getDeleteSqlMap(String dialectname, String delsql) {
		Map valueMap = new HashMap();
		int whereindex = delsql.indexOf("where");
		int inindex = delsql.indexOf("id in(");
		int notindex = delsql.indexOf("id not in(");
		String newsql = "";
		String sql1 = delsql.substring(0, whereindex);
		if (notindex > 0) {
			String idsin = delsql.substring(inindex + 6, notindex - 6);
			String idsnotin = delsql.substring(notindex + 10, delsql.length() - 1);
			newsql = sql1 + " where ? like '%'+id+'%' and ? not like '%'+id+'%'";
			if (dialectname.contains("Oracle")) {
				newsql = sql1 + " where ? like '%'||id||'%' and ? not like '%'||id||'%'";
			}
			Object[] objs = {idsin, idsnotin};
			valueMap.put("_obj", objs);
		} else {
			String idsin = delsql.substring(inindex + 6, delsql.length() - 1);
			newsql = sql1 + " where ? like '%'+id+'%'";
			if (dialectname.contains("Oracle")) {
				newsql = sql1 + " where ? like '%'||id||'%'";
			}
			Object[] objs = {idsin};
			valueMap.put("_obj", objs);
		}
		valueMap.put("_str", newsql);
		return valueMap;
	}

	public static String[] splitString(String input, String delim) {
		int count = StringHelper.getStrCount(input, delim) + 1;
		String[] ss = new String[count];
		int i = 0;
		int index = input.indexOf(delim);
		while (index >= 0) {
			ss[i] = input.substring(0, index);
			input = input.substring(index + 1);
			index = input.indexOf(delim);
			i++;
		}
		return ss;
	}

	public static int getStrCount(String str, String regex) {
		int total = 0;
		for (String tmp = str; tmp != null && tmp.length() >= regex.length();) {
			if (tmp.indexOf(regex) == 0) {
				total++;
				tmp = tmp.substring(regex.length());
			} else {
				tmp = tmp.substring(1);
			}
		}
		return total;
	}

	/**
	 * 用,分割字符串数组
	 * 
	 * @param str
	 * @return
	 */
	public static String jion(String[] str) {
		StringBuffer sb = new StringBuffer();
		for (String s : str) {
			if (s == null) {
				continue;
			}
			sb.append(s).append(",");
		}
		return sb.toString().replaceAll(",$", "");
	}

	/**
	 * 用于将Mysql数据库取值时中文乱码处理
	 * 
	 * @param str
	 * @return
	 */
	public static String latin1ToGBK(String str) {
		try {
			String temp_p = str;
			byte[] temp_t = temp_p.getBytes("ISO-8859-1");
			String temp = new String(temp_t, "GBK");
			return temp;
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex);
			return "";
		}
	}

	/**
	 * 转化整数天数,不够两位补空格
	 */
	public static String transStr(int value) {
		StringBuffer sb = new StringBuffer();
		String temp = String.valueOf(value);
		if (temp.length() < 2) {
			sb.append("0").append(temp);
		} else {
			sb.append(temp);
		}
		return sb.toString();
	}

	public static String fillValuesToString(String str, Map ht) {
		return fillValuesToString(str, ht, '$');
	}

	public static String fillValuesToString(String str, Map ht, char VARIABLE_PREFIX) {

		char TERMINATOR = '\\';

		if (str == null || str.length() == 0 || ht == null) {
			return str;
		}

		char s[] = str.toCharArray();
		char ch, i = 0;
		String vname;
		StringBuffer buf = new StringBuffer();

		ch = s[i];
		while (true) {
			if (ch == VARIABLE_PREFIX) {
				vname = "";
				if (++i < s.length) {
					ch = s[i];
				} else {
					break;
				}
				while (true) {
					if (ch != '_' && ch != '-' && !Character.isLetterOrDigit(ch)) {
						break;
					}
					vname += ch;
					if (++i < s.length) {
						ch = s[i];
					} else {
						break;
					}
				}

				if (vname.length() != 0) {
					String vval = (String) ht.get(vname);
					if (vval != null) {
						buf.append(vval);
					}
				}
				if (vname.length() != 0 && ch == VARIABLE_PREFIX) {
					continue;
				}
				if (ch == TERMINATOR) {
					if (++i < s.length) {
						ch = s[i];
					} else {
						break;
					}
					continue;
				}
				if (i >= s.length) {
					break;
				}
			}

			buf.append(ch);
			if (++i < s.length) {
				ch = s[i];
			} else {
				break;
			}
		}
		return buf.toString();
	}

	/**
	 * 判断字符串的类型：0表字符，1表整数，2表浮点数，3为空
	 * @param str
	 * @return
	 */
	public static int getStringType(String str) {
		if(StringHelper.isEmpty(str)){
			return 3;
		}
		if(str.startsWith("-")){
			//处理负数的情况
			str = str.substring(1);
		}
		String nums = "0123456789.";
		char s[] = str.toCharArray();
		for (int i = 0; i < s.length; i++) {
			char tmp = s[i];
			if (!".".equals(tmp) && nums.indexOf(tmp) == -1) {
				return 0;
			}
		}
		if (str.indexOf(".") == -1) {
			if (str.startsWith("0")) {
				if (str.equals("0"))
					return 1;
				else
					return 0;
			} else {
				return 1;
			}
		} else {
			if (str.indexOf(".") == str.lastIndexOf(".")) {
				return 2;
			} else {
				return 0;
			}
		}
	}

	/**
	 * 传入BigDecimal金额，返回数字金额对应的中文大字与读法
	 * @param money
	 * 金额
	 * @return 金额中文大写
	 */

	public static String transFormMoney(BigDecimal money) {
		return transFormMoney(df.format(money));
	}
	
	/**
	 * 传入数字金额双精度型值，返回数字金额对应的中文大字与读法
	 * @param money
	 * 金额
	 * @return 金额中文大写
	 */

	public static String transFormMoney(double money) {
		return transFormMoney(df.format(money));
	}

	/**
	 * 传入数字金额浮点型值，返回数字金额对应的中文大字与读法
	 * @param money
	 * 金额
	 * @return 金额中文大写
	 */
	public static String transFormMoney(float money) {
		return transFormMoney(df.format(money));
	}

	/**
	 * 传入数字金额字符串，返回数字金额对应的中文大字与读法
	 * @param money
	 * 金额字符串
	 * @return 金额中文大写
	 */
	public static String transFormMoney(String money) {
		String result = "";
		try {
			BigDecimal big = new BigDecimal(money);
			String[] t = null;
			try{
				t = big.toString().replace(".", ";").split(";");
			} catch (Exception e){
				// 金额如果没有小数位时,也要加上小数位
				t = (big.toString() + ".0").replace(".", ";").split(";");
			}
			String[] intString = splitMoney(t[0]);
			String tmp_down = t[1];
			for (int i = 0; i < intString.length; i++){
				result = result + count(intString[i]);
				if (i != intString.length - 1 && count(intString[i]) != ""){
					result = result + CHS[intString.length - 2 - i];
				}
			}

			if (Integer.parseInt(tmp_down) == 0){
				result = result + (intString[0].equals("0") ? "零元" : "元整");
			} else {
				result = result + (intString[0].equals("0") ? "" : tmp_down.startsWith("0") ? "元零" : "元")
						+ getFloat(tmp_down);
			}

		} catch (Exception e) {
			return "你输入的不是数字符串";
		}
		return result;
	}

	/**
	 * 对整数部分字符串进行每四位分割放置分割符
	 * @param money
	 * 整数部分字符串
	 * @return 放置分割符后的字符串
	 */
	public static String[] splitMoney(String money){
		StringBuffer tmp_int = new StringBuffer();
		tmp_int.append(money);
		// 先對整數位進行分割，每四位爲一組。
		int i = tmp_int.length();
		do{
			try{
				// 進行try..catch是因爲當位數不能滿足每四位放分割符時，就退出循環
				i = i - 4;
				if (i == 0)
					break;
				tmp_int.insert(i, ';');
			} catch (Exception e){
				break;
			}
		} while (true);
		return tmp_int.toString().split(";");
	}

	/**
	 * 转换整数部分
	 * @param money
	 * 整数部分金额
	 * @return 整数部分大写
	 */
	public static String count(String money){
		String tmp = "";
		Integer money_int = 0;
		char[] money_char;
		// 如果數字開始是零時
		if (money.startsWith("0")){
			money_int = Integer.parseInt(money);
			if (money_int == 0)
				return tmp;
			else
				tmp = "零";
			money_char = money_int.toString().toCharArray();
		} else {
			money_char = money.toCharArray();
		}
		for (int i = 0; i < money_char.length; i++){
			if (money_char[i] != '0'){
				// 如果當前位不爲“0”，才可以進行數字和位數轉換
				tmp = tmp + CHS_NUMBER[Integer.parseInt(money_char[i] + "")] + CH[money_char.length - i];
			} else {
				// 要想該位轉換爲零，要滿足三個條件
				// 1.上一位沒有轉換成零，2.該位不是最後一位，3.該位的下一位不能爲零
				if (!tmp.endsWith("零") && i != money_char.length - 1 && money_char[i + 1] != '0'){
					tmp = tmp + CHS_NUMBER[Integer.parseInt(money_char[i] + "")];
				}
			}
		}
		return tmp;
	}

	/**
	 * 转换小数部分
	 * @param fl
	 * 小数
	 * @return 小数大写
	 */
	private static String getFloat(String fl){
		String f = "";
		char[] ch = fl.toCharArray();
		switch (ch.length){
			case 1 :{
				f = f + CHS_NUMBER[Integer.parseInt(ch[0] + "")] + "角整";
				break;
			}
			case 2 :{
				if (ch[0] != '0')
					f = f + CHS_NUMBER[Integer.parseInt(ch[0] + "")] + "角"
							+ CHS_NUMBER[Integer.parseInt(ch[1] + "")] + "分";
				else
					f = f + CHS_NUMBER[Integer.parseInt(ch[1] + "")] + "分";
				break;
			}
		}
		return f;
	}
	/**
     * 判断字符串的编码
     *
     * @param str
     * @return
     */  
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}
	
	public static String join(String separator, String ... joinedStr) {
		if(joinedStr == null || joinedStr.length == 0)
			return "";
		if(separator == null)
			separator = "";
		
		StringBuffer buf = new StringBuffer();
		for (String str : joinedStr) {
			if(str == null) str = "";
			buf.append(str).append(separator);
		}
		buf.delete(buf.length()-separator.length(), buf.length());
		
		return buf.toString();
	}

	public static String escape2(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);

		for (i = 0; i < src.length(); i++) {

			j = src.charAt(i);

			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static String unescape2(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	/**
	 * 检查matcherstring是否符合正则表达式regexstring
	 * @param matcherstring
	 * @param regexstring
	 * @return 符合：true
	 */
	public static boolean check(String matcherstring, String regexstring) {
		boolean flag = false;
		try {
			String check = regexstring;
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(matcherstring);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * unicode 转换成 中文
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
							"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}
	
	/**
	 * base64decode
	 * @param base64Str
	 * @return
	 */
	public static String base64ToString(String base64Str){
		base64Str = StringHelper.null2String(base64Str);
		String s = "";
		try{
			s = Base64Coder.decodeString(base64Str);
		}catch(Exception e){
			s = base64Str;
		}
		return s;
	}
	
	/**
	 * 去重复字符串
	 * @param string 要去重的字符串
	 * @param inDelimiter 输入字符串分隔符
	 * @param outDelimiter 输出字符串分隔符
	 * @return
	 */
	public static String removeDuplicate(String string, String inDelimiter, String outDelimiter){
		Set<String> set = new HashSet<String>();
		if(StringHelper.isEmpty(inDelimiter)){
			inDelimiter = ",";
		}
		for(String s : string.split(inDelimiter)){
			set.add(s);
		}
		if(StringHelper.isEmpty(outDelimiter)){
			outDelimiter = ",";
		}
		return StringUtils.join(set.toArray(), outDelimiter);
	}
	
	/**
	 * 	/**
	 * 去重复字符串
	 * @param string 要去重的字符串
	 * @param inDelimiter 输入字符串分隔符
	 * @return
	 */
	public static String removeDuplicate(String string, String inDelimiter){
		return removeDuplicate(string, inDelimiter, null);
	}
	
	/**
	 * 去重复字符串
	 * @return
	 */
	public static String removeDuplicate(String string){
		return removeDuplicate(string, null, null);
	}
	
	/**
	 * 根据Unicode编码完美的判断中文汉字和符号
	 * @param c
	 * @return
	 */
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/**
	 * 完整的判断中文汉字和符号
	 * @param strName
	 * @return
	 */
	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}
}
