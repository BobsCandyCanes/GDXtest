package com.GDX.test;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Background
{
	static TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	
	public Background()
	{
		tiledMap = new TmxMapLoader().load("data/Tilemaps/tilemap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
	}
	
	public void draw()
	{
		 tiledMapRenderer.setView((OrthographicCamera) GDXtest.stage.getCamera());
	     tiledMapRenderer.render();
	}	
	
	public void dispose()
	{
		tiledMap.dispose();
	}
}
