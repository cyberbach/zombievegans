package net.overmy.szv.neatresources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/** Графический атлас */
public class GFX {
	
	private final static String		PATH = "gfx.atlas";

	private static TextureAtlas		atlas = null;


	public enum IMG {
		
		BOX("box"),
		PLATE("plate"),
		BODY1("body1"),
		BODY2("body2"),
		BODY3("body3"),
		BODY4("body4"),
		TRIANGLE("triangle"),
		ERCIRCLE("encircle"),
		LIGHT("light"),
		FACE1("face1"),
		FACE2("face2"),
		FACE3("face3"),
		FACE4("face4"),
		FACE5("face5"),
		FACE6("face6"),
		ICON1("icon1"),
		ICON2("icon2"),
		ICON3("icon3"),
		ICON4("icon4"),
		ICON5("icon5"),
		ICON6("icon6"),
		SMALL1("small1"),
		SMALL2("small2"),
		SMALL3("small3"),
		SMALL4("small4"),
		SMALL5("small5"),
		SMALL6("small6"),
		MOUNTAIN("mountain"),
		SKY("sky"),
		CLOUD("cloud"),
		TREE("tree"),
		TREE2("tree2"),
		TREE3("tree3"),
		FLAKE1("flake01"),
		FLAKE2("flake02"),
		FLAKE3("flake03"),
		FLAKE4("flake04"),
		FLAKE5("flake05"),
		LEAF1("leaf01"),
		LEAF2("leaf02"),
		LEAF3("leaf03");
		
		private final String text;

	    private IMG(final String text) { this.text = text; }

	    @Override
	    public String toString() { return text; }
		
	}

	
	
	/*
	 * Получение атласа из памяти. Только после загрузки!
	 */
	public static void build( final AssetManager manager ) {
		
		atlas = manager.get( PATH, TextureAtlas.class );
		
	}

	
	
	/*
	 * Получение ссылки на атлас
	 */
	public static TextureAtlas getAltas() {
		
		return atlas;
		
	}

	

	/*
	 * Освобождение атласа
	 */
	private static void dispose() {
		
		atlas.dispose();
		atlas = null;
		
	}
	

	
	/*
	 * Загрузка атласа в память
	 */
	public static void load( final AssetManager manager ) {

		manager.load( PATH, TextureAtlas.class );
		
	}
	

	
	/*
	 * Выгрузка атласа из памяти
	 */
	public static void unload( final AssetManager manager ) {
		
		dispose();
		
		manager.unload( PATH );
		
	}
	

	
	/*
	 * Получение картинки из атласа по имени (IMG)
	 */
	public static AtlasRegion get( final IMG imgName ) {
		
		return atlas.findRegion( imgName.toString() );
		
	}
	

	
	/*
	 * Получение картинки из атласа по имени (IMG)
	 */
	public static Sprite getSprite( final IMG imgName ) {
		
		return atlas.createSprite( imgName.toString() );
		
	}	
}
