package net.overmy.szv.neatresources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

import net.overmy.szv.Core;
import net.overmy.szv.Settings;

/** ����������� ���� */
public class MusicTrack {
	
	private final static String			DEFAULT_FOLDER = "music/";
	
	/*
	 * ��� ����� ������
	 */
	private final static String []	PATH = {
			DEFAULT_FOLDER + "piano.mp3",
			DEFAULT_FOLDER + "piano2.mp3",
			DEFAULT_FOLDER + "piano3.mp3",
		};
	
	/*
	 * ������� �������� �����
	 */
	static Music			[]mus = null;
	
	public static void play() {
		stopAll();
		if(Settings.musicFlag) {
			final int n = Core.rnd.nextInt(PATH.length); 
			mus[n].setLooping( true );
			mus[n].play();
		}
	}
	
	public static void stopAll() {
		for( int i = 0; i < PATH.length; i++ )
			mus[i].stop();
	}

	/*
	 * ��������� ������ �� ������. ������ ����� ��������!
	 */
	public static void build( final AssetManager manager ) {
		
		mus = new Music[PATH.length];
		
		for( int i = 0; i < PATH.length; i++ )
			mus[i] = manager.get( PATH[i], Music.class );
		
	}

	

	/*
	 * ������������ ������ �� ������
	 */
	private static void dispose() {
		
		for( int i = 0; i < PATH.length; i++ ) {
			mus[i].stop();
			mus[i].dispose();
		}
		mus = null;

	}
	
	
	
	/*
	 * �������� ������
	 */
	public static void load( final AssetManager manager ) {
		
		for( int i = 0; i < PATH.length; i++ )
			manager.load( PATH[i], Music.class );
		
	}
	
	
	
	/*
	 * �������� ������
	 */
	public static void unload( final AssetManager manager ) {
		
		dispose();
		
		for( int i = 0; i < PATH.length; i++ )
			manager.unload( PATH[i] );
		
	}
}
