package cc.alpha.base.utils;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import com.sun.jndi.ldap.LdapResult;

public class LdapHelper {
	private final String LDAPURL = "ldap://192.168.110.10:389/";
	private final String ROOT = "dc=ztgame,dc=com"; // 根据自己情况进行修改
	private final String ADMINUSER = "ztgame\\addadmin";// 管理员,注意用户名的写法：domain\User 或 cn=user
	private final String ADMINPWD = "addadmin";// 管理员密码
	
	private LdapContext ctx = null;
	private final Control[] connCtls = null;

	private void ldapConnect() {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, LDAPURL + ROOT);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, ADMINUSER);
		env.put(Context.SECURITY_CREDENTIALS, ADMINPWD);

		try {
			ctx = new InitialLdapContext(env, connCtls);// 初始化
			System.out.println("连接成功");

		} catch (javax.naming.AuthenticationException e) {
			System.out.println("连接失败：");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("连接出错：");
			e.printStackTrace();
		}

	}

	private void closeContext() {
		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
				e.printStackTrace();
			}

		}
	}

	private String getUserDN(String uid) {
		String userDN = "";
		ldapConnect();
		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration<SearchResult> en = ctx.search("", "employeeID=" + uid, constraints);

			if (en == null || !en.hasMoreElements()) {
				System.out.println("未找到该用户");
			}
			// maybe more than one element
			while (en != null && en.hasMoreElements()) {
				Object obj = en.nextElement();
				if (obj instanceof SearchResult) {
					SearchResult si = (SearchResult) obj;
					userDN += si.getName();
					userDN += "," + ROOT;
				} else {
					System.out.println(obj);
				}
			}
		} catch (Exception e) {
			System.out.println("查找用户时产生异常。");
			e.printStackTrace();
		}

		return userDN;
	}

	private String getUserByAttr(String attribute, String value) {
		String userDN = "";

		ldapConnect();

		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration en = ctx.search("", attribute+"=" + value, constraints); // The
																					// UID
																					// you
																					// are
																					// going
																					// to
																					// query,*
																					// means
																					// all
																					// nodes
			if (en == null) {
				System.out.println("Have no NamingEnumeration.");
			}
			if (!en.hasMoreElements()) {
				System.out.println("Have no element.");
			}
			while (en != null && en.hasMoreElements()) {// maybe more than one
														// element
				Object obj = en.nextElement();
				if (obj instanceof SearchResult) {
					SearchResult si = (SearchResult) obj;
					userDN += si.getName();
					userDN += "," + ROOT;
					
					NamingEnumeration<? extends Attribute> attrs = si.getAttributes().getAll();//获取所有属性
					while (attrs.hasMore()) {
						Attribute attr = attrs.next();
						System.out.println(attr.getID() + "  =  " + attr.get());
					}
					
				} else {
					System.out.println(obj);
				}
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("Exception in search():" + e);
		}

		return userDN;
	}
	
	
	private String getDN(String type, String value) {
		String dn = "";

		ldapConnect();

		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration en = ctx.search("", type+"=" + value, constraints); 
			if (en == null) {
				System.out.println("Have no NamingEnumeration.");
			}
			if (!en.hasMoreElements()) {
				System.out.println("Have no element.");
			}
			while (en != null && en.hasMoreElements()) {
				Object obj = en.nextElement();
				if (obj instanceof SearchResult) {
					SearchResult si = (SearchResult) obj;
					dn += si.getName();
					dn += "," + ROOT;
				} else {
					System.out.println(obj);
				}
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("Exception in search():" + e);
		}

		return dn;
	}
	
	/**
	 * 部门查询
	 * @param deptName 部门名称
	 * @return map:如果map.size()不为0，则查询成功；否则为发生异常       null:部门不存在
	 */
	public Map<String, String> findDept(String deptName) {
		String deptDN = getDN("ou", deptName);
		if(deptDN == null){
			return null;
		}
		
		Map<String, String> messageMap = new HashMap<>();
		
		SearchControls searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String searchFiter = "ou=" + deptName;
		searchCtls.setReturningAttributes(null);
		
		try {
			NamingEnumeration<SearchResult> answer = ctx.search(ROOT, searchFiter, searchCtls);
			while(answer.hasMoreElements()){
				SearchResult sr = (SearchResult) answer.next();
				Attributes attrs = sr.getAttributes();
				if(attrs != null){
					for(NamingEnumeration<? extends Attribute> e = attrs.getAll();e.hasMore();){
						String user = e.next().toString().replace(" ", "");
						String[] buff = user.split(":");
						messageMap.put(buff[0], buff[1]);
					}
				}
			}
			return messageMap;
		} catch (NamingException e) {
			Map<String, String> nullMap = new HashMap<>();
			return nullMap;
		}
	}
	
	public boolean authenricate(String UID, String password) {
		boolean valide = false;
		String userDN = getUserDN(UID);

		try {
			ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, userDN);
			ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
			ctx.reconnect(connCtls);
			System.out.println(userDN + " 验证通过");
			valide = true;
		} catch (AuthenticationException e) {
			System.out.println(userDN + " 验证失败");
			System.out.println(e.toString());
			valide = false;
		} catch (NamingException e) {
			System.out.println(userDN + " 验证失败");
			valide = false;
		}
		closeContext();
		return valide;
	}

	public boolean addUser(String usr, String pwd, String uid, String description) {
		try {
			ldapConnect();
			BasicAttributes attrsbu = new BasicAttributes();
			BasicAttribute objclassSet = new BasicAttribute("objectclass");
			objclassSet.add("inetOrgPerson");
			attrsbu.put(objclassSet);
			attrsbu.put("sn", usr);
			attrsbu.put("cn", usr);
			attrsbu.put("uid", uid);
			attrsbu.put("userPassword", pwd);
			attrsbu.put("description", description);
			ctx.createSubcontext("uid=" + uid + "", attrsbu);

			return true;
		} catch (NamingException ex) {
			ex.printStackTrace();
		}
		closeContext();
		return false;
	}

	// 测试
	public static void main(String[] args) {
		LdapHelper ldap = new LdapHelper();

//		System.out.println(ldap.getUserByAttr("sAMAccountName","zhouyisheng"));
		
		System.out.println(ldap.getDN("ou","巨人网络集团"));
		
		System.out.println(ldap.getDN("sAMAccountName","chenxu"));
		
//		System.out.println(ldap.getUserByAttr("mail","yizhiqiang@ztgame.com"));
//		
//		System.out.println(ldap.getUserByAttr("employeeID","02301"));
//		
//		System.out.println(ldap.getUserByAttr("staffnum","02301"));
//		System.out.println(ldap.getUserDN("02299"));
//		ldap.ldapConnect();

//		String account = "";
//		if (ldap.authenricate("yorker", "daqsoft") == true) {
//
//			System.out.println("该用户认证成功");
//
//		}
		// ldap.addUser("yorker","secret");

	}
}