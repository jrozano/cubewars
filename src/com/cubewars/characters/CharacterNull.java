package com.cubewars.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class CharacterNull extends Character
{
	private static final Texture texture = new Texture (Gdx.files.internal ("media/characters/character-null.png"));

	public CharacterNull ()
	{
		super (texture, 0, 0, 0, 0, 0, 0, 0);
	}

}
