package net.overmy.szv.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import net.overmy.szv.Core;
import net.overmy.szv.MyGdxGame;
import net.overmy.szv.MyGdxGame.SCREEN_ID;
import net.overmy.szv.Settings;
import net.overmy.szv.logic.Level;
import net.overmy.szv.neatresources.SoundFX;

public class GameScreen extends Base2D {

	Level					level;
	Background				bg;
	
	
	
	public GameScreen( final MyGdxGame game ) {
		super(game);
		
		
		Gdx.app.log("GameScreen", "constructor");
		
		layerTop.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					SoundFX.play(0);
					layerTop.addAction(Actions.sequence(
							Actions.delay(Core.FADE_TIME),
							Actions.run(runMenuScreen)
						));//Actions.run(runMenuScreen));
					layerTop.addActor(new FadeInGroup(Color.BLACK));
				}	
				return true;
			}
		});

		level = new Level(){
			@Override
			public void restart() { layerTop.addActor(new FadeInGroup(Color.BLACK));
				layerBottom.addAction(Actions.sequence(
						Actions.delay(Core.FADE_TIME),
						Actions.run(restartGameScreen)
					));}
			@Override
			public void finish() { layerTop.addActor(new FadeInGroup(Color.BLACK));
				Settings.currentLevel = 0; layerBottom.addAction(Actions.sequence(
						Actions.delay(Core.FADE_TIME),
						Actions.run(runMenuScreen)
					));}
		};

		
		layerBottom.addActor( bg = new Background() );
		layerTop.addActor( level.getGroups() );
		
		layerTop.addActor(new FadeOutGroup(Color.BLACK));

		
	}

	
	
	@Override
	public void dispose() {
		Gdx.app.log("GameScreen", "dispose");
		
		level.clear();
		bg.clear();
		
		super.dispose();
	}
	
	
	final Runnable restartGameScreen = new Runnable() {
		@Override
		public void run() {
			Settings.currentLevel++;
			baseGame.switchScreen( SCREEN_ID.SCRIPTS );
		}
	};	
	
	final Runnable runMenuScreen = new Runnable() {
		@Override
		public void run() {
			
			baseGame.switchScreen( SCREEN_ID.MENU );
		}
	};
	
}
