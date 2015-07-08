package org.acm.seguin.uml.jpg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.acm.seguin.uml.UMLPackage;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import com.sun.image.codec.jpeg.*;

/**
 *  Description of the Class
 *
 *@author    Chris Seguin
 */
public class Save {
	private String filename;
	private UMLPackage diagram;


	/**
	 *  Constructor for the Save object
	 *
	 *@param  init            Description of Parameter
	 *@param  packageDiagram  Description of Parameter
	 */
	public Save(String init, UMLPackage packageDiagram) {
		filename = init;
		diagram = packageDiagram;
	}


	/**
	 *  Main processing method for the Save object
	 */
	public void run() {
		try {
			Dimension dim = diagram.getPreferredSize();
			BufferedImage doubleBuffer = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
			Graphics g = doubleBuffer.getGraphics();
			g.setColor(Color.gray);
			g.fillRect(0,0,dim.width,dim.height);
			diagram.print(g, 0, 0);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
			JPEGEncodeParam param = JPEGCodec.getDefaultJPEGEncodeParam(doubleBuffer);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out, param);
			encoder.encode(doubleBuffer);
			out.flush();
			out.close();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
