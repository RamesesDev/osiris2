import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import com.keypoint.*;

/**
 * TestSimple creates a PNG file that shows an analog clock
 * displaying the current time of day.
 *
 * This test program was written in a burning hurry, so
 * it is not a model of efficient, or even good, code.
 * Comments and bug fixes should be directed to:
 *
 *   david@catcode.com
 *
 * @author J. David Eisenberg
 * @version 1.0, 10 Nov 1999
 */
public class TestSimple extends Frame
{
	String		message;
	String		timeStr;
	Image		clockImage = null;
	int			hour, minute;

	public TestSimple(String s)
	{
		super(s);
		setSize(200,200);
	}

	public void drawClockImage(int hour, int minute)
	{
		// variables used for drawing hands of clock
		Graphics g;
		Font smallFont = new Font("Helvetica", Font.PLAIN, 9 );
		FontMetrics fm;

		int x0, y0, x1, y1;
		double angle;

		g = clockImage.getGraphics();
		g.setFont( smallFont );
		fm = g.getFontMetrics();

		// draw the clock face; yellow for AM, blue for PM
		if (hour < 12)
		{
			g.setColor( new Color( 255, 255, 192 ) );
		}
		else
		{
			g.setColor( new Color( 192, 192, 255) );
		}
		g.fillOval(10, 10, 80, 80);
		g.setColor( Color.black );
		g.drawOval(10, 10, 80, 80);
		g.drawOval( 48, 48, 4, 4);

		/* draw the 12 / 3 / 6/ 9 numerals */
		g.setFont( smallFont );
		g.drawString( "12", 50-fm.stringWidth("12")/2, 11+fm.getAscent() );
		g.drawString( "3", 88-fm.stringWidth("3"), 50+fm.getAscent()/2 );
		g.drawString( "6", 50-fm.stringWidth("6")/2, 88 );
		g.drawString( "9", 12, 50+fm.getAscent()/2 );

		x0 = 50;
		y0 = 50;

		/* draw the hour hand */
		hour %= 12;
		angle = -(hour * 30 + minute/2) + 90;
		angle = angle * Math.PI / 180.0;
		x1 = (int) (x0 + 28 * (Math.cos( angle ) ));
		y1 = (int) (y0 - 28 * (Math.sin( angle ) ));
		g.drawLine( x0, y0, x1, y1 );

		/* and the minute hand */
		angle = -(minute * 6) + 90;
		angle = angle * Math.PI / 180.0;
		x1 = (int) (x0 + 35 * (Math.cos( angle ) ));
		y1 = (int) (y0 - 35 * (Math.sin( angle ) ));
		g.drawLine( x0, y0, x1, y1 );
	}

	public void paint( Graphics g )
	{
		if (clockImage == null)
		{
			clockImage = createImage(100, 100);
			drawClockImage(hour, minute);
			saveClockImage();
		}
		g.drawImage( clockImage, 50, 20, null );
		if (message != null)
		{
			g.drawString( message, 10, 140 );
		}
	}

	public static void main(String[] args)
	{
		TestSimple te = new TestSimple("Test PNG Encoder");
		te.do_your_thing();
	}

	public void do_your_thing()
	{
		Image	img;

		// The resultant PNG data will go into this array...

		Calendar cal = Calendar.getInstance();

		hour = cal.get(Calendar.HOUR);
		if (cal.get(Calendar.AM_PM) == 1)
		{
			hour += 12;
		}
		hour %= 24;
		minute = cal.get(Calendar.MINUTE);

		/*
		 * format the time to a string of the form
		 *    hhmm
		 * for use in the filename
		 */

		timeStr = Integer.toString( minute );
		if (minute < 10)
		{
			timeStr = "0" + timeStr;
		}
		timeStr = Integer.toString( hour ) + timeStr;
		if (hour < 10)
		{
			timeStr = "0" + timeStr;
		}
		message =  "File name: clock" + timeStr + ".png";

		WindowListener l = new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                        System.exit(0);
                       }
        };
        addWindowListener(l);
		show();
	}

	public void saveClockImage()
	{
		byte[] pngbytes;
		PngEncoder png =  new PngEncoder( clockImage );

		try
		{
			FileOutputStream outfile = new FileOutputStream( "clock" + timeStr + ".png" );
			pngbytes = png.pngEncode();
			if (pngbytes == null)
			{
				System.out.println("Null image");
			}
			else
			{
				outfile.write( pngbytes );
			}
			outfile.flush();
			outfile.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
