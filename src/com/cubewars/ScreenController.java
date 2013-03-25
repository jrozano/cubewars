package com.cubewars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Crea un controlador de pantalla. ScreenController es la única clase con acceso al dispositivo de
 * pantalla, y se comunica con el controlador del juego para obtener la lista de elementos que debe
 * dibujar.
 * 
 * @author pyrosphere3
 * 
 */
public class ScreenController implements Screen
{
	private GameController controlador;
	private SpriteBatch batch;

	/**
	 * Contructor de controlador de pantalla. Crea un controlador encargado de dibujar las texturas
	 * del juego en pantalla.
	 */
	public ScreenController (GameController controlador)
	{
		this.controlador = controlador;
		this.batch = new SpriteBatch ();
	}

	@Override
	public void show ()
	{
	}

	@Override
	public void render (float delta)
	{
		Gdx.gl.glClearColor (0, 0, 0, 1);
		Gdx.gl.glClear (GL10.GL_COLOR_BUFFER_BIT);

		/* Dibujamos en pantalla todos los elementos que haya en el contenedor del controlador. */
		batch.begin ();

		for (GameObject g : controlador.getDrawingContainer ())
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
