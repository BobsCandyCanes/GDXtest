package com.GDX.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GUI extends Stage
{
	static TextureAtlas HUD_Assets;

	Table table;

	Skin skin;
	TextButton button1;
	TextButton button2;
	TextButton button3;

	Image hud;
	Image healthBar;
	Image healthBarBackground;

	float healthBarMaxHeight;
	
	public GUI(Batch batch)
	{	
		super(new ScreenViewport(), batch);

		HUD_Assets = new TextureAtlas(Gdx.files.internal("data/GUI_Assets/HUD_Assets/HUD_Assets.pack"));

		table = new Table();
		table.setFillParent(true);
		addActor(table);

		skin = new Skin(Gdx.files.internal("data/GUI_Assets/uiskin.json"));

		initButtons();
		initHUD();
	}

	public void initButtons()
	{
		button1 = new TextButton("Ship 1", skin, "default");
		button1.setWidth(150);
		button1.setHeight(40);
		button1.setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, 0);

		button1.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				if(GDXtest.ship1 != null)
				{
					GDXtest.playerNum = 1;
				}
			}
		});

		table.addActor(button1);

		button2 = new TextButton("Ship 2", skin, "default");
		button2.setWidth(150);
		button2.setHeight(40);
		button2.setPosition(button1.getX() + button1.getWidth(), 0);

		button2.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				if(GDXtest.ship2 != null)
				{
					GDXtest.playerNum = 2;
				}
			}
		});

		table.addActor(button2);
		
		button3 = new TextButton("Ship 3", skin, "default");
		button3.setWidth(150);
		button3.setHeight(40);
		button3.setPosition(button2.getX() + button3.getWidth(), 0);

		button3.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				if(GDXtest.ship3 != null)
				{
					GDXtest.playerNum = 3;
				}
			}
		});

		table.addActor(button3);
	}

	public void initHUD()
	{
		hud = new Image(HUD_Assets.findRegion("HUD"));
		healthBar = new Image(HUD_Assets.findRegion("healthbar"));
		healthBarBackground = new Image(HUD_Assets.findRegion("healthbarBackground"));

		healthBarBackground.setPosition(Gdx.graphics.getWidth() - 59, 110);
		table.addActor(healthBarBackground);
		
		healthBarMaxHeight = healthBar.getHeight();
		System.out.println(healthBarMaxHeight);
		healthBar.setPosition(Gdx.graphics.getWidth() - 59, 110);
		table.addActor(healthBar);
		
		hud.setPosition(Gdx.graphics.getWidth() - 100, 25);
		table.addActor(hud);
	}

	public void act(float deltaTime)
	{
		super.act(deltaTime);

		updateHealthBar();
	}

	public void updateHealthBar()
	{
		if(GDXtest.getPlayer() != null)
		{			
			healthBar.setHeight((float)GDXtest.getPlayer().health / GDXtest.getPlayer().maxHealth * healthBarMaxHeight);
		}
		else
		{
			healthBar.setHeight(0);
		}
	}
}
