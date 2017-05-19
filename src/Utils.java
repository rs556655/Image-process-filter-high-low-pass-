
public class Utils {
	public static int getR(int rgb) {return (rgb >> 16) & 0x000000FF;}
	public static int getG(int rgb) {return (rgb >>8 ) & 0x000000FF;}
	public static int getB(int rgb) {return (rgb) & 0x000000FF;}
	public static int getRGB(int r, int g, int b) {return 0xFF000000 | (r << 16) & 0x00FF0000 | (g << 8) & 0x0000FF00 | b & 0x000000FF;}

	public static int getGrayScale(int r, int g, int b) {
		return checkPixelBound((int) (0.2126*r+0.7152*g+0.0722*b));
	}
	
	public static int checkPixelBound(int s) {return s > 255 ? 255 : (s < 0 ? 0 : s);}
	public static double checkHSIConvertBound(double a) {return a < 0.0? 0.0:(a>1.0?1.0:a);}
	
	public static int [] getInterpolationColor(int [] color1, int [] color2, double percent) {
		int [] color = new int [3];
		for (int i = 0; i < 3; i++) {
			color[i] = checkPixelBound((int)(color1[i] + ( (color2[i] - color1[i]) * percent )));
		}
		return color;
	}
	
	// 為了效能而將整數與浮點完全分開，演算法一致 (浮點乘法非常消耗效能)
	public static int[][] multiply(int [][] a, int [][] b)  {
		int  c [][] = new int[a.length][b[0].length];
		for (int k=0; k<a.length; k++)
			for (int i=0; i<b.length; i++)
				for (int j=0; j<b[0].length; j++)
					c[k][j]+= a[k][i]*b[i][j];
	    return c;
	}
	public static double[][] multiply(double [][] a, double [][] b)  {
		double  c [][] = new double[a.length][b[0].length];
		for (int k=0; k<a.length; k++)
			for (int i=0; i<b.length; i++)
				for (int j=0; j<b[0].length; j++)
					c[k][j]+= a[k][i]*b[i][j];
	    return c;
	}
	
	public static int getHueFromRGB(int r, int g, int b) {
		double sumRGB = r + g + b;
		
		if (sumRGB == 0.0) return 0;
		
		double _r = ((double) r) / sumRGB;
		double _g = ((double) g) / sumRGB;
		double _b = ((double) b) / sumRGB;
		
		double value1 = 0.5 * ((_r - _g) + (_r - _b));
		double value2 = Math.sqrt((Math.pow(_r - _g, 2) + (_r - _b) * (_g - _b)));
		
		if (value2 == 0.0) return 0;
		
		double theta = (Math.acos(value1 / value2) * 180.0 / Math.PI);
		
		return (int) Math.round(((b > g) ? (360.0 - theta) : theta));
	}
	
	public static double getIntFromRGB(int r, int g, int b) {
		return checkHSIConvertBound(((double)( r + g + b )) / ( 3.0 * 255.0)) ;
	}
	
	public static double getSatFromRGB(int r, int g, int b) {
		double sumRGB = r + g + b;
		
		if (sumRGB == 0.0) return 0.0;
		
		double _r = ((double) r) / sumRGB;
		double _g = ((double) g) / sumRGB;
		double _b = ((double) b) / sumRGB;
		return checkHSIConvertBound(1.0 - 3.0 * Math.min(Math.min(_r,_g), _b));
	}

	public static int [] getRGBFromHSI(double h, double s, double i) {

		int sec = ((h<120)?0:((h>=240)?2:1));	
		h -= (sec==1)?120.0:(sec==2?240.0:0.0);
		
		double x = i * (1.0 - s);
		double y = i * (1.0 + ((s * Math.cos(Math.toRadians(h))) / (Math.cos(Math.toRadians(60.0 - h)))));
		double z = (3.0 * i) - (x + y);

		x *= 255;
		y *= 255;
		z *= 255;
		
		switch (sec) {
		case 0:return new int [] {
				checkPixelBound((int) Math.round(y)),
				checkPixelBound((int) Math.round(z)),
				checkPixelBound((int) Math.round(x))};
		case 1:return new int [] {
				checkPixelBound((int) Math.round(x)),
				checkPixelBound((int) Math.round(y)),
				checkPixelBound((int) Math.round(z))};
		case 2:return new int [] {
				checkPixelBound((int) Math.round(z)),
				checkPixelBound((int) Math.round(x)),
				checkPixelBound((int) Math.round(y))};
		default:return new int [] {255,255,255};
		}
	}
}
