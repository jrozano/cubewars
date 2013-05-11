package com.cubewars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import java.util.*;


public class AudioController 
{
	private static Map<String,Sound> SoundList = new HashMap<String, Sound>();
	private static Map<String,Music> MusicList = new HashMap<String, Music>();
	
	public AudioController()
	{	
		MusicList.put("Battle_1",Gdx.audio.newMusic(Gdx.files.internal("media/sounds/Batalla-1.mp3")));
	}
	
	void PlaySound(String s)
	{
		SoundList.get(s).play();
	}
	
	void PlayMusic(String s)
	{
	    Music m = MusicList.get(s);
	    m.setLooping(true);
	    m.play();
	}

}
