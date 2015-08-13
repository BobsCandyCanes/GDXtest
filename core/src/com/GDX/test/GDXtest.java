package com.GDX.test;

import java.util.ArrayList;

import box2dLight.DirectionalLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GDXtest extends ApplicationAdapter
{
	static TextureAtlas spritesheet;

	static int windAngle = 90;

	static int worldWidth = 9600;
	static int worldHeight = 9600;

	static Stage stage;

	static int playerNum;

	ArrayList<Entity> entities;

	Background background;

	static ShapeRenderer shapeRenderer;	

	World world;
	static RayHandler rayHandler;

	GUI gui;

	static Ship ship1;
	static Ship ship2;
	static Ship ship3;

	@Override
	public void create()
	{
		shapeRenderer = new ShapeRenderer();

		spritesheet = new TextureAtlas(Gdx.files.internal("data/Spritesheets/spritesheet.pack"));

		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		gui = new GUI(stage.getBatch());

		Gdx.input.setInputProcessor(gui);

		background = new Background();

		initLighting();

		ship1 = new Ship(1000, 1000, "ship1");
		stage.addActor(ship1);
		playerNum = 1;

		ship2 = new Ship(1000, 1200, "ship2");
		ship2.setRotation(60);
		stage.addActor(ship2);

		ship3 = new Ship(1200, 1200, "ship3");
		stage.addActor(ship3);
	}

	public void initLighting()
	{
		world = new World(new Vector2(0,0),false);
		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(stage.getCamera().combined);

		DirectionalLight sun = new DirectionalLight(rayHandler, 50, new Color(0.0f, 0.1f, 0.0f, 0.25f), -90);
	}

	@Override
	public void dispose()
	{
		stage.dispose();
		background.dispose();
		world.dispose();
		gui.dispose();
	}

	@Override
	public void resize(int width, int height) 
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(getPlayer() != null)
		{
			stage.getCamera().position.set(getPlayer().getX() + getPlayer().getOriginX(), getPlayer().getY() + getPlayer().getOriginY(), 0);
		}
		stage.getCamera().update();

		pollInput();

		stage.act(Gdx.graphics.getDeltaTime());
		gui.act(Gdx.graphics.getDeltaTime());

		entities = new ArrayList<Entity>();

		background.draw();
		stage.draw();

		rayHandler.updateAndRender();

		gui.draw();
	}

	public void pollInput()
	{
		Ship player = getPlayer();
		
		if(player != null)
		{
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
					|| Gdx.input.isKeyPressed(Input.Keys.D))
			{
				if(!player.isColliding)
				{
					float turnSpeed = 0.1f + player.turnSpeed * player.getVelocity();

					player.rotationSpeed += turnSpeed;
				}
			}
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
					|| Gdx.input.isKeyPressed(Input.Keys.A))
			{
				if(!player.isColliding)
				{
					float turnSpeed = 0.1f + player.turnSpeed * player.getVelocity();

					player.rotationSpeed += -turnSpeed;
				}
			}
			if (Gdx.input.isKeyPressed(Input.Keys.UP)
					|| Gdx.input.isKeyPressed(Input.Keys.W))
			{
				float xVector = player.speed * (float)Math.sin(Math.toRadians(player.getRotation()));
				float yVector = player.speed * (float)Math.cos(Math.toRadians(player.getRotation()));

				player.xVelocity += xVector;
				player.yVelocity += yVector;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
					|| Gdx.input.isKeyPressed(Input.Keys.S))
			{
				float xVector = player.speed * (float)Math.sin(Math.toRadians(player.getRotation()));
				float yVector = player.speed * (float)Math.cos(Math.toRadians(player.getRotation()));

				player.xVelocity -= xVector;
				player.yVelocity -= yVector;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
			{
				player.shoot();
			}
		}
	}

	public static TextureRegion getTexture(String name)
	{
		return spritesheet.findRegion(name);
	}

	public static Ship getPlayer()
	{
		switch(playerNum)
		{
		case 1: 
			return ship1;
		case 2:
			return ship2;
		case 3:
			return ship3;
		default:
			return null;
		}
	}
}
