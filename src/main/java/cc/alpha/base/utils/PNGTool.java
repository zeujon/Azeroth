package cc.alpha.base.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;

import javax.imageio.ImageIO;

public class PNGTool {
	
	public static void main(String[] args) throws ClassNotFoundException, ParseException, IOException {

		Long star = System.currentTimeMillis();
		composePic("c:/1/yuying_logo.png", "c:/1/yuying_logo.png", "c:/1/Sayuying_logo.png", 350, 350);
		Long end = System.currentTimeMillis();
		System.out.print("time====:" + (end - star));
		
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
			//JPG必须用BufferedImage.TYPE_INT_RGB,有Alpha通道的图片需要用BufferedImage.TRANSLUCENT
			//BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
			
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
}
