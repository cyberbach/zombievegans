package net.overmy.szv.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.overmy.szv.Core;
import net.overmy.szv.MyGdxGame;
import net.overmy.szv.MyGdxGame.SCREEN_ID;
import net.overmy.szv.neatresources.GameTranslation;
import net.overmy.szv.neatresources.SoundFX;
import net.overmy.szv.screen.menu.MenuButton;
import net.overmy.szv.screen.menu.MenuLogotype;

public class MenuScreen extends Base2D {
	
	MenuButton start;
	MenuButton options;
	MenuLogotype logo;
	//MenuBG bg;
	
	public MenuScreen( final MyGdxGame game ) {
		super(game);

		
		Gdx.app.log("MenuScreen", "constructor");
		
		//backgroundColor = Core.color[4];
		
		// Если нажимаем кнопку ESC или НАЗАД на телефоне - то полностью выходим из игры
		layerTop.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					layerTop.addAction(Actions.run(stopTheGame));
				}	
				return true;
			}
		});
		
		layerTop.addActor( start = new MenuButton(GameTranslation.PLAY.get()) );
		//start.setPosition(Core.WIDTH_HALF, Core.HEIGHT_HALF - start.textLabel.getHeight() * 1.2f);
		start.setPosition(Core.WIDTH_HALF, -Core.HEIGHT_HALF);
		start.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				start.setClickAction(runScriptsScreen);
				layerTop.addActor(new FadeInGroup(Color.BLACK));
			}
		});
		start.addAction(Actions.moveTo(Core.WIDTH_HALF, Core.HEIGHT_HALF - start.textLabel.getHeight() * 0.4f, 0.7f, Interpolation.fade));
		
		layerTop.addActor( options = new MenuButton(GameTranslation.OPTIONS.get()) );
		//options.setPosition(Core.WIDTH_HALF, Core.HEIGHT_HALF - options.textLabel.getHeight() * 2.5f);
		options.setPosition(Core.WIDTH_HALF, -Core.HEIGHT_HALF);
		options.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				options.setClickAction(runOptionsScreen);
				layerTop.addActor(new FadeInGroup(Color.BLACK));
			}
		});
		options.addAction(Actions.moveTo(Core.WIDTH_HALF, Core.HEIGHT_HALF - options.textLabel.getHeight() * 1.7f, 0.8f, Interpolation.fade));
		
		layerTop.addActor( logo = new MenuLogotype() );
		
		//layerBottom.addActor( bg = new MenuBG() );
		
		//logo.setPosition( Core.WIDTH_HALF, Core.HEIGHT * 3 / 4 );
		logo.setPosition( Core.WIDTH_HALF, Core.HEIGHT );
		logo.addAction(Actions.moveTo(Core.WIDTH_HALF, Core.HEIGHT * 3 / 4, 0.6f, Interpolation.fade));
		
		layerBottom.addActor(new Background());
		
		layerTop.addActor(new FadeOutGroup(Color.BLACK));
		
		SoundFX.play(19);
	}

	@Override
	public void dispose() {
		Gdx.app.log("MenuScreen", "dispose");
		
		start.clear();
		options.clear();
		logo.clear();
		//bg.clear();
		
		super.dispose();
	}
	
	
	final Runnable runScriptsScreen = new Runnable() {
		@Override
		public void run() {
			baseGame.switchScreen( SCREEN_ID.SCRIPTS ); // GO TO  NEXT SCREEN <---------
		}
	};

	
	
	final Runnable runOptionsScreen = new Runnable() {
		@Override
		public void run() {
			baseGame.switchScreen( SCREEN_ID.OPTIONS ); // GO TO  NEXT SCREEN <---------
		}
	};
	
	
	final Runnable stopTheGame = new Runnable() {
		@Override
		public void run() {
			baseGame.stop();
		}
	};
	
	
}
