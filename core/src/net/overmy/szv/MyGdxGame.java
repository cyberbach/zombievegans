package net.overmy.szv;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import net.overmy.szv.neatresources.NeatResources;
import net.overmy.szv.screen.GameScreen;
import net.overmy.szv.screen.LoadingScreen;
import net.overmy.szv.screen.MenuScreen;
import net.overmy.szv.screen.OptionsScreen;
import net.overmy.szv.screen.ScriptScene;

public class MyGdxGame extends Game {
	
	private Screen			screen = null;
	
	public enum SCREEN_ID {
		LOADING,
		MENU,
		GAME,
		OPTIONS,
		SCRIPTS
	};

	
	public MyGdxGame() {  }

	
	
	@Override
	public void create() {
		
		Settings.load();
		Core.init();
		NeatResources.load();
		
		switchScreen( SCREEN_ID.LOADING );
		
	}

	
	public void switchScreen( final SCREEN_ID id ) {
		
		this.setScreen( createScreen( id ) );
		
	}
	

	
	private Screen createScreen( final SCREEN_ID id ) {
		
		if(this.getScreen() != null) { this.getScreen().dispose(); screen = null; }

		switch ( id ) {
			case LOADING: return screen = new LoadingScreen(this);
			case GAME: return screen = new GameScreen(this);
			case MENU: return screen = new MenuScreen(this);
			case OPTIONS: return screen = new OptionsScreen(this);
			case SCRIPTS: return screen = new ScriptScene(this);
			default: return null;
		}
		
	}
	

	
	public void stop() {
		
		screen.dispose();
		screen = null;
		
		Settings.save();
		NeatResources.unload();
		
		Gdx.app.exit();
		
	}
}
