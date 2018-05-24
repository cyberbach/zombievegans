package net.overmy.szv;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class Settings { // Если нужно зашифровать строчку Base64Coder.encodeString(
	
	private Settings() {}
	
	private final static String SETTINGS = "game.prefs";
	private static Preferences prefs;
	
	
	public static boolean soundFlag;
	public static boolean musicFlag;
	
	public static int currentLevel = 0;
	public static int levelSize = 1;
	
	
	
	public static void init() {
		prefs = Gdx.app.getPreferences(SETTINGS);
	}
	
	
	
	public static void load() {
		init();
		Gdx.app.log("Settings", "loading\n");
		soundFlag = prefs.getBoolean("snd", true);
		musicFlag = prefs.getBoolean("mus", true);
		currentLevel = prefs.getInteger("lev", 0);
		levelSize = prefs.getInteger("levsize", 1);
	}

	

	public static void save() {
		Gdx.app.log("Settings", "save");
		prefs.putBoolean("snd", soundFlag);
		prefs.putBoolean("mus", musicFlag);
		prefs.putInteger("lev", currentLevel);
		prefs.putInteger("levsize", levelSize);
		prefs.flush();
	}
	
}
