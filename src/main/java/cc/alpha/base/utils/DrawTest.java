package cc.alpha.base.utils;
/**
   @version 1.31 2004-05-03
   @author Cay Horstmann
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawTest {
	public static void main(String[] args) {
		DrawFrame frame = new DrawFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

/**
 * A frame that contains a panel with drawings
 */
class DrawFrame extends JFrame {
	public DrawFrame() {
		setTitle("DrawTest");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		// add panel to frame
		DrawPanel panel = new DrawPanel();
		panel.setBackground(Color.WHITE);
		setLocationRelativeTo(null);
		add(panel, BorderLayout.CENTER);
	}

	public static final int DEFAULT_WIDTH = 320;
	public static final int DEFAULT_HEIGHT = 340;
}

/**
 * A panel that displays rectangles and ellipses.
 */
class DrawPanel extends JPanel {
	private String message = "公章测试有限公司";

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.RED);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 绘制圆
		int radius = 150;
		Ellipse2D circle = new Ellipse2D.Double();
		circle.setFrameFromCenter(CENTERX, CENTERY, CENTERX + radius, CENTERY + radius);
		g2.draw(circle);

		// 绘制中间的五角星
		Font starFont = new Font("宋体", Font.BOLD, 120);
		g2.setFont(starFont);
		g2.drawString("★", CENTERX - 60, CENTERY + 40);

		// 根据输入字符串得到字符数组
		String[] messages2 = message.split("", 0);
		String[] messages = new String[messages2.length - 1];
		System.arraycopy(messages2, 1, messages, 0, messages2.length - 1);

		// 输入的字数
		int ilength = messages.length;

		// 设置字体属性
		int fontsize = 40;
		Font f = new Font("Serif", Font.BOLD, fontsize);

		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = f.getStringBounds(message, context);

		// 字符宽度＝字符串长度/字符数
		double char_interval = (bounds.getWidth() / ilength);
		// 上坡度
		double ascent = -bounds.getY();

		int first = 0, second = 0;
		boolean odd = false;
		if (ilength % 2 == 1) {
			first = (ilength - 1) / 2;
			odd = true;
		} else {
			first = (ilength) / 2 - 1;
			second = (ilength) / 2;
			odd = false;
		}

		double radius2 = radius - ascent;
		double x0 = CENTERX;
		double y0 = CENTERY - radius + ascent;
		// 旋转角度
		double a = 2 * Math.asin(char_interval / (2 * radius2));

		if (odd) {
			g2.setFont(f);
			g2.drawString(messages[first], (float) (x0 - char_interval / 2), (float) y0);

			// 中心点的右边
			for (int i = first + 1; i < ilength; i++) {
				double aa = (i - first) * a;
				double ax = radius2 * Math.sin(aa);
				double ay = radius2 - radius2 * Math.cos(aa);
				AffineTransform transform = AffineTransform.getRotateInstance(aa);// ,x0
																					// +
																					// ax,
																					// y0
																					// +
																					// ay);
				Font f2 = f.deriveFont(transform);
				g2.setFont(f2);
				g2.drawString(messages[i], (float) (x0 + ax - char_interval / 2 * Math.cos(aa)), (float) (y0 + ay - char_interval / 2 * Math.sin(aa)));
			}
			// 中心点的左边
			for (int i = first - 1; i > -1; i--) {
				double aa = (first - i) * a;
				double ax = radius2 * Math.sin(aa);
				double ay = radius2 - radius2 * Math.cos(aa);
				AffineTransform transform = AffineTransform.getRotateInstance(-aa);// ,x0
																					// +
																					// ax,
																					// y0
																					// +
																					// ay);
				Font f2 = f.deriveFont(transform);
				g2.setFont(f2);
				g2.drawString(messages[i], (float) (x0 - ax - char_interval / 2 * Math.cos(aa)), (float) (y0 + ay + char_interval / 2 * Math.sin(aa)));
			}

		} else {
			// 中心点的右边
			for (int i = second; i < ilength; i++) {
				double aa = (i - second + 0.5) * a;
				double ax = radius2 * Math.sin(aa);
				double ay = radius2 - radius2 * Math.cos(aa);
				AffineTransform transform = AffineTransform.getRotateInstance(aa);// ,x0
																					// +
																					// ax,
																					// y0
																					// +
																					// ay);
				Font f2 = f.deriveFont(transform);
				g2.setFont(f2);
				g2.drawString(messages[i], (float) (x0 + ax - char_interval / 2 * Math.cos(aa)), (float) (y0 + ay - char_interval / 2 * Math.sin(aa)));
			}

			// 中心点的左边
			for (int i = first; i > -1; i--) {
				double aa = (first - i + 0.5) * a;
				double ax = radius2 * Math.sin(aa);
				double ay = radius2 - radius2 * Math.cos(aa);
				AffineTransform transform = AffineTransform.getRotateInstance(-aa);// ,x0
																					// +
																					// ax,
																					// y0
																					// +
																					// ay);
				Font f2 = f.deriveFont(transform);
				g2.setFont(f2);
				g2.drawString(messages[i], (float) (x0 - ax - char_interval / 2 * Math.cos(aa)), (float) (y0 + ay + char_interval / 2 * Math.sin(aa)));
			}
		}

	}

	public static final int CENTERX = 150;
	public static final int CENTERY = 150;
}
