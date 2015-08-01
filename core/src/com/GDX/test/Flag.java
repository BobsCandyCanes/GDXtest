package com.GDX.test;

import com.badlogic.gdx.graphics.g2d.Batch;

public class Flag extends Entity
{
	public Flag(float x, float y)
	{
		region = GDXtest.getTexture("flag");

		setX(x);
		setY(y);
	}

	@Override
	public void act(float deltaTime)
	{

	}

	@Override
	public void draw(Batch batch, float alpha)
	{
		if(batch != null)
		{
			batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(), -getRotation());
		}
	}
}
