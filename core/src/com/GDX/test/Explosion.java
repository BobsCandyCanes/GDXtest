package com.GDX.test;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;

	class Explosion extends Entity
	{
		int age;
		float explosionAlpha = 1;
		
		PointLight light;
		
		public Explosion(float x, float y)
		{
			this(x, y, 5, 5);
		}
		
		public Explosion(float x, float y, float w, float h)
		{
			super(x, y, w, h);

			setTexture("explosion");
			
			initLight();
		}
		
		public void initLight()
		{
			light = new PointLight(GDXtest.rayHandler, 100, new Color(1.0f, 0.0f, 0.0f, 0.8f), 50, 0, 0);	
		}

		@Override
		public void act(float deltaTime)
		{			
			if(explosionAlpha >= 0.02)
			{
				explosionAlpha -= 0.02;
			}

			age++;
			
			setWidth(getWidth() + 4);
			setHeight(getHeight() + 4);
			
			setOriginX(getWidth() / 2);
			setOriginY(getHeight() / 2);
			
			setX(getX() - 2);
			setY(getY() - 2);
			
			updateLights();
			
			if(age > 10)
			{
				destroy();
			}
		}
		
		public void updateLights()
		{
			Camera camera = GDXtest.stage.getCamera();

			Vector3 lightLocation = camera.project(new Vector3(getX() + getOriginX(), getY() + getOriginY(), 0)
			, GDXtest.stage.getViewport().getScreenX(), GDXtest.stage.getViewport().getScreenY(), camera.viewportWidth, camera.viewportHeight);

			light.setPosition(lightLocation.x, lightLocation.y);
		}

		@Override
		public void draw(Batch batch, float alpha)
		{	
			Color color = getColor();
	        batch.setColor(color.r, color.g, color.b, explosionAlpha);
			
	        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
	            getWidth(), getHeight(), getScaleX(), getScaleY(), -getRotation());
	        
	        batch.setColor(color.r, color.g, color.b, 1);
			
	        
			drawChildren(batch, alpha);
		}
		
		public void destroy()
		{
			light.remove();
			light.dispose();
			super.destroy();
		}
	}