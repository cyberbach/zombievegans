package net.overmy.szv.neatresources;

import com.badlogic.gdx.graphics.Color;

import net.overmy.szv.Core;

public enum GameColor {
	
	SAND( 0xF2F0D5 ),
	BLUE1( 0x9CBBFF ),
	YELLOW1( 0xE8DB91 ),
	RED1( 0xFFB19F ),
	BLUE2( 0x80A2A4 ),
	
	LIGHT_RED( 0xF5CCC6 ),
	GREEN1( 0x82DAC7 ),
	INK( 0xEDE5F4 ),
	DARK_GREEN( 0x164146 ),
	DARK_BROWN( 0x3F373D ),
	
	BLUE_AQUA( 0x8EC7D2 ),
	ROZOVIY( 0xF48282 ),
	LIGHT_BLUE( 0x3498DB ),
	DARK_BLUE( 0x0C2840 ),
	SNOW_BLUE( 0xC0E0EF ),
	
	LIGHT_YELLOW( 0xDEDBBD ),
	GREEN2( 0x4B8279 ),
	GREEN3( 0x6B8A78 ),
	
	
	
	GREEN_APPLE( 0x56724F ),
	ORANGE_BANANA( 0xC68D1E ),
	RED_VISHNYA( 0xA0394A ),
	BLUE_FISH( 0x4E6696 ),
	VIOLET_VINOGRAD( 0x6E3D71 ),
	BROWN_COFFEE( 0x6D4E2E ),
	
	
	HERO( 0xF48282 ),
	ZOMBIE1( 0x617262 ),
	ZOMBIE2( 0xD1D7B5 );
	
	
	private final int iColor;
    private GameColor(final int newColor) { this.iColor = newColor; }
    public Color get() { return Core.hexToColor( iColor ); }
    
    public static int fruitsOffset(){ return GREEN_APPLE.ordinal(); }
    
    public static Color getByNumber( final int n ){
    	return Core.hexToColor( GameColor.values()[n].iColor );
    }
	
}


