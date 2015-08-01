package com.GDX.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;

	class Splash extends Entity
	{
		int age;
		float splashAlpha = 1;
		
		String imagePath = "data/splash.png";

		public Splash(float x, float y)
		{
			this(x, y, 5, 5);
		}
		
		public Splash(float x, float y, float w, float h)
		{
			setWidth(w);
			setHeight(h);
			
			setX(x - w / 2);
			setY(y - h / 2);

			setBounds(getX(), getY(), getWidth(), getHeight());
			
			setOrigin(getWidth() / 2, getHeight() / 2);  

			region = GDXtest.getTexture("splash");
			region.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		@Override
		public void act(float deltaTime)
		{			
			if(splashAlpha >= 0.05)
			{
				splashAlpha -= 0.05;
			}

			age++;
			
			setWidth(getWidth() + 2);
			setHeight(getHeight() + 2);
			
			setX(getX() - 1);
			setY(getY() - 1);
			
			if(age > 30)
			{
				destroy();
			}
		}

		@Override
		public void draw(Batch batch, float alpha)
		{	
			Color color = getColor();
	        batch.setColor(color.r, color.g, color.b, splashAlpha);
			
	        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
	            getWidth(), getHeight(), getScaleX(), getScaleY(), -getRotation());
	        
	        batch.setColor(color.r, color.g, color.b, 1);
			
	        
			drawChildren(batch, alpha);
		}
	}