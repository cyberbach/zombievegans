package net.overmy.szv.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.overmy.szv.Core;
import net.overmy.szv.MyGdxGame;
import net.overmy.szv.MyGdxGame.SCREEN_ID;
import net.overmy.szv.neatresources.MusicTrack;
import net.overmy.szv.neatresources.NeatResources;

public class LoadingScreen extends Base2D {
	
	final String LOADING_TEXTURE = "box.png";
	final String LOADING_TEXT = "loadtext.png";
	
	Texture loadingTexture1 = null;
	Texture loadingText2 = null;
	
	Image bar1;
	Image text;
	float percent = 0.0f;
	
	boolean idleFlag = false;
	boolean afterLoadingFlag = false;

	
	
	public LoadingScreen( final MyGdxGame game ) {
		super(game);
		
		Gdx.app.log("LoadingScreen", "constructor");

		loadingTexture1 = new Texture( Gdx.files.internal(LOADING_TEXTURE) );
		bar1 = new Image( loadingTexture1 );
		bar1.setSize(Core.WIDTH, Core.HEIGHT_HALF / 3);
		
		loadingText2 = new Texture( Gdx.files.internal(LOADING_TEXT) );
		text = new Image( loadingText2 );
		//text.setSize(Core.HEIGHT_HALF * 2 / 3, Core.HEIGHT_HALF / 3);
		text.setPosition(Core.WIDTH_HALF - text.getWidth()/2, Core.HEIGHT_HALF / 6 - text.getHeight()/2);
		
		layerBottom.addActor(bar1);
		layerBottom.addActor(text);
	}
	
	@Override
	public void act (float delta) {
		super.act(delta);

		// Это условие, чтобы опять не начали загружаться ресурсы
		if( !idleFlag ) {
			if( !afterLoadingFlag ) {
				NeatResources.getManager().update(); // Здесь ресурсы загружаются
				if( percent > 0.99f ) afterLoadingFlag = true;
				percent = Interpolation.linear.apply(percent, NeatResources.getManager().getProgress(), 0.1f);
				bar1.setScale(percent + 0.1f, 1);
			} else {
				NeatResources.build(); // Здесь полностью загрузились
				idleFlag = true;
				layerTop.addActor(new FadeInGroup(Color.BLACK));
				layerTop.addAction(Actions.sequence(
						Actions.delay(Core.FADE_TIME),
						Actions.run(runNextScreen)
					));

				// TODO music volume
				
				MusicTrack.play();
				
			}
		}
	}

	final Runnable runNextScreen = new Runnable() {
		@Override
		public void run() {
			baseGame.switchScreen( SCREEN_ID.MENU ); // GO TO NEXT SCREEN <---------
		}
	};
	
	@Override
	public void dispose() {
		
		Gdx.app.log("LoadingScreen", "dispose");
		bar1.clear();
		text.clear();

		loadingTexture1.dispose();
		loadingText2.dispose();
		
		loadingTexture1 = null;
		loadingText2 = null;
		
		super.dispose();
	}
	
	
}
