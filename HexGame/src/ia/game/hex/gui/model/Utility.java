package ia.game.hex.gui.model;


import java.awt.Font;
import java.net.MalformedURLException;
import java.net.URL;



public class Utility {

	public final static int DEFAULT_NUMBERS_OF_ROWS =  10;
	public final static int DEFAULT_NUMBERS_OF_COLUMNS = 10;
	public final static float XCENTER = 70; 						//coordinata x del centro del primo esagono
	public final static float YCENTER = 90; 						//coordinata y del centro del primo esagono
	public final static int DEFAULT_SIDE = 25;
	public final static int MARGIN = 2;
	public final static float ANGLE = (float) -Math.PI/8;			//angolo di rotazione degli esagoni
	private static Font font = null;
	public static int DEFAULT_GUI_WIDTH = 770;
	public static int DEFAULT_GUI_HEIGHT = 700;
	public static int PLAYER_HOR=0;
	public static int PLAYER_VERT=1;
	public static int PLAYER_NAME_RECT_HEIGHT=DEFAULT_GUI_HEIGHT-105;
	
	public static void createFont(){
		try {
			//_createFont();
		} catch (Exception e) {
			//do nothing
		}
	}
	
	
	private static void _createFont() throws Exception{
		URL fontUrl = null;
		if(font == null){
				fontUrl = new URL("http://www.webpagepublicity.com/" +
						"free-fonts/a/Airacobra%20Condensed.ttf");
				font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
		}
	}

	public static Font getFont(int fontSize){
		try {
			//_createFont();
			//font = font.deriveFont(Font.PLAIN,fontSize);
			font = new Font("Serif",Font.PLAIN,fontSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//font = new Font("Serif",Font.PLAIN,fontSize);
		}
			
		return font;
	}
	
	

	
}
