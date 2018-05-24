package net.overmy.szv.neatresources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/** ������ ������ */
public class ParticleFX {
	
	private final static String		DEFAULT_FOLDER = "particles/";
	
	/*
	 * ��� ����� �������� ������
	 */
	private final static String []	PATH = {
			DEFAULT_FOLDER + "circle.pfx"
		};
	
	/*
	 * ������� �������� �������
	 */
	public static ParticleEffect effect = null;
	
	
	
	/*
	 * ��������� �������� �� ������. ������ ����� ��������!
	 */
	public static void build( final AssetManager manager ) {
		
		effect = manager.get( PATH[0] );
		
	}
	/*
	public static void setSprite( final IMG img ) {
		effect.allowCompletion();
		Sprite sprite = null;
		sprite = GFX.getSprite(img);
		
		for( int i = 0; i < effect.getEmitters().size; i++ )
			effect.getEmitters().get(i).setSprite(sprite); // TODO why not change?
		
		//final float []colors = {c.r, c.g, c.b, c.a};
		//effect.getEmitters().first().getTint().setColors(colors);
		
		Gdx.app.log("setSprite","img " + img.ordinal());
	}*/
	
	public static void setColor( final Color c ) {
		//Sprite spr = effect.getEmitters().first().getSprite();
		//spr.setColor( c );
		//effect.getEmitters().first().setSprite(spr);
		final float []colors = {c.r, c.g, c.b, c.a};
		effect.getEmitters().first().getTint().setColors(colors);
	}

	/*
	 * ������������ ������ �� �������
	 */
	private static void dispose() {
		
		effect.allowCompletion();
		
		effect.dispose();

	}
	
	
	
	/*
	 * �������� �������� � ������
	 */
	public static void load( final AssetManager manager ) {
		
		for( int i = 0; i < PATH.length; i++ )
			manager.load( PATH[i], ParticleEffect.class );
		
	}
	
	
	
	/*
	 * �������� �������� �� ������
	 */
	public static void unload( final AssetManager manager ) {
		
		dispose();
		
		for( int i = 0; i < PATH.length; i++ )
			manager.unload( PATH[i] );
		
	}
}
