package cc.alpha.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import com.velcro.base.util.DateHelper;
import com.velcro.base.util.NumberHelper;
import com.velcro.base.util.StringHelper;

/**
 * 生成发布用的增量war包
 */
public final class ReleaseMUSTHelper {
	private String fromDir;
	private String toDir;
	private String beginDate;// 开始日期
	private String beginTime;// 开始时间
	private boolean isCreateSrc;

	public static void main(String arg[]) {
		String fromDate = "2016-10-14"; // "2007-10-01"以后修改的文件 不包含"2007-10-01"
		String fromTime = "18:55:00";
		String from = "D:\\Project\\TeamWork\\teamwork"; // 开发路径
		String to = "D:\\Project\\TeamWork\\packout\\teamwork_" + fromDate + "_" + DateHelper.getCurrentDate() + ".war"; // 生成的war包路径
		boolean isCreateSrc = true;
		ReleaseMUSTHelper releaseHelper = new ReleaseMUSTHelper(from, to, fromDate, fromTime, isCreateSrc);
		releaseHelper.release();

	}
	
	public ReleaseMUSTHelper(String fDir, String tDir, String bDate, String bTime, boolean isCreateSrc) {
		this.fromDir = fDir; // 开发目录
		this.toDir = tDir; // 发布目录
		this.beginDate = bDate;
		this.beginTime = bTime;
		this.isCreateSrc = isCreateSrc;
	}
	
	/**
	 * 初始化发布目录及时间
	 */
	private void init() {
		String filePath = toDir;
		filePath = filePath.toString();// 中文转换
		java.io.File myFilePath = new java.io.File(filePath);
		if (myFilePath.exists()) {
			myFilePath.delete();
		}
		if (this.isFolder(filePath))
			myFilePath.mkdirs();

		printMsg("正在创建输出目录！\n\n\n\n\n");
	}
	
	/**
	 * 处理resources目录
	 * @param f
	 */
	private void dealResources(File f) {
		if (this.isNeedDeal(f)) {
			String to = "";
			// String from = "";
			if (this.isSomeTypeFile(f, "xml") || this.isSomeTypeFile(f, "properties") || this.isSomeTypeFile(f, "wsdd") || this.isSomeTypeFile(f, "tld")) { // deal
																																							// xml
				to = f.toString().replace(fromDir + "\\src\\main\\resources\\", toDir + "\\WEB-INF\\classes\\");
				try {
					copyFile(f.toString(), to);
				} catch (Exception e) {
					System.out.println("copy xml,properties file error!+++Exception===" + e.toString());
				}
			}
		}
	}

	/**
	 * 处理src目录类文件
	 * @param f
	 */
	private void dealSrc(File f) {
		if (this.isNeedDeal(f)) {
			String to = "";
			String from = "";
			if (this.isSomeTypeFile(f, "xml") || this.isSomeTypeFile(f, "properties") || this.isSomeTypeFile(f, "wsdd")) { // deal
																															// xml
				to = f.toString().replace(fromDir + "\\src\\main\\java\\", toDir + "\\WEB-INF\\");
				try {
					copyFile(f.toString(), to);
				} catch (Exception e) {
					System.out.println("copy xml,properties file error!+++Exception===" + e.toString());
				}
			} else if (this.isSomeTypeFile(f, "java")) {// deal java
				from = f.toString().replace("\\src\\main\\java\\", "\\target\\classes\\");
				from = from.replace(".java", ".class");
				to = from.replace(fromDir + "\\target\\", toDir + "\\WEB-INF\\");
				String tosrc = f.toString().replace(fromDir + "\\src\\main\\java\\", toDir + "\\src\\");
				try {
					copyFile(from, to);
					if (isCreateSrc) {
						copyFile(f.toString(), tosrc);
					}
				} catch (Exception e) {
					System.out.println("copy class file error!+++Exception===" + e.toString());
				}
			}
		}
	}
	
	/**
	 * 处理内部类文件
	 * @param f
	 */
	private void dealSrc2(File f) {
		if (this.isNeedDeal(f)) {
			if (f.getName().contains("$")) {
				try {
					String from = f.toString();
					from = from.replace(".java", ".class");
					String to = from.replace(fromDir + "\\target\\", toDir + "\\WEB-INF\\");
					copyFile(from, to);
				} catch (Exception e) {
					System.out.println("copy class file error!+++Exception===" + e.toString());
				}
			}
		}
	}

	/**
	 * 处理webapp目录
	 * @param f
	 */
	private void dealJsp(File f) {
		if (this.isSomeTypeFile(f, "jsp") || this.isSomeTypeFile(f, "html") || this.isSomeTypeFile(f, "gif") || this.isSomeTypeFile(f, "jpg") || this.isSomeTypeFile(f, "bmp")
				|| this.isSomeTypeFile(f, "css") || this.isSomeTypeFile(f, "png") || this.isSomeTypeFile(f, "jar") || this.isSomeTypeFile(f, "properties") || this.isSomeTypeFile(f, "bat")
				|| this.isSomeTypeFile(f, "cer") || this.isSomeTypeFile(f, "xml") || this.isSomeTypeFile(f, "scc") || this.isSomeTypeFile(f, "js") || this.isSomeTypeFile(f, "wsdd")
				|| this.isSomeTypeFile(f, "tld") || this.isSomeTypeFile(f, "xls") || this.isSomeTypeFile(f, "json") || this.isSomeTypeFile(f, "txt") || this.isSomeTypeFile(f, "swf")) {
			if (this.isNeedDeal(f)) {
				String to = f.toString().replace(fromDir + "\\src\\main\\webapp", toDir);
				try {
					copyFile(f.toString(), to);
				} catch (Exception e) {
					System.out.println("copy jsp file error!+++Exception===" + e.toString());
				}
			}
		}
	}

	/**
	 * 根据服务器上用户对应的自己的文件夹，生成对应的类型下的文档
	 * @param filepath
	 * @param applicationId
	 */
	public void createDocByFilePath(String filepath, String applicationId) {
		// Date d = new Date();
		String curdate = DateHelper.getCurrentDate();
		String comdate = DateHelper.dayMove(curdate, -1);

		File f = (new File(filepath));
		if (f.isDirectory()) {
			File[] fe = f.listFiles();
			for (int i = 0; i < fe.length; i++) {
				if (fe[i].isDirectory() && isFolder(fe[i].getName())) {
					String userdirpath = fe[i].toString();
					// String userdir =
					// userdirpath.substring(userdirpath.lastIndexOf("\\")+1);
					String domainUserId = "";
					recursionDoc(userdirpath, comdate, domainUserId, applicationId);
				}
			}
		}
	}

	private void recursionDoc(String bdir, String comdate, String domainUserId, String applicationId) {
		File f = (new File(bdir));
		if (f.isDirectory()) {
			File[] fe = f.listFiles();
			for (int i = 0; i < fe.length; i++) {
				if (fe[i].isDirectory() && isFolder(fe[i].getName())) {
					recursionDoc(fe[i].toString(), comdate, domainUserId, applicationId);
				} else {
					if (!fe[i].isDirectory()) {
						// String a = fe[i].toString();
						dealResources(fe[i], comdate);
					}
				}
			}
		}
	}

	private void dealResources(File f, String comdate) {
		if (this.isNeedDeal(f, comdate)) {
			String to = "";
			// String from = "";
			to = f.toString().replace("F:\\TEMP", "D:\\GreenSoft\\apache-tomcat-7.0.12\\webapps\\et7\\uploads\\item");
			try {
				copyFile(f.toString(), to);
			} catch (Exception e) {
				System.out.println("copy xml,properties file error!+++Exception===" + e.toString());
			}
		}
	}

	/**
	 * //判断文件是否需要处理 在 begindate之后修改的文件需要处理
	 * @param f
	 * @return
	 */
	private boolean isNeedDeal(File f) {
		boolean ret = false;
		Date modifyDate = new Date(f.getAbsoluteFile().lastModified());
		String mDate = DateHelper.getDate(modifyDate);
		Date d = new Date();
		d.setYear(NumberHelper.string2Int(this.beginDate.substring(0, 4)) - 1900);
		d.setMonth(NumberHelper.string2Int(this.beginDate.substring(5, 7)) - 1);
		d.setDate(NumberHelper.string2Int(this.beginDate.substring(8, 10)));
		d.setHours(NumberHelper.string2Int(this.beginTime.substring(0, 2)));
		d.setMinutes(NumberHelper.string2Int(this.beginTime.substring(3, 5)));
		d.setSeconds(NumberHelper.string2Int(this.beginTime.substring(6, 7)));

		if (modifyDate.getTime() >= d.getTime())
			ret = true;
		// ret = DateHelper.afterAndEqual(beginDate,mDate );
		// long datetime = modifyDate.getTime() ;
		// Timestamp timestamp = new Timestamp(datetime) ;
		// String time = (timestamp.toString()).substring(11,13) + ":" +
		// (timestamp.toString()).substring(14,16) + ":"
		// +(timestamp.toString()).substring(17,19);
		// if (beginDate.equals(mDate)){
		// if (time < this.beginTime)
		// ret = false;
		// }

		return ret;
	}
	
	/**
	 * 判断文件是否某一类型
	 * @param f
	 * @param type
	 * @return
	 */
	private boolean isSomeTypeFile(File f, String type) {
		boolean ret = false;
		String fileName = f.getAbsoluteFile().getName();
		int len = type.length();
		int filelen = fileName.length();
		if (filelen > len && type.equalsIgnoreCase(fileName.substring(fileName.length() - len)))
			ret = true;
		return ret;
	}
	
	/**
	 * 判断是否是 .* 形式的文件夹 ，带点的返回false
	 * @param path
	 * @return
	 */
	private boolean isFolder(String path) {
		boolean ret = true;
		path = path.toString();
		if (".".equals(path.substring(0, 1))) {
			ret = false;
		} else {
			int idx = path.lastIndexOf("\\");
			if (idx > 0) {
				String temp = path.substring(idx + 1);
				if (".".equals(temp.substring(0, 1)))
					ret = false;
			}
		}
		if (path.contains(".svn"))
			ret = false;
		return ret;
	}
	
	/**
	 * //判断文件是否需要处理 在 begindate之后修改的文件需要处理
	 * @param f
	 * @return
	 */
	private boolean isNeedDeal(File f, String comdate) {
		// String to =
		// "D:\\GreenSoft\\apache-tomcat-7.0.12\\webapps\\et7\\uploads\\item\\你\\我\\他\\adsdf.doc";
		// String typepathname =
		// to.substring(to.indexOf("\\item")+5,to.indexOf("adsdf.doc"));
		// String fname = f.getName().substring(0,f.getName().indexOf("."));
		Date modifyDate = new Date(f.getAbsoluteFile().lastModified());
		String mdate = DateHelper.getDate(modifyDate);
		return DateHelper.after(comdate, mdate);
	}

	/**
	 * 拷贝文件
	 * @param input
	 * @param output
	 * @throws Exception
	 */
	private void copyFile(String input, String output) throws Exception {
		int BUFSIZE = 65536;
		int idx = output.lastIndexOf("\\");
		if (idx > 0) {
			String path = output.substring(0, idx);
			java.io.File myFilePath = new java.io.File(path);
			if (!myFilePath.exists())
				myFilePath.mkdirs();
		}

		try {
			FileInputStream fis = new FileInputStream(input);
			FileOutputStream fos = new FileOutputStream(output);
			this.printMsg(output);

			int s;
			byte[] buf = new byte[BUFSIZE];
			while ((s = fis.read(buf)) > -1) {
				fos.write(buf, 0, s);
			}
			
			fis.close();
			fos.close();
		} catch (Exception ex) {
			throw new Exception("makeFileError=========" + ex.getMessage());
		}
	}
	
	/**
	 * 遍历处理文件
	 * @param bdir
	 */
	private void recursion(String bdir, String type) {
		String url1 = bdir;
		File f = (new File(url1));
		if (f.isDirectory()) {
			File[] fe = f.listFiles();
			for (int i = 0; i < fe.length; i++) {
				if (fe[i].getName().indexOf(".svn") == -1) {
					if (fe[i].isDirectory() && isFolder(fe[i].getName())) {
						recursion(fe[i].toString() , type);//调用自己
					} else {
						if (!fe[i].isDirectory()) {
							if("webapp".equals(type)){
								dealJsp(fe[i]);
							}else if("resources".equals(type)){
								dealResources(fe[i]);
							}else if("class".equals(type)){
								dealSrc(fe[i]);
							}else if("innerclass".equals(type)){
								dealSrc2(fe[i]);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 打印消息
	 * @param msg
	 */
	private void printMsg(String msg) {
		System.out.println(StringHelper.null2String(msg));
	}
	
	/**
	 * 发布文件
	 */
	private void release() {
		this.init();
		
		printMsg("#################################################################");
		printMsg("##                      处理webapp目录                                                                    ##");
		printMsg("#################################################################");
		printMsg("开始处理webapp目录！");
//		this.recursionWebroot(fromDir + "\\src\\main\\webapp");// 处理jsp
		this.recursion(fromDir + "\\src\\main\\webapp" , "webapp");// 处理jsp
		printMsg("处理webapp目录成功！\n\n\n\n\n");

		printMsg("#################################################################");
		printMsg("##                      处理src下类文件                                                                  ##");
		printMsg("#################################################################");
		printMsg("开始处理src目录！");
//		this.recursionSrc(fromDir + "\\src\\main\\java");// 处理src下文件
		this.recursion(fromDir + "\\src\\main\\java" , "class");// 处理src下文件

		printMsg("\n\n\n\n\n");
		printMsg("#################################################################");
		printMsg("##                      处理内部类文件                                                                     ##");
		printMsg("#################################################################");
//		this.recursionSrc2(fromDir + "\\target\\classes");// 处理src下内部类文件
		this.recursion(fromDir + "\\target\\classes" , "innerclass");// 处理src下内部类文件
		printMsg("处理src目录成功！\n\n\n\n\n");

		printMsg("#################################################################");
		printMsg("##                      处理resources目录                                                            ##");
		printMsg("#################################################################");
		printMsg("开始处理resources目录！");
//		this.recursionResources(fromDir + "\\src\\main\\resources");// 处理java下文件
		this.recursion(fromDir + "\\src\\main\\resources" , "resources");// 处理java下文件
		printMsg("处理 resources 目录成功！\n\n\n\n\n");

		this.printMsg("文件成功发布至" + toDir + "!");
		this.printMsg("Finished!");
	}
	
}
