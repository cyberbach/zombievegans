package net.overmy.szv.neatresources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import net.overmy.szv.Settings;

/** Звуковой эффект */
public class SoundFX {
	
	private final static String			DEFAULT_FOLDER = "sound/";
	
	/*
	 * Все звуковые файлы
	 */
	private final static String []	PATH = {
			
			DEFAULT_FOLDER + "click1.wav",
			DEFAULT_FOLDER + "click2.wav",
			DEFAULT_FOLDER + "click3.wav",
			DEFAULT_FOLDER + "fall.wav",
			DEFAULT_FOLDER + "click4.wav",
			
			DEFAULT_FOLDER + "nyamnyam.wav", // 5
			DEFAULT_FOLDER + "umm.wav",
			DEFAULT_FOLDER + "ooo.wav",

			DEFAULT_FOLDER + "bee.wav", // 8
			DEFAULT_FOLDER + "fuhh.wav",
			DEFAULT_FOLDER + "oi.wav",

			DEFAULT_FOLDER + "ai.wav", // 11
			DEFAULT_FOLDER + "ui.wav",
			DEFAULT_FOLDER + "oh.wav",

			DEFAULT_FOLDER + "rrr.wav", // 14
			DEFAULT_FOLDER + "khekhe.wav",

			DEFAULT_FOLDER + "win.wav", // 16
			DEFAULT_FOLDER + "fail.wav",

			DEFAULT_FOLDER + "magic.wav", // 18
			DEFAULT_FOLDER + "intro.wav", // 19
			
		};
	
	/*
	 * Удобное название звука
	 */
	static Sound			[] sound = null;
	
	public static void play( final int n ) {
		/*
		for( int i = 0; i < PATH.length; i++ )
			sound[i].stop();
		*/
		if(Settings.soundFlag) sound[n].play();
	}
	
	/*
	 * Получение звуков из памяти. Только после загрузки!
	 */
	public static void build( final AssetManager manager ) {
		
		sound = new Sound[PATH.length];
		
		for( int i = 0; i < PATH.length; i++ )
			sound[i] = manager.get( PATH[i], Sound.class );
		
	}

	

	/*
	 * Освобождение ссылок на звуки
	 */
	private static void dispose() {
		
		for( int i = 0; i < PATH.length; i++ )
			sound[i].dispose();

	}
	
	
	
	/*
	 * Загрузка звуков в память
	 */
	public static void load( final AssetManager manager ) {
		
		for( int i = 0; i < PATH.length; i++ )
			manager.load( PATH[i], Sound.class );
		
	}
	
	
	
	/*
	 * Выгрузка звуков из памяти
	 */
	public static void unload( final AssetManager manager ) {
		
		dispose();
		
		for( int i = 0; i < PATH.length; i++ )
			manager.unload( PATH[i] );
		
	}
}
