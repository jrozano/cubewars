package com.cubewars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

	/**
	 * Contructor.
	 * 
	 * @param controller The Game controller used in this game.
	 */
	public ScreenController (GameController controller)
	{
		this.controller = controller;
		this.batch = new SpriteBatch ();
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

		/* Dibujamos en pantalla todos los elementos que haya en el contenedor del controller. */
		batch.begin ();
		
		controller.tick();

		for (GameObject g : controller.getDrawingContainer ())
			batch.draw (g.getTexture (), g.area.x, g.area.y);

		batch.end ();
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
		batch.dispose ();
	}
}
