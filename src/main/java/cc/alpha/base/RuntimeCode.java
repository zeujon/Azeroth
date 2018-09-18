package com.velcro.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.tools.javac.Main;
import com.velcro.base.util.StringHelper;

public class RuntimeCode {

	private static Main javac = new Main();
	protected static final Log logger = LogFactory.getLog(RuntimeCode.class);

	public Object execute(Map parameters) {
		Object bResult = null;
		String javafile = StringHelper.null2String(parameters.get("javafile"));
		String methodname = StringHelper.null2String(parameters.get("methodname"));
		Object[] methodArgs = (Object[]) parameters.get("methodargs");

		File file = new File(javafile);
		if (file == null)
			return null;

		String filename = file.getName();
		String classname = getClassName(filename);

		Class cls = null;
		try {
			cls = Class.forName(classname);
		} catch (Exception e) {
			try {
				compile(file);
				cls = Class.forName(classname);
			} catch (Exception er) {
				logger.error("动态编译代码错误信息：" + er.toString());
			}
		}

		try {
			if (cls != null)
				bResult = run(cls, methodname, methodArgs);

		} catch (Exception e) {
			logger.error("动态执行代码错误信息：" + e.toString());
		}

		return bResult;
	}

	public synchronized static int compile(File rf) throws Exception {
		File file;
		String javafilepath = System.getProperty("user.dir");

		String filename = rf.getName();
		String classname = getClassName(filename);

		file = new File(javafilepath + File.separatorChar + filename);

		InputStreamReader read = new InputStreamReader(new FileInputStream(rf), "utf-8");

		BufferedReader reader = new BufferedReader(read);

		String docContent = new String();
		String line = new String();
		while ((line = reader.readLine()) != null) {
			docContent += new String(line.getBytes());
			docContent += "\r\n";
		}
		if (docContent.startsWith("?"))
			docContent = docContent.substring(1);

		docContent = StringHelper.replaceString(docContent, "?import ", "import ");

		PrintWriter out = new PrintWriter(new FileOutputStream(file));
		out.write(docContent);
		out.flush();
		out.close();

		/**
		 * BEGIN 把所有WEB-INF/lib下的所有jar加入到classpath中。
		 */
		String sysPath = BaseContext.getRootPath().replace("\\\\", "\\");
		String libroot = sysPath + "WEB-INF" + File.separatorChar + "lib" + File.separatorChar;
		StringBuffer sb = new StringBuffer();
		File lib = new File(libroot);
		if (lib.exists()) {
			File[] libs = lib.listFiles();
			for (File jar : libs) {
				if (jar.getName().endsWith(".jar")) {
					sb.append(jar.getAbsolutePath() + ";");
				}
			}
		}
		String libroot2 = sysPath.substring(0,libroot.lastIndexOf("deploy"))+ "lib" + File.separatorChar;
		File lib2 = new File(libroot2);
		if (lib2.exists()) {
			File[] libs2 = lib2.listFiles();
			for (File jar : libs2) {
				if (jar.getName().endsWith(".jar")) {
					sb.append(jar.getAbsolutePath() + ";");
				}
			}
		}
		String libpath = sb.toString();
		/**
		 * END
		 */
		String classespath = sysPath + "WEB-INF" + File.separatorChar + "classes";
		String[] args = new String[] { "-cp", classespath + ";" + libpath, "-d", classespath, "-sourcepath", javafilepath, filename };
		int status = javac.compile(args);
		file.delete();
		return status;
	}

	private static synchronized Object run(Class cls, String methodname, Object[] methodArgs) throws Exception {
		Object result = null;

		try {

			Object object = cls.newInstance();

			Method[] methods = cls.getMethods();
			for (int i = 0; i < methods.length; i++) {
				String _methodName = methods[i].getName();
				if (_methodName.equals(methodname)) {

					result = (Object) methods[i].invoke(object, methodArgs);
				}
			}
		} catch (SecurityException se) {

			logger.error("动态执行代码错误信息：" + se.toString());
		}
		return result;
	}

	private static String getClassName(String filename) {
		return filename.substring(0, filename.length() - 5);
	}

}
