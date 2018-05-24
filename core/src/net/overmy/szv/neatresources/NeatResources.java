package net.overmy.szv.neatresources;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

/** NEAT RESOURCES - ������� ������� */
public final class NeatResources {
	
	/* ����������� �����. ��������������� ����������� */
	private NeatResources() {}

	private static AssetManager		manager = null;
	
	

	
	
	/* ��������� ������������� */
	public static void load() {
		
		manager = new AssetManager();
		
		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader( FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver) );
		manager.setLoader( BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver) );
		manager.setLoader( ParticleEffect.class, new ParticleEffectLoader(resolver) );

		GFX.load( manager );
		Fonts.load( manager );
		MusicTrack.load( manager );
		SoundFX.load( manager );
		ParticleFX.load( manager );
		
		Gdx.app.log("Resources", "start loading");
		
	}
	

	
	/* ����-��������� ��������. ������ ����� ��������! */
	public static void build() {
		
		flush();
		
		GFX.build( manager );
		Fonts.build( manager );
		MusicTrack.build( manager );
		SoundFX.build( manager );
		ParticleFX.build( manager );

		Gdx.app.log("Resources", "Finish loading. Finish build");
		
	}
	
	
	
	/* �������� ���� �������� */
	
	public static void unload() {
		
		GFX.unload( manager );
		Fonts.unload( manager );
		MusicTrack.unload( manager );
		SoundFX.unload( manager );
		ParticleFX.unload( manager );
		
		manager.finishLoading();
		manager.clear();
		manager.dispose();
		
		Gdx.app.log("Resources", "unloaded");
		
	}

	
	
	/**
	 * �������� ��������. �� ����� ��� �������� � ��������. �� ���� ������� ��� �������.
	 */
	public static AssetManager getManager() {
		
		return manager;
		
	}
	


	/**
	 * ������������� ��������. ���������� ��������������� (�����) �� ������� ������ �������� ���� ��������
	 */
	public static void flush() {
		
		manager.finishLoading();
		
	}
}
