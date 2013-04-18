package com.cubewars;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * A controller used to draw objects into the screen.
 * 
 * ScreenController should be the only class with direct access to the screen, and is used by the
 * game controller to render entities onto the screen.
 * 
 * @author pyrosphere3
 * 
 */
public class ScreenController implements Screen
{
	private GameController controller;
	private SpriteBatch batch;
	private ShapeRenderer gridLines;
	private BufferedWriter output;
	private double elapsedTime;
	private double interval = 0;

	/**
	 * Contructor.
	 * 
	 * @param controller The Game controller used in this game.
	 */
	public ScreenController (GameController controller)
	{
		try
		{

			this.controller = controller;
			this.batch = new SpriteBatch ();
			this.gridLines = new ShapeRenderer ();
			this.elapsedTime = 0;

			/* FIXME Only valid for desktop. */
			FileWriter fw = new FileWriter ("metrics/frames.dat", false);
			output = new BufferedWriter (fw);

		} catch (Exception e)
		{
			e.printStackTrace ();
		}
	}

	@Override
	public void show ()
	{
	}

	/**
	 * The rendering loop.
	 */
	@Override
	public void render (float delta)
	{
		Gdx.gl.glClearColor (0, 0, 0, 1);
		Gdx.gl.glClear (GL10.GL_COLOR_BUFFER_BIT);

		controller.tick ();

		/* Draw grid lines. */
		gridLines.begin (ShapeType.Line);

		/* Horizontal lines: one line for each 80 pixels. */
		gridLines.setColor (1, 1, 0, 1);
		for (int i = 0; i < Gdx.graphics.getHeight () / 80; ++i)
			gridLines.line (0, i * 80, 1280, i * 80);

		/* Vertical lines: one line for each 128 pixels. */
		for (int i = 0; i < Gdx.graphics.getWidth () / 128; ++i)
			gridLines.line (i * 128, 0, i * 128, 800);

		gridLines.end ();

		/* Dibujamos en pantalla todos los elementos que haya en el contenedor del controller. */
		batch.begin ();

		for (GameObject g : controller.getDrawingContainer ())
		{
			batch.draw (g.getTexture (), g.area.x, g.area.y, g.area.width, g.area.height);
		}

		batch.end ();
		
		elapsedTime += delta;
		interval += delta;

		if (interval > 1)
		{
			interval = 0;
			try
			{
				output.write (elapsedTime + "\t" + Gdx.graphics.getFramesPerSecond ());
				output.newLine ();
				output.flush ();
			} catch (IOException e)
			{
				e.printStackTrace ();
			}
		}
	}

	@Override
	public void resize (int width, int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void hide ()
	{

	}

	@Override
	public void pause ()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume ()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose ()
	{
		try
		{
			batch.dispose ();
			output.close ();
			System.out.println ("CERRANDO");
		} catch (IOException e)
		{
			e.printStackTrace ();
		}
	}
}
