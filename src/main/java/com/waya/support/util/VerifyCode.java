package com.waya.support.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.imageio.ImageIO;

public class VerifyCode {
	
	private int imageWidth = 100;

	// 图片高度默认值
	private int imageHeight = 50;

	// 图片验证码长度
	private int codeLength = 4;

	// 噪点率（噪点分布的疏密）
	private float yawpRate = 0.012f;

	// 字体大小
	private int fontSize = 28;

	// 图片是否需要扭曲变形
	private boolean isDistortion = true;

	// 图片验证码
	private String imageCode = "";

	// 输出的图片格式
	private final String IMAGE_FORMAT = "jpg";

	// 0，1，2易和字母的o，l,z易混淆，不生成,可以生成汉字
	private char[] captchars = new char[] { '2', '3', '4', '5', '6',
			'7', '8', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',
			'n', 'p', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y' };

	/**
	 * 把生成的验证码图片写到指定目录文件中
	 * @param filePath 目录文件
	 * @throws IOException
	 */
	public void createImageFile(String filePath) throws IOException {
		OutputStream fos = new FileOutputStream(new File(filePath));
		BufferedImage bufferedImage = createImage(getImageWidth(), getImageHeight());
		ImageIO.write(bufferedImage, IMAGE_FORMAT, fos);
	}

	/**
	 * 把创建好的图片验证码放入输出流中
	 * @param os 输出流
	 * @return 生成的code
	 * @throws IOException
	 */
	public String createImage(OutputStream os) throws IOException {
		BufferedImage bufferedImage = createImage(getImageWidth(), getImageHeight());
		ImageIO.write(bufferedImage, IMAGE_FORMAT, os);
		return this.imageCode;
	}

	/**
	 * 创建指定宽高的验证码图片对象
	 * @param imageWidth 图片宽度
	 * @param imageHeight 图片高度
	 * @return BufferedImage 图片对象
	 */
	private BufferedImage createImage(int imageWidth, int imageHeight) {
		// 创建一个指定宽高并带索引的字节图像对象
		BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_INDEXED);
		// 创建一个二维图形对象以进行更复杂的图像绘制
		Graphics2D graphics2D = bufferedImage.createGraphics();
		// 设置背景色
		Color color = getRandomColor(200, 250);
		graphics2D.setColor(color);
		// 填充指定宽高的矩形，其中x,y坐标为0
		graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		// 加字符
		graphics2D.setColor(getRandomColor());
		drawString(graphics2D, getCodeLength());
		// drawCnString(graphics2D, getCodeLength());
		// 加一道线
		drawThickLine(graphics2D, 0, imageWidth / (2) + 1, imageWidth, imageHeight / (3) + 1, 1, getRandomColor(100, 200));
		// 加干扰短线
		setLTAttachLine(graphics2D, imageWidth, imageHeight);
		setRBAttachLine(graphics2D, imageWidth, imageHeight);
		// 使图片扭曲
		if (isDistortion) {
			shear(graphics2D, bufferedImage.getWidth(), bufferedImage.getHeight(), color);
		}
		// 设置噪点
		setYawp(bufferedImage, getYawpRate());
		return bufferedImage;
	}

	/**
	 * 在图片上画上字符
	 * 
	 * @param graphics2D
	 *            图形对象
	 * @param length
	 *            字符长度
	 */
	private void drawString(Graphics2D graphics2D, int length) {
		int car = captchars.length - 1;
		Random random = new Random();

		// 随机写字母或汉字
		String text = "";
		for (int i = 0; i < length; i++) {
			// 1.随机得到num个字母或数字
			text += captchars[random.nextInt(car) + 1];
		}
		this.imageCode = text;

		int xOffset = 1 + new Random().nextInt(40);
		int yOffset = 30;

		char[] charArray = text.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			graphics2D.setFont(getRandomFont());
			graphics2D.drawString(String.valueOf(charArray[i]), xOffset, yOffset);
			xOffset = xOffset + 14 + new Random().nextInt(5);
		}
	}

	@SuppressWarnings("unused")
	private void drawCnString(Graphics2D graphics2D, int length) {
		int xOffset = 1 + new Random().nextInt(40);
		int yOffset = 30;
		for (int i = 0; i < length; i++) {
			graphics2D.setFont(getRandomCnFont());
			graphics2D.drawString(String.valueOf(getRandomCnChar()), xOffset, yOffset);
			xOffset = xOffset + 20 + new Random().nextInt(5);
		}
	}

	/**
	 * 从左上到右下加上多道干扰线
	 */
	private void setLTAttachLine(Graphics2D graphics2D, int imageWidth, int imageHeight) {
		int lineLeft = 7;// 从左上到右下的线条个数
		Random random = new Random();
		for (int i = 0; i < lineLeft; i++) {
			int x = random.nextInt(imageWidth - 1);
			int y = random.nextInt(imageHeight - 1);
			int xl = random.nextInt(6) + 1;
			int yl = random.nextInt(12) + 1;
			graphics2D.drawLine(x, y, x + xl + 40, y + yl + 20);
		}
	}

	/**
	 * 从右上到左下加多道干扰线
	 */
	private void setRBAttachLine(Graphics2D graphics2D, int imageWidth, int imageHeight) {

		int lineRight = 8;// 从右上到左下的线条个数
		Random random = new Random();
		for (int i = 0; i < lineRight; i++) {
			int x = random.nextInt(imageWidth - 1);
			int y = random.nextInt(imageHeight - 1);
			int xl = random.nextInt(12) + 1;
			int yl = random.nextInt(6) + 1;
			graphics2D.drawLine(x, y, x - xl + 40, y - yl);
		}
	}

	/**
	 * 设置噪点
	 * 
	 * @param bufferedImage
	 *            图片对象
	 * @param yawpRate
	 *            噪声率
	 */
	private void setYawp(BufferedImage bufferedImage, float yawpRate) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		int area = (int) (yawpRate * width * height);
		Random random = new Random();
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int rgb = getRandomIntColor();
			bufferedImage.setRGB(x, y, rgb);
		}
	}

	private int getRandomIntColor() {
		int[] rgb = getRandomRgb();
		int color = 0;
		for (int c : rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}

	private int[] getRandomRgb() {
		int[] rgb = new int[3];
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

	/**
	 * 随机得到一个汉字的方法
	 * 
	 * @return char
	 */
	protected char getRandomCnChar() {
		final int minFirstByte = 176;
		final int maxFirstByte = 215;
		final int minSecondByte = 161;
		final int maxSecondByte = 249;

		byte[] b = new byte[2];
		b[0] = (byte) (getRandomIntBetween(minFirstByte, maxFirstByte));
		b[1] = (byte) (getRandomIntBetween(minSecondByte, maxSecondByte));
		try {
			String s = new String(b, "gb2312");
			System.out.println(s);
			assert s.length() == 1;
			return s.toCharArray()[0];
		} catch (UnsupportedEncodingException uee) {
			// 重试
			return getRandomCnChar();
		}
	}

	/**
	 * 获取两个整数之间的某个随机数，包含较小的整数，不包含较大的整数
	 * 
	 * @param first
	 *            第一个整数
	 * @param second
	 *            第二个整数
	 * @return
	 */
	private int getRandomIntBetween(int first, int second) {
		Random random = new Random();
		if (second < first) {
			int tmp = first;
			first = second;
			second = tmp;
		}
		return random.nextInt(second - first) + first;
	}

	/**
	 * 产生随机字体
	 */
	private Font getRandomFont() {
		Random random = new Random();
		Font[] font = new Font[5];
		int fontSize = getFontSize();
		font[0] = new Font("Algerian", Font.PLAIN, fontSize);
		font[1] = new Font("Antique Olive Compact", Font.PLAIN, fontSize);
		font[2] = new Font("Fixedsys", Font.PLAIN, fontSize);
		font[3] = new Font("Algerian", Font.PLAIN, fontSize);
		font[4] = new Font("Arial", Font.PLAIN, fontSize);
		return font[random.nextInt(5)];
	}

	/**
	 * 产生随机中文字体
	 */
	private Font getRandomCnFont() {
		Random random = new Random();
		Font[] font = new Font[5];
		int fontSize = getFontSize();
		font[0] = new Font("微软雅黑", Font.PLAIN, fontSize);
		font[1] = new Font("宋体", Font.PLAIN, fontSize);
		font[2] = new Font("新宋体", Font.PLAIN, fontSize);
		font[3] = new Font("楷体", Font.PLAIN, fontSize);
		font[4] = new Font("幼圆", Font.PLAIN, fontSize);
		return font[random.nextInt(5)];
	}

	/**
	 * 给定范围获得随机颜色
	 * 
	 * @param fc
	 *            颜色范围-小
	 * @param bc
	 *            颜色范围-大
	 * @return Color 随机颜色
	 */
	private Color getRandomColor(int fc, int bc) {
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		Random random = new Random();
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	private Color getRandomColor() {
		Random random = new Random();
		Color[] color = new Color[10];
		color[0] = new Color(32, 158, 25);
		color[1] = new Color(218, 42, 19);
		color[2] = new Color(31, 75, 208);
		return color[random.nextInt(3)];
	}

	// 画一道粗线的方法
	private void drawThickLine(Graphics g, int x1, int y1, int x2,
			int y2, int thickness, Color c) {

		// The thick line is in fact a filled polygon
		g.setColor(c);
		int dX = x2 - x1;
		int dY = y2 - y1;
		// line length
		double lineLength = Math.sqrt(dX * dX + dY * dY);

		double scale = (double) (thickness) / (2 * lineLength);

		// The x and y increments from an endpoint needed to create a
		// rectangle...
		double ddx = -scale * (double) dY;
		double ddy = scale * (double) dX;
		ddx += (ddx > 0) ? 0.5 : -0.5;
		ddy += (ddy > 0) ? 0.5 : -0.5;
		int dx = (int) ddx;
		int dy = (int) ddy;

		// Now we can compute the corner points...
		int xPoints[] = new int[4];
		int yPoints[] = new int[4];

		xPoints[0] = x1 + dx;
		yPoints[0] = y1 + dy;
		xPoints[1] = x1 - dx;
		yPoints[1] = y1 - dy;
		xPoints[2] = x2 - dx;
		yPoints[2] = y2 - dy;
		xPoints[3] = x2 + dx;
		yPoints[3] = y2 + dy;

		g.fillPolygon(xPoints, yPoints, 4);
	}

	private void shearX(Graphics g, int w1, int h1, Color color) {

		Random random = new Random();
		int period = random.nextInt(2);

		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);

		for (int i = 0; i < h1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
							+ (6.2831853071795862D * (double) phase)
							/ (double) frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}

	}

	private void shearY(Graphics g, int w1, int h1, Color color) {

		Random random = new Random();
		int period = random.nextInt(40) + 10; // 50;

		boolean borderGap = true;
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < w1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
							+ (6.2831853071795862D * (double) phase)
							/ (double) frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}

		}

	}

	/**
	 * 使图片扭曲
	 * 
	 * @param graphics
	 *            图形对象
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param color
	 *            颜色对象
	 */
	private void shear(Graphics graphics, int width, int height,
			Color color) {
		shearX(graphics, width, height, color);
		shearY(graphics, width, height, color);
	}

	public int getImageWidth() {
		return imageWidth;
	}

	/**
	 * 设置图片的宽度
	 * @param width 图片的宽度
	 * @return {@link VerifyCode}
	 */
	public VerifyCode width(int width) {
		if (width > 0) {
			this.imageWidth = width;
		}
		return this;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	/**
	 * 设置图片的高度
	 * @param height 图片的高度
	 * @return {@link VerifyCode}
	 */
	public VerifyCode height(int height) {
		if (height > 0) {
			this.imageHeight = height;
		}
		return this;
	}

	public int getCodeLength() {
		return codeLength;
	}

	/**
	 * 设置字符的长度
	 * @param length 字符的长度
	 * @return {@link VerifyCode}
	 */
	public VerifyCode length(int length) {
		if (length > 0) {
			this.codeLength = length;
		}
		return this;
	}

	public float getYawpRate() {
		return yawpRate;
	}

	/**
	 * 设置噪点密度
	 * @param yawpRate 噪点密度
	 * @return {@link VerifyCode}
	 */
	public VerifyCode yawpRate(float yawpRate) {
		this.yawpRate = yawpRate;
		return this;
	}

	public int getFontSize() {
		return fontSize;
	}

	/**
	 * 设置字体大小
	 * @param fontSize 字体大小
	 * @return {@link VerifyCode}
	 */
	public VerifyCode fontSize(int fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public boolean isDistortion() {
		return isDistortion;
	}

	/**
	 * 设置是否需要扭曲变形
	 * @param distortion 是否需要扭曲变形
	 * @return {@link VerifyCode}
	 */
	public VerifyCode distortion(boolean distortion) {
		this.isDistortion = distortion;
		return this;
	}
}
