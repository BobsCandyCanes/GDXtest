package com.GDX.test;

public class BarrelExit
{
	float xOffset;
	float yOffset;
	
	float transformedX = 0;
	float transformedY = 0;
	
	Turret source;
	
	public BarrelExit(float x, float y, Turret source)
	{
		xOffset = x;
		yOffset = y;
		
		transformedX = x;
		transformedY = y;
		
		this.source = source;
	}
	
	public void update()
	{		
		double rotationInRadians = Math.toRadians(-source.getRotation());

		float sinTheta = (float)Math.sin(rotationInRadians);
		float cosTheta = (float)Math.cos(rotationInRadians);

		//APPLY ROTATION
		transformedX = xOffset * cosTheta + yOffset * sinTheta;
		transformedY = -xOffset * sinTheta + yOffset * cosTheta;
	}
}
