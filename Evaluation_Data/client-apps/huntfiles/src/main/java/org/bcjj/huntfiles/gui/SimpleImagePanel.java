package org.bcjj.huntfiles.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SimpleImagePanel extends JPanel{

	private static final long serialVersionUID = -766105583985986596L;
	private BufferedImage image=null;

    public SimpleImagePanel() {
    }

    public void setImageInputStream(InputStream inputStream) throws Exception {
    	image=null;
    	if (inputStream==null) {
    		this.repaint();
    		return;
    	}
    	try {
    		image=ImageIO.read(inputStream);
    		this.repaint();
    	} catch (Exception e) {
    		this.repaint();
    		throw e;
		}
    	
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.drawImage(image, 0, 0, this); 
        if (image==null) {
        	//
        } else {
        
	        image.getHeight();
	        image.getWidth();
	        
	        double x=(1d*this.getWidth()/image.getWidth());
	        double y=(1d*this.getHeight()/image.getHeight());
	        double t=Math.min(x, y);
	        
	        
	        g.drawImage(image, 0, 0, (int)Math.round(t*image.getWidth()), (int)Math.round(t*image.getHeight()),null);      
        }
    }

}