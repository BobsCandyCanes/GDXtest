package com.GDX.test.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.GDX.test.GDXtest;

public class DesktopLauncher 
{
	public static void main (String[] arg) 
	{
		System.out.println("Launching");
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Test";
		config.width = 1200;
		config.height = 700;
		
		new LwjglApplication(new GDXtest(), config);
	}
}
