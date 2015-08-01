package com.GDX.test;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Turret extends Entity
{	
	final static int DFLT_WIDTH = 11;
	final static int DFLT_HEIGHT = 16;

	final static int DFLT_ORIGIN_X = 5;
	final static int DFLT_ORIGIN_Y = 5;

	static int fireDelay = 50;

	//Distance from the CENTER of the ship to the CENTER of the turret
	float xOffset;
	float yOffset;

	float angle = 0;

	double degreesOfFreedom = 90;

	double targetRotation;

	float rotationSpeed;

	int timeSinceLastShot;

	BarrelExit[] barrelExits;

	public Turret(Ship s, int x, int y, String name)
	{
		loadFromFile(name);
		
		setX(x - getWidth() / 2);
		setY(y - getHeight() / 2);
		
		xOffset = x;
		yOffset = y;

		//barrelExits = new BarrelExit[1];
		//barrelExits[0] = new BarrelExit(0, 8, this);
	}
	
	public void loadFromFile(String filename)
	{
		FileHandle file = Gdx.files.internal("data/Turrets/" + filename + ".turret");
		String inputString = file.readString();
		String[] input = inputString.split("\\r?\\n");
		
		setTexture(input[1]);
		setWidth(Integer.parseInt(input[3]));
		setHeight(Integer.parseInt(input[5]));
		setOriginX(Integer.parseInt(input[7]));
		setOriginY(Integer.parseInt(input[9]));
		fireDelay = Integer.parseInt(input[11]);
		
		int numBarrelExits = Integer.parseInt(input[14]);
		
		barrelExits = new BarrelExit[numBarrelExits];
		
		
		int numTurrets = Integer.parseInt(input[14]);
		int lineNumber = 14;
		
		int exitX;
		int exitY;
		
		for(int i = 0; i < numTurrets; i++)
		{
			exitX = Integer.parseInt(input[lineNumber + 2]);
			exitY = Integer.parseInt(input[lineNumber + 4]);
			
			barrelExits[i] = new BarrelExit(exitX, exitY, this);
			
			lineNumber += 4;
		}
	}

	@Override
	public void act(float deltaTime)
	{
		timeSinceLastShot++;

		updatePosition();
		
		if(getParent().equals(GDXtest.player))
		{
			pointTowardsMouse();
		}

		setRotation(getRotation() + rotationSpeed);

		updateBarrelExits();
		
		for(Actor a : getChildren())
		{
			a.act(deltaTime);
		}	
	}

	@Override
	public void draw(Batch batch, float alpha)
	{
		batch.draw(region, getParent().getX() + getX(), getParent().getY() + getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

		drawChildren(batch, alpha);
	}

	public void updatePosition()
	{
		double rotationInRadians = Math.toRadians(getParent().getRotation());

		float sinTheta = (float)Math.sin(rotationInRadians);
		float cosTheta = (float)Math.cos(rotationInRadians);

		//APPLY ROTATION
		float newX2 = xOffset * cosTheta + yOffset * sinTheta;
		float newY2 = -xOffset * sinTheta + yOffset * cosTheta;

		setX((float)(newX2 + getParent().getOriginX() - getOriginX()));
		setY((float)(newY2 + getParent().getOriginY() - getOriginY()));
	}

	public void pointTowardsMouse()
	{
		Vector2 mouseLocation = GDXtest.stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

		Vector3 turretCenter = new Vector3(getParent().getX() + getX() + getOriginX(), getParent().getY() + getY() + getOriginY(), 0);

		double xDiff = mouseLocation.x - turretCenter.x;
		double yDiff = mouseLocation.y - turretCenter.y;

		double angleToMouse = Math.atan2(yDiff, -xDiff);

		setRotation(90 - (float)Math.toDegrees(angleToMouse));
	}

	public void updateBarrelExits()
	{
		for(BarrelExit barrelExit : barrelExits)
		{
			barrelExit.update();
		}
	}

	public void shoot()
	{
		if(timeSinceLastShot > fireDelay)
		{
			for(BarrelExit barrelExit : barrelExits)
			{
				float xPos = getParent().getX() + getX() + getOriginX() + barrelExit.transformedX;
				float yPos = getParent().getY() + getY() + getOriginY() + barrelExit.transformedY;
				
				GDXtest.stage.addActor(new Projectile(xPos, yPos, -getRotation(), (Ship)getParent()));
			}
			timeSinceLastShot = 0;
		}
	}
}
