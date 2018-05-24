package net.overmy.szv.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.overmy.szv.Core;
import net.overmy.szv.MyGdxGame;
import net.overmy.szv.MyGdxGame.SCREEN_ID;
import net.overmy.szv.Settings;
import net.overmy.szv.neatresources.Fonts;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.GameColor;
import net.overmy.szv.neatresources.GameTranslation;
import net.overmy.szv.neatresources.GFX.IMG;
import net.overmy.szv.neatresources.MusicTrack;
import net.overmy.szv.neatresources.SoundFX;
import net.overmy.szv.screen.menu.MenuButton2;

public class OptionsScreen extends Base2D {
	
	float dx, dy;
	
	public OptionsScreen( final MyGdxGame game ) {
		super(game);
		
		Gdx.app.log("OptionsScreen", "constructor");
		
		layerTop.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					SoundFX.play(0);
					layerTop.addAction(Actions.run(runMenuScreen));
				}	
				return true;
			}
		});
		
		
		dx = Core.WIDTH * 0.17f;
		

		LabelStyle labelStyle2 = new LabelStyle();
		labelStyle2.font = Fonts.CAVIAN_DREAMS_50;
		labelStyle2.fontColor = Color.WHITE;

		Label soundLabel = new Label( GameTranslation.SND.get(), labelStyle2 );
		dy = soundLabel.getHeight();
		soundLabel.setPosition(dx, Core.HEIGHT - dy * 3.0f);
		
		layerTop.addActor(soundLabel);

		
		
		Label musicLabel = new Label( GameTranslation.MUS.get(), labelStyle2 );
		musicLabel.setPosition(dx, Core.HEIGHT - dy * 4.5f);
		
		layerTop.addActor(musicLabel);
		
		
		Label fieldsizeLabel = new Label( GameTranslation.FIELD_SIZE.get(), labelStyle2 );
		fieldsizeLabel.setPosition(dx, Core.HEIGHT - dy * 6.0f);
		
		layerTop.addActor(fieldsizeLabel);
		
		updateSoundButton();
		updateMusicButton();
		updateFieldButton();
		
		final MenuButton2 back = new MenuButton2(GameTranslation.OK.get());
		back.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				back.setClickAction(runMenuScreen);
			}
		});
		back.setPosition(Core.WIDTH_HALF, Core.HEIGHT_HALF/5);
		
		layerBottom.addActor(new Background());
		layerTop.addActor(back);
		
		layerTop.addActor(new FadeOutGroup(Color.BLACK));
	}
	
	
	
	Group musicButton;
	Group soundButton;
	Group fieldButton;
	
	void updateMusicButton() {
		if(musicButton != null) musicButton.clear();
		musicButton = Button(Settings.musicFlag ? GameTranslation.ON.get() : GameTranslation.OFF.get());
		musicButton.setPosition(Core.WIDTH - dx, Core.HEIGHT - dy * 2.5f);
		musicButton.addListener(new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				 Settings.musicFlag = !Settings.musicFlag;
				 updateMusicButton();
				 MusicTrack.play();
			}
		});
		
		layerTop.addActor(musicButton);
	}


	
	void updateSoundButton() {
		if(musicButton != null) soundButton.clear();
		soundButton = Button(Settings.soundFlag ? GameTranslation.ON.get() : GameTranslation.OFF.get());
		soundButton.setPosition(Core.WIDTH - dx, Core.HEIGHT - dy * 4.0f);
		soundButton.addListener(new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				 Settings.soundFlag = !Settings.soundFlag;
				 updateSoundButton();
				 SoundFX.play(0);
			}
		});
		
		layerTop.addActor(soundButton);
	}
	
	void updateFieldButton() {
		if(fieldButton != null) fieldButton.clear();
		
		String fieldText = "";
		switch(Settings.levelSize){
			case 0 : fieldText = "4 x 4"; break;
			case 1 : fieldText = "5 x 5"; break;
			case 2 : fieldText = "6 x 6"; break;
			case 3 : fieldText = "7 x 7"; break;
			case 4 : fieldText = "8 x 8"; break;
			case 5 : fieldText = "9 x 9"; break;
			case 6 : fieldText = "10 x 10"; break;
			default : fieldText = "4 x 4"; break;
		}
		
		fieldButton = Button(fieldText);
		fieldButton.setPosition(Core.WIDTH - dx, Core.HEIGHT - dy * 5.5f);
		fieldButton.addListener(new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				 //Settings.soundFlag = !Settings.soundFlag;
				Settings.levelSize++;
				if(Settings.levelSize > 6) Settings.levelSize = 0;
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
				updateFieldButton();
				SoundFX.play(0);
			}
		});
		
		layerTop.addActor(fieldButton);
	}
	
	
	
	@Override
	public void act (float delta) {
		super.act(delta);

	}

	@Override
	public void dispose() {
		Gdx.app.log("OptionsScreen", "dispose");
		
		super.dispose();
	}
	
	final Runnable runMenuScreen = new Runnable() {
		@Override
		public void run() {
			baseGame.switchScreen( SCREEN_ID.MENU ); // GO TO NEXT SCREEN <---------
		}
	};
	
	
	Group Button( final String ButtonText ) {
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Fonts.CAVIAN_DREAMS_40;
		labelStyle.fontColor = GameColor.DARK_BROWN.get();
		
		Label textLabel = new Label( ButtonText, labelStyle );
		textLabel.setPosition(-textLabel.getWidth()/2, -textLabel.getHeight()/2);

		Image bg = new Image( GFX.get(IMG.BOX) );
		bg.setSize(textLabel.getWidth() * 1.1f, textLabel.getHeight() * 1.1f);
		bg.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.8f);
		bg.setPosition(-bg.getWidth()/2, -bg.getHeight()/2);
		bg.setOrigin(bg.getWidth()/2, bg.getHeight()/2);
		
		Group tmp = new Group();
		
		tmp.addActor(bg);
		tmp.addActor(textLabel);
		return tmp;
	}
	
}
