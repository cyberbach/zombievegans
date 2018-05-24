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

/** NEAT RESOURCES - Удобные ресурсы */
public final class NeatResources {
	
	/* Статический класс. Заблокированный конструктор */
	private NeatResources() {}

	private static AssetManager		manager = null;
	
	

	
	
	/* Начальная инициализация */
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
	

	
	/* Пост-обработка ресурсов. Только после загрузки! */
	public static void build() {
		
		flush();
		
		GFX.build( manager );
		Fonts.build( manager );
		MusicTrack.build( manager );
		SoundFX.build( manager );
		ParticleFX.build( manager );

		Gdx.app.log("Resources", "Finish loading. Finish build");
		
	}
	
	
	
	/* Выгрузка всех ресурсов */
	
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
	 * Менеджер загрузки. Он нужен для загрузки и выгрузки. Из него достаем все ресурсы.
	 */
	public static AssetManager getManager() {
		
		return manager;
		
	}
	


	/**
	 * Форсированная загрузка. Приложение останавливается (висит) до момента полной загрузки всех ресурсов
	 */
	public static void flush() {
		
		manager.finishLoading();
		
	}
}
