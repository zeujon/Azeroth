package cc.alpha.base.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ResizeImage {

	private static ArrayList<String> filelist = new ArrayList<String>();

	/**
	 * 用于批量创建缩略图
	 * 
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ClassNotFoundException, ParseException, IOException {

//		Long star = System.currentTimeMillis();
//		composePic("c:/1/yuying_logo.png", "c:/1/yuying_logo.png", "c:/1/Sayuying_logo.png", 350, 350);
//		Long end = System.currentTimeMillis();
//		System.out.print("time====:" + (end - star));
		
		String filePath = "C:/img150901";
		getFiles(filePath);
		for (int i = 0; i < filelist.size(); i++) {
			// System.out.println(filelist.get(i));
			String filepath = filelist.get(i);
			String path = filepath.substring(0, filepath.lastIndexOf("\\"));
			String name = filepath.substring(filepath.lastIndexOf("\\") + 1, filepath.length());
			String targetpath = path + "/S" + name;
			// 重设图片并重新生成
			String returnurl = createNewImg(filepath, targetpath, 480, 960);
			File srcfile = new File(filepath);
			System.out.println(returnurl);
			System.out.println(srcfile.delete());
		}
	}

	/*
	 * 通过递归得到某一路径下所有的目录及其文件
	 */
	static void getFiles(String filePath) throws IOException {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file1 : files) {
			if (file1.isDirectory()) {
				/*
				 * 递归调用
				 */
				getFiles(file1.getAbsolutePath());
				// System.out.println(file1.getAbsolutePath());
			} else {
				filelist.add(file1.getAbsolutePath());
				// System.out.println(file1.getAbsolutePath());
			}
		}
	}

	static String createNewImg(String srcimgpath, String tarimgpath, int width, int height) throws IOException {
		String newurl = tarimgpath; // 新的缩略图保存地址
		// Image src = javax.imageio.ImageIO.read(file); // 构造Image对象
		// ImageIO.read()读取图片产生ICC信息的丢失,生成的缩略图会蒙上一层红色
		Image img = Toolkit.getDefaultToolkit().getImage(srcimgpath);
		BufferedImage src = toBufferedImage(img);
		if (src != null) {
			float tagsize = width;
			int old_w = src.getWidth(null); // 得到源图宽
			int old_h = src.getHeight(null);
			int new_w = 0;
			int new_h = 0; // 得到源图长
			float tempdouble;
			// if (old_w > old_h) {
			tempdouble = old_w / tagsize;
			// } else {
			// tempdouble = old_h / tagsize;
			// }
			new_w = Math.round(old_w / tempdouble);
			new_h = Math.round(old_h / tempdouble);// 计算新图长宽
			BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
			// tag.getGraphics().drawImage(src, 0, 0, new_w, new_h, null); //
			// 绘制缩小后的图
			tag.getGraphics().drawImage(src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0, null);
			FileOutputStream newimage = new FileOutputStream(newurl); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
			encoder.encode(tag); // 近JPEG编码
			newimage.close();
			return newurl;
		}
		return "";
	}

	static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();
		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent
		// Pixels
		// boolean hasAlpha = hasAlpha(image);
		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			/*
			 * if (hasAlpha) { transparency = Transparency.BITMASK; }
			 */
			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}
		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			// int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
			/*
			 * if (hasAlpha) { type = BufferedImage.TYPE_INT_ARGB; }
			 */
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}
		// Copy image to buffered image
		Graphics g = bimage.createGraphics();
		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bimage;
	}
	
	
	private byte[] cropAndCompressImage(int x, int y, int w, int h, BufferedImage image, int targetW, int targetH) throws Exception {
		if (w == -1 && h == -1 && image != null) {
			// use full image
			w = image.getWidth();
			h = image.getHeight();
		}
		if (x < 0 || y < 0 || w <= 0 || h <= 0 || image == null) {
			// error
		}
		w = Math.min(x + w, image.getWidth()) - x;
		h = Math.min(y + h, image.getHeight()) - y;
		try {
			BufferedImage cropedImage = image.getSubimage(x, y, w, h);

			// do image compression
			int type = image.getType() == 0 ? Transparency.TRANSLUCENT : image.getType();
//			BufferedImage compressedImage = new BufferedImage(targetW, targetH, type);
			// 下面要解决的是图片变红问题。这个问题其实是alpha通道需要变为透明的。而原始程序是判断，如果该图片是定义为Custom的图片，那么就变为透明。显然custom的图片不一定是透明图片。所以这里需要判断image是否为透明的。代码变为:
			// int type = image.getType() == 0 ? BufferedImage.TRANSLUCENT: image.getType();
			// BufferedImage compressedImage = new BufferedImage(targetW, targetH, type);
			BufferedImage compressedImage = null;
			if (image.isAlphaPremultiplied()) {
				compressedImage = new BufferedImage(targetW, targetH, Transparency.TRANSLUCENT);
			} else {
				compressedImage = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB);
			}

			Graphics2D graph = compressedImage.createGraphics();
			graph.drawImage(cropedImage, 0, 0, targetW, targetH, null);
			//首先是背景有时变黑，这里最简单的做法是将最后的背景图片设置为白色。见下面代码：
			//graph.drawImage(cropedImage, 0, 0, targetW, targetH, null);
			graph.drawImage(cropedImage, 0, 0, targetW, targetH, Color.white, null);
			//压缩图片时，发现很多地方不光滑，这里也非常好解决。加入SCALE_SMOOTH参数
			//graph.drawImage(cropedImage, 0, 0, targetW, targetH, null);
			//graph.drawImage(cropedImage, 0, 0, targetW, targetH, Color.white,null);
			graph.drawImage(cropedImage.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH), 0, 0, Color.white, null);
			
			graph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f)); // 透明度设置开始
			graph.drawImage(cropedImage.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH), x, y, targetW, targetH, null);
			graph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER)); // 透明度设置
																						// 结束

			graph.dispose();

			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			ImageIO.write(compressedImage, "jpg", bytes);

			return bytes.toByteArray();
		} catch (IOException e) {
			// error
			return null;
		}
	}
	
	/**
	 * 
	 * @param filesrc
	 * @param logosrc
	 * @param outsrc
	 * @param x
	 *            位置
	 * @param y
	 *            位置
	 */
	public static void composePic(String filesrc, String logosrc, String outsrc, int x, int y) {
		try {
			File bgfile = new File(filesrc);
			Image bg_src = javax.imageio.ImageIO.read(bgfile);

			File logofile = new File(logosrc);
			Image logo_src = javax.imageio.ImageIO.read(logofile);

			int bg_width = bg_src.getWidth(null);
			int bg_height = bg_src.getHeight(null);
			int logo_width = logo_src.getWidth(null);
			;
			int logo_height = logo_src.getHeight(null);

			BufferedImage tag = new BufferedImage(bg_width, bg_height, Transparency.TRANSLUCENT);
			
			Graphics2D g2d = tag.createGraphics();
			g2d.drawImage(bg_src, 0, 0, bg_width, bg_height, null);
			// 设置透明度 
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.0f)); // 透明度设置开始，1.0f为透明度 ，值从0-1.0，依次变得不透明
			g2d.drawImage(logo_src, x, y, logo_width, logo_height, null);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER)); // 透明度设置
																					// 结束

			FileOutputStream out = new FileOutputStream(outsrc);
			ImageIO.write(tag, "png", out);
			
			
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//			encoder.encode(tag);
//			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main1(String args[]) {
		Long star = System.currentTimeMillis();
		composePic("c:\\bb.gif", "c:\\bc.gif", "c:\\out_pic.gif", 490, 360);
		Long end = System.currentTimeMillis();
		System.out.print("time====:" + (end - star));
	}

}
