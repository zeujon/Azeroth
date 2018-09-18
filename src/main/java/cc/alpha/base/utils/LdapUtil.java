package cc.alpha.base.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.Modification;
import com.unboundid.ldap.sdk.ModificationType;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.controls.SubentriesRequestControl;

public class LdapUtil {

	// 当前配置信息
	private static String ldapHost = "192.168.110.10";
	private static int ldapPort = 389;
	private static String ldapBaseDN = "dc=ztgame,dc=com";
	private static String ldapAdmin = "ztgame\\addadmin";
	private static String ldapPassword = "addadmin";
	private static LDAPConnection connection = null;

	/**
	 * 连接LDAP
	 */
	public static void openConnection() {
		if (connection == null) {
			try {
				connection = new LDAPConnection(ldapHost, ldapPort, ldapAdmin, ldapPassword);
				System.out.println("连接LDAP成功");
			} catch (Exception e) {
				System.out.println("连接LDAP出现错误：\n" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建DC
	 * @param baseDN
	 * @param dc
	 */
	public static void createDC(String baseDN, String dc) {
		String entryDN = "dc=" + dc + "," + baseDN;
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(entryDN);
			if (entry == null) {
				// 不存在则创建
				ArrayList<Attribute> attributes = new ArrayList<Attribute>();
				attributes.add(new Attribute("objectClass", "top", "organization", "dcObject"));
				attributes.add(new Attribute("dc", dc));
				attributes.add(new Attribute("o", dc));
				connection.add(entryDN, attributes);
				System.out.println("创建DC" + entryDN + "成功！");
			} else {
				System.out.println("DC " + entryDN + "已存在！");
			}
		} catch (Exception e) {
			System.out.println("创建DC出现错误：\n" + e.getMessage());
		}
	}

	/**
	 * 创建组织
	 * @param baseDN
	 * @param o
	 */
	public static void createO(String baseDN, String o) {
		String entryDN = "o=" + o + "," + baseDN;
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(entryDN);
			if (entry == null) {
				// 不存在则创建
				ArrayList<Attribute> attributes = new ArrayList<Attribute>();
				attributes.add(new Attribute("objectClass", "top", "organization"));
				attributes.add(new Attribute("o", o));
				connection.add(entryDN, attributes);
				System.out.println("创建组织" + entryDN + "成功！");
			} else {
				System.out.println("组织" + entryDN + "已存在！");
			}
		} catch (Exception e) {
			System.out.println("创建组织出现错误：\n" + e.getMessage());
		}
	}

	/**
	 * 创建组织单元
	 * @param baseDN
	 * @param ou
	 */
	public static void createOU(String baseDN, String ou) {
		String entryDN = "ou=" + ou + "," + baseDN;
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(entryDN);
			if (entry == null) {
				// 不存在则创建
				ArrayList<Attribute> attributes = new ArrayList<Attribute>();
				attributes.add(new Attribute("objectClass", "top", "organizationalUnit"));
				attributes.add(new Attribute("ou", ou));
				connection.add(entryDN, attributes);
				System.out.println("创建组织单元" + entryDN + "成功！");
			} else {
				System.out.println("组织单元" + entryDN + "已存在！");
			}
		} catch (Exception e) {
			System.out.println("创建组织单元出现错误：\n" + e.getMessage());
		}
	}

	/**
	 * 创建用户
	 * @param baseDN
	 * @param uid
	 */
	public static void createEntry(String baseDN, String uid) {
		String entryDN = "uid=" + uid + "," + baseDN;
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(entryDN);
			if (entry == null) {
				// 不存在则创建
				ArrayList<Attribute> attributes = new ArrayList<Attribute>();
				attributes.add(new Attribute("objectClass", "top", "account"));
				attributes.add(new Attribute("uid", uid));
				connection.add(entryDN, attributes);
				System.out.println("创建用户" + entryDN + "成功！");
			} else {
				System.out.println("用户" + entryDN + "已存在！");
			}
		} catch (Exception e) {
			System.out.println("创建用户出现错误：\n" + e.getMessage());
		}
	}

	/**
	 * 修改用户信息
	 * @param requestDN
	 * @param data
	 */
	public static void modifyEntry(String requestDN, Map<String, String> data) {
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(requestDN);
			if (entry == null) {
				System.out.println(requestDN + " user:" + requestDN + " 不存在");
				return;
			}
			// 修改信息
			ArrayList<Modification> md = new ArrayList<Modification>();
			for (String key : data.keySet()) {
				md.add(new Modification(ModificationType.REPLACE, key, data.get(key)));
			}
			connection.modify(requestDN, md);

			System.out.println("修改用户信息成！");
		} catch (Exception e) {
			System.out.println("修改用户信息出现错误：\n" + e.getMessage());
		}
	}

	/**
	 * 删除用户信息
	 * @param requestDN
	 */
	public static void deleteEntry(String requestDN) {
		try {
			// 连接LDAP
			openConnection();

			SearchResultEntry entry = connection.getEntry(requestDN);
			if (entry == null) {
				System.out.println(requestDN + " user:" + requestDN + "不存在");
				return;
			}
			// 删除
			connection.delete(requestDN);
			System.out.println("删除用户信息成！");
		} catch (Exception e) {
			System.out.println("删除用户信息出现错误：\n" + e.getMessage());
		}
	}

	/**
	 * LDAP查询
	 * @param searchDN
	 * @param filter
	 */
	public static void queryLdap(String searchDN, String filter) {
		try {
			// 连接LDAP
			openConnection();

			// 查询企业所有用户
			SearchRequest searchRequest = new SearchRequest(searchDN, SearchScope.SUB, "(" + filter + ")");
			searchRequest.addControl(new SubentriesRequestControl());
			SearchResult searchResult = connection.search(searchRequest);
			System.out.println(">>>共查询到" + searchResult.getSearchEntries().size() + "条记录");
			int index = 1;
			for (SearchResultEntry entry : searchResult.getSearchEntries()) {
				System.out.println((index++) + " " + entry.getDN()+";\n  "
						+ "telephoneNumber="+entry.getAttributeValue("telephoneNumber")+"; "
						+ "employeeID="+entry.getAttributeValue("employeeID")+"; "
						+ "staffnum="+entry.getAttributeValue("staffnum"));
			}
		} catch (Exception e) {
			System.out.println("查询错误，错误信息如下：\n" + e.getMessage());
		}
	}

	/**
	 * 测试代码
	 * @param args
	 */
	public static void main(String[] args) {
		String root = "com";
		String dc = "ztadmin";
		String o = "易志强";
		String ou = "巨人网络集团";
		String uid = "yizhiqiang";
		String filter = "objectClass=user";
//		filter = "sAMAccountName=wanghui";
		filter = "staffnum=18036";
		

//		createDC("dc=" + root, dc);
//		createO("dc=" + dc + ",dc=" + root, o);
//		createOU("o=" + o + ",dc=" + dc + ",dc=" + root, ou);
//		createEntry("ou=" + ou + ",o=" + o + ",dc=" + dc + ",dc=" + root, uid);
//		queryLdap("ou=" + ou + ",cn=" + o + ",dc=" + dc + ",dc=" + root, filter);
		
		queryLdap("OU=巨人网络集团,DC=ztgame,DC=com", filter);
//
//		HashMap<String, String> data = new HashMap<String, String>(0);
//		data.put("userid", uid);
//		modifyEntry("uid=" + uid + ",ou=" + ou + ",o=" + o + ",dc=" + dc + ",dc=" + root, data);
//
//		deleteEntry("uid=" + uid + ",ou=" + ou + ",o=" + o + ",dc=" + dc + ",dc=" + root);
		
//		openConnection();
		
//		queryLdap("ou=" + ou + ",o=" + o + ",dc=" + dc + ",dc=" + root, filter);
	}
}
