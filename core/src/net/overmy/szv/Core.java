package net.overmy.szv;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import net.overmy.szv.neatresources.GameTranslation;
import net.overmy.szv.neatresources.LevelSettings;

public class Core {
	
	public static Random			rnd = null;
	
	public static int				WIDTH;
	public static int				HEIGHT;
	public static int				WIDTH_HALF;
	public static int				HEIGHT_HALF;
	public static float				ASPECT_RATIO; // не меньше 1
	
	public static final float FADE_TIME = 0.35f;

	// GAME STUFF
	public static int MAX_FIELD_CELLS = 6; // TODO update from settings
	public static int MAX_CELL_VALUES = 6;

	//static String language = "";
	/*
	public final static Color[]		color = {
			
			hexToColor( 0xF2F0D5 ),// sand
			hexToColor( 0x9CBBFF ),// blue
			hexToColor( 0xE8DB91 ),// yellow
			hexToColor( 0xFFB19F ),// red
			hexToColor( 0x80A2A4 ),// blue --------4
			
			hexToColor( 0xF5CCC6 ),// light red
			hexToColor( 0x82DAC7 ),// green
			hexToColor( 0xEDE5F4 ),// ink
			hexToColor( 0x164146 ),// dark green
			hexToColor( 0x3F373D ),// dark brown -------9
			
			hexToColor( 0x8EC7D2 ),// blue auqa
			hexToColor( 0xF48282 ), // rozoviy
			hexToColor( 0x3498DB ), // light blue
			hexToColor( 0x0C2840 ), // dark blue
			hexToColor( 0xC0E0EF ), // snow blue/white -------14
			
			hexToColor( 0xDEDBBD ), // light yellow
			hexToColor( 0x4B8279 ), // one more green
			hexToColor( 0x6B8A78 ), // natural green --------17

	};
	
	public final static Color[]		magicColor = {
			
			hexToColor( 0x56724F ), // зелЄное €блоко
			hexToColor( 0xC68D1E ), // оранжевый банан
			hexToColor( 0xA0394A ), // красна€ вишн€
			hexToColor( 0x4E6696 ), // син€€ рыба
			hexToColor( 0x6E3D71 ), // фиалетовый виноград
			hexToColor( 0x6D4E2E ), // коричневый кофе
			
		};
	
	public final static Color[]		zombieColor = {
			
			hexToColor( 0xF48282 ), // hero

			hexToColor( 0x617262 ), // other zombies // green
			hexToColor( 0xD1D7B5 ),
			
		};
	*/
	

	


	


	
	

	
	public static LevelSettings [] levelSettings = {
			// enemy life, enemy type, enemy color
			new LevelSettings(10, 1, 1),
			new LevelSettings(10, 0, 0),
			new LevelSettings(12, 1, 1),
			
			new LevelSettings(10, 0, 0),
			new LevelSettings(11, 2, 1),
			new LevelSettings(13, 0, 0),
			
			new LevelSettings(11, 2, 1),
			new LevelSettings(12, 1, 0),
			new LevelSettings(14, 2, 1),
		};
	
	
	
	public static int[] cellsPower = { 1, 1, 2, 2, 3, 3 };
	
	
	/*
	 * √лавный метод класса Core
	 */
	public static void init() {
		
		Gdx.app.log("Core", "init");

		rnd = new Random();
		resize( Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
		
		//language = java.util.Locale.getDefault().toString();
		
		switch(Settings.levelSize){
			case 0 : Core.MAX_FIELD_CELLS = 4; break;
			case 1 : Core.MAX_FIELD_CELLS = 5; break;
			case 2 : Core.MAX_FIELD_CELLS = 6; break;
			case 3 : Core.MAX_FIELD_CELLS = 7; break;
			case 4 : Core.MAX_FIELD_CELLS = 8; break;
			case 5 : Core.MAX_FIELD_CELLS = 9; break;
			case 6 : Core.MAX_FIELD_CELLS = 10; break;
			default: Core.MAX_FIELD_CELLS = 4; break;
		}
		
		GameTranslation.detect();
	}
	

	
	/*
	 * »зменение внутренних переменных, если был вызван resize
	 */
	public static void resize( final int width, final int height ) {
		
		WIDTH = width;
		HEIGHT = height;
		WIDTH_HALF = WIDTH / 2;
		HEIGHT_HALF = HEIGHT / 2;
		
		ASPECT_RATIO = (float)width / (float)height;
		if(ASPECT_RATIO < 1.7f) ASPECT_RATIO = 1.0f;
				
	}
	
	public static float randomPercent( final float percent, final float r ){ return r * percent + Core.rnd.nextFloat() * r * (1 - percent); };
	
	

	/** ѕеревод из Hex в Color */
	public final static Color hexToColor( int a ) {
		
		final int mask = 0x0FF;
		
		int c1 = (a >> 16);
		int c2 = (a >> 8) & mask;
		int c3 = a & mask;
		
		float f1 = (float)((float)c1 / (float)0xFF);
		float f2 = (float)((float)c2 / (float)0xFF);
		float f3 = (float)((float)c3 / (float)0xFF);
		
		return new Color( f1, f2, f3, 1 );
	}
}
