package org.magic.api.beans.technical;

import java.awt.Dimension;

public class PictureDimension {

	int width;
	int height;
	double zoom;
	double x;
	double y;
	
	public PictureDimension(int width, int height, double zoom, double x, double y) {
		this.width = width;
		this.height = height;
		this.zoom = zoom<1?1:zoom;
		this.x = x;
		this.y = y;
	}

	
	public Dimension getDimension()
	{
		return new Dimension(width,height);
	}



	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public double getZoom() {
		return zoom;
	}
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	
	
	
	
}
