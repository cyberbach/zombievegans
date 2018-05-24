package net.overmy.szv.screen.menu;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

import net.overmy.szv.Core;
import net.overmy.szv.neatresources.Fonts;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.GFX.IMG;
import net.overmy.szv.neatresources.GameColor;
import net.overmy.szv.neatresources.SoundFX;

public class MenuButton extends Group {

	final float CLICK_TIME = 0.10f;
	
	public Label		textLabel;
	
	final Runnable clickSound = new Runnable() {
		@Override
		public void run() {
			SoundFX.play(0);
		}
	};
	
	
	
	public MenuButton( final String buttonText ) {
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Fonts.CAVIAN_DREAMS_50;
		labelStyle.fontColor = GameColor.DARK_GREEN.get();
		
		textLabel = new Label( buttonText, labelStyle );
		textLabel.setPosition( -textLabel.getWidth() * 0.5f, -textLabel.getHeight() * 0.5f );
		textLabel.setAlignment( Align.center );
		
		final float h = textLabel.getHeight() * 1.15f;
		final float w = textLabel.getWidth() * 1.15f;
		
		Image bgImage1 = new Image( GFX.get(IMG.BOX) );
		//bgImage1.setColor(Core.color[14]);
		bgImage1.setSize( w, h );
		bgImage1.setPosition( -bgImage1.getWidth() * 0.5f, -bgImage1.getHeight() * 0.5f );
		
		this.addActor(bgImage1);
		this.addActor(textLabel);
	}
	
	
	public void setClickAction( final Runnable action ) {
		
		this.addAction( Actions.sequence( 
				Actions.scaleTo(0.9f, 0.9f, CLICK_TIME/2, Interpolation.fade),
				Actions.run(clickSound),
				Actions.scaleTo(1.0f, 1.0f, CLICK_TIME/2, Interpolation.fade),
				Actions.delay(Core.FADE_TIME),
				Actions.run(action) ) 
			);
	}

}
