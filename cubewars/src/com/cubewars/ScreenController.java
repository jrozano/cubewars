package com.cubewars;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.cubewars.backgrounds.Environment;

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
	private ShapeRenderer highlightedCells;
	private BufferedWriter output;
	private double elapsedTime;
	private double interval = 0;
	private OrthographicCamera camera;

	private Color attackHighlightColor;
	private Color moveHighlightColor;

	/**
	 * Contructor.
	 * 
	 * @param controller The Game controller used in this game.
	 */
	public ScreenController (GameController controller)
	{
		try
		{
			this.camera = new OrthographicCamera (1280, 800); // metros, cuadrados, cogemos toda la
																// pantalla
			this.camera.position.set (1280 / 2f, 800 / 2f, 0); // la camara mirar� a la mitad de la
																// pantalla, z=0
			this.controller = controller;
			this.batch = new SpriteBatch ();
			this.gridLines = new ShapeRenderer ();
			this.highlightedCells = new ShapeRenderer ();
			this.elapsedTime = 0;

			/* Set default colors for highlighting. */
			this.attackHighlightColor = new Color (1, 0, 0, 0.3f);
			this.moveHighlightColor = new Color (0, 1, 0, 0.3f);

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

		camera.update ();
		controller.tick ();

		/* Draw terrain. */
		if (controller.getTerrainContainer () != null)
		{
			batch.begin ();

			for (Environment g : controller.getTerrainContainer ())
			{
				batch.draw (g.getTexture (), g.area.x, g.area.y, g.area.width, g.area.height);
			}

			batch.end ();
		}

		/* Enable alpha. */
		Gdx.gl.glEnable (GL10.GL_BLEND);
		Gdx.gl.glBlendFunc (GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		/* Draw highlighted cells. */
		Set<Coordinates> coordinatesSet = controller.getAttackHighlightArea ();

		if (coordinatesSet != null && coordinatesSet.size () != 0)
		{
			highlightedCells.begin (ShapeType.FilledRectangle);
			highlightedCells.setColor (this.attackHighlightColor);

			for (Coordinates c : coordinatesSet)
				highlightedCells.filledRect (c.toPixel ().x, c.toPixel ().y, 128 - 1, 80 - 1);

			highlightedCells.end ();
		}

		coordinatesSet = controller.getMoveHighlightArea ();

		if (coordinatesSet != null && coordinatesSet.size () != 0)
		{
			highlightedCells.begin (ShapeType.FilledRectangle);
			highlightedCells.setColor (this.moveHighlightColor);

			for (Coordinates c : coordinatesSet)
				highlightedCells.filledRect (c.toPixel ().x, c.toPixel ().y, 127, 79);

			highlightedCells.end ();
		}

		Gdx.gl.glDisable (GL10.GL_BLEND);

		/* Draw characters and objects. */

		if (controller.getCharacterContainer () != null)
		{
			batch.begin ();

			for (GameObject g : controller.getCharacterContainer ())
			{
				batch.draw (g.getTexture (), g.area.x, g.area.y, g.area.width, g.area.height);
			}

			batch.end ();
		}

		/* Draw objects. */
		if (controller.getObjectsContainer () != null)
		{
			batch.begin ();

			for (GameObject g : controller.getObjectsContainer ())
			{
				batch.draw (g.getTexture (), g.area.x, g.area.y, g.area.width, g.area.height);
			}

			batch.end ();
		}

		/* Draw life bars. */
		if (controller.getLifebarContainer () != null)
		{
			batch.begin ();

			for (GameObject g : controller.getLifebarContainer ())
			{
				batch.draw (g.getTexture (), g.area.x + 1, g.area.y + 1, g.area.width, g.area.height);
			}

			batch.end ();
		}

		/* Draw grid lines. */
		gridLines.begin (ShapeType.Line);

		/* Horizontal lines: one line for each 80 pixels. */
		gridLines.setColor (0, 0, 0, 1);
		for (int i = 0; i < camera.viewportHeight / 80; ++i)
			gridLines.line (0, i * 80, 1280, i * 80);

		/* Vertical lines: one line for each 128 pixels. */
		for (int i = 0; i < camera.viewportWidth / 128; ++i)
			gridLines.line (i * 128, 0, i * 128, 800);

		gridLines.end ();

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

	public void setAttackHighlightColor (Color c)
	{
		this.attackHighlightColor = c;
	}

	public void setMoveHighlightColor (Color c)
	{
		this.moveHighlightColor = c;
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
		} catch (IOException e)
		{
			e.printStackTrace ();
		}
	}

	public OrthographicCamera camera ()
	{
		return camera;
	}

	public Vector3 unproject (int screenX, int screenY)
	{
		Vector3 vector = new Vector3 ();
		camera.unproject (vector.set (screenX, screenY, 0));
		return vector;
	}
}
