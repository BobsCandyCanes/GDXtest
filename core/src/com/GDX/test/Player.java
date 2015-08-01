package com.GDX.test;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Player extends Ship
{	
	public Player(int x, int y)
	{
		super(x, y, "ship1");

		addListener(new InputListener() 
		{
			@Override
			public boolean keyDown (InputEvent event, int keycode) 
			{
				System.out.println("pressed");
				return false;
			}

			@Override
			public boolean keyUp (InputEvent event, int keycode) 
			{
				System.out.println("released");
				return false;
			}
		});
	}
	
	public void checkForCollision()
	{
		int objectLayerId = 2;
	
		Vector2 endpoint1 = new Vector2();
		Vector2 endpoint2 = new Vector2();
		
		float[] vertices;
		
		TiledMap tiledMap = Background.tiledMap;
		
		MapLayer collisionObjectLayer = tiledMap.getLayers().get(objectLayerId);
		MapObjects objects = collisionObjectLayer.getObjects();

		// Uses polylines instead of polygons for better performance
		for (PolylineMapObject polylineObject : objects.getByType(PolylineMapObject.class)) 
		{
		    Polyline polyline = polylineObject.getPolyline();
		    
		    vertices = polyline.getTransformedVertices();
		    
		    endpoint1.set(vertices[0], vertices[1]);
		    endpoint2.set(vertices[2], vertices[3]);
		    
		    if (Intersector.intersectSegmentPolygon(endpoint1, endpoint2, this.boundingPolygon))
		    {
		    	isColliding = true;
		    	
		    	moveBy(-xVelocity * 1.2f, -yVelocity * 1.2f);
		    	rotateBy(-rotationSpeed);
		    	
		      	xVelocity *= 0.05;
		      	yVelocity *= 0.05;
		    }
		}
		
		endpoint1 = null;
		endpoint2 = null;
		vertices = null;
	}
}
