package com.GDX.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GUI extends Stage
{
	Skin skin;
	TextButton button1;
	TextButton button2;
	TextButton button3;
	
	public GUI(Batch batch)
	{	
		super(new ScreenViewport(), batch);
		
		skin = new Skin(Gdx.files.internal("data/GUI_Assets/uiskin.json"));
		
		initButtons();
	}
	
	public void initButtons()
	{
		button1 = new TextButton("Ship 1", skin, "default");
	    button1.setWidth(200);
	    button1.setHeight(50);
	    button1.setPosition(100, 10);
	    addActor(button1);
	    
		button2 = new TextButton("Ship 2", skin, "default");
	    button2.setWidth(200);
	    button2.setHeight(50);
	    button2.setPosition(400, 10);
	    addActor(button2);
	    
		button3 = new TextButton("Ship 3", skin, "default");
	    button3.setWidth(200);
	    button3.setHeight(50);
	    button3.setPosition(700, 10);
	    addActor(button3);
	}
}
