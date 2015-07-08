package org.acm.seguin.tools.install;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;


/**
 *  A class that will divide different features
 *
 *@author     Chris Seguin
 *@created    September 17, 2002
 */
public class Divider extends JComponent
{
    /**
     *  Constructor for the Divider object
     */
    public Divider()
    {
        setMinimumSize(new Dimension(100, 2));
        setPreferredSize(new Dimension(100, 2));
    }

    public void adjustSize(int width)
    {
        setMinimumSize(new Dimension(width, 2));
        setPreferredSize(new Dimension(width, 2));
    }


    /**
     *  Description of the Method
     *
     *@param  g  Description of the Parameter
     */
    public void paint(Graphics g)
    {
        Dimension size = getSize();
        g.setColor(Color.black);
        g.fillRect(0, 0, size.width, size.height);
    }
}
