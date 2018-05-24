package net.overmy.szv.neatresources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/** Шрифт */
public class Fonts {
	
	private final static String			DEFAULT_FOLDER = "font/";
	
	/*
	 * Все файлы шрифтов
	 */
	private final static String []		PATH = {
			
			DEFAULT_FOLDER + "cavian.ttf"

		};
	
	/*
	 * Название шрифта для загрузки
	 */
	private final static short			CAVIAN_DREAMS = 0;
	
	/*
	 * Удобное название шрифта и его размер
	 */
	public static BitmapFont			CAVIAN_DREAMS_70 = null;
	public static BitmapFont			CAVIAN_DREAMS_50 = null;
	public static BitmapFont			CAVIAN_DREAMS_60 = null;
	public static BitmapFont			CAVIAN_DREAMS_40 = null;
	public static BitmapFont			CAVIAN_DREAMS_30 = null;
	
	
	
	/*
	 * Генерация шрифтов. Только после загрузки!
	 */
	public static void build( final AssetManager manager ) {
		
		FreeTypeFontGenerator myFontGenerator;
		FreeTypeFontParameter myFontParameters = new FreeTypeFontParameter();
		myFontParameters.borderWidth = 1;
		myFontParameters.borderColor = GameColor.BLUE1.get();//Color.LIGHT_GRAY;
		myFontParameters.characters = createChars();
				
		myFontGenerator = manager.get( PATH[CAVIAN_DREAMS], FreeTypeFontGenerator.class );
		myFontParameters.size = 70;
		CAVIAN_DREAMS_70 = myFontGenerator.generateFont( myFontParameters );
		
		myFontParameters.size = 50;
		CAVIAN_DREAMS_50 = myFontGenerator.generateFont( myFontParameters );
		
		myFontParameters.size = 60;
		CAVIAN_DREAMS_60 = myFontGenerator.generateFont( myFontParameters );
		
		myFontParameters.size = 30;
		CAVIAN_DREAMS_30 = myFontGenerator.generateFont( myFontParameters );

		myFontParameters.borderWidth = 0;
		myFontParameters.size = 44;
		CAVIAN_DREAMS_40 = myFontGenerator.generateFont( myFontParameters );

		// myFontGenerator.dispose(); // не нужен, т.к. вызывается автоматически в manager.unload
	}
	
	
	
	/*
	 * Генерация строки символов
	 */
	protected static String createChars() {

		final StringBuilder fontChars = new StringBuilder();

		for ( int i = 0x20; i < 0x7B; i++ ) {
			fontChars.append( (char) i );
		}

		for ( int i = 0x401; i < 0x452; i++ ) {
			fontChars.append( (char) i );
		}

		return fontChars.toString();
		
	}

	
	
	/*
	 * Освобождение ссылок на шрифты
	 */
	private static void dispose() {
		
		CAVIAN_DREAMS_70.dispose();
		CAVIAN_DREAMS_60.dispose();
		CAVIAN_DREAMS_50.dispose();
		CAVIAN_DREAMS_30.dispose();
		CAVIAN_DREAMS_40.dispose();

	}
	
	
	
	/*
	 * Загрузка файлов шрифтов в память
	 */
	public static void load( final AssetManager manager ) {
		
		for( int i = 0; i < PATH.length; i++ )
			manager.load( PATH[i], FreeTypeFontGenerator.class );
		
	}
	
	
	
	/*
	 * Выгрузка шрифтов из памяти
	 */
	public static void unload( final AssetManager manager ) {
		
		dispose();
		
		for( int i = 0; i < PATH.length; i++ )
			manager.unload( PATH[i] );
		
	}
}
