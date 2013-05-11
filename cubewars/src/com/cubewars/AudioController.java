package com.cubewars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import java.util.*;

/** Class whose task is controlling all the game`s audio
 *  
 * @author Gandio
 */

public class AudioController 
{
	/* Sounds' playlist, <title, address> */
	private static Map<String,Sound> SoundList = new HashMap<String, Sound>();
	
	/* Musics' playlist <title, address> */
	private static Map<String,Music> MusicList = new HashMap<String, Music>();
	
	/*The constructor add sounds or songs to the playlist*/
	public AudioController()
	{	
		MusicList.put("Battle_1",Gdx.audio.newMusic(Gdx.files.internal("media/sounds/Batalla-1.mp3")));
	}
	
	/* Play a sound without loop */
	void PlaySound(String s)
	{
		SoundList.get(s).play();
	}
	
	/* Play a song with loop */
	void PlayMusic(String s)
	{
	    Music m = MusicList.get(s);
	    m.setLooping(true);
	    m.play();
	}

}
