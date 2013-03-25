package com.cubewars.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class CharacterNull extends Character
{
	private static final Texture textura = new Texture (Gdx.files.internal ("media/characters/character-null.png"));

	public CharacterNull ()
	{
		super (textura, 0, 0, 0, 0, 0, 0, 0);
	}

}
