package net.overmy.szv.screen.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

import net.overmy.szv.neatresources.Fonts;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.GFX.IMG;
import net.overmy.szv.neatresources.GameColor;
import net.overmy.szv.neatresources.SoundFX;

public class GameDebugButton extends Group {

	public Label		textLabel;
	
	final Runnable clickSound = new Runnable() {
		@Override
		public void run() {
			SoundFX.play(0);
		}
	};
	
	public float HEIGHT = 0.0f;
	public float WIDTH = 0.0f;
	
	
	
	public GameDebugButton( final String buttonText ) {
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Fonts.CAVIAN_DREAMS_50;
		labelStyle.fontColor = Color.WHITE;
		
		textLabel = new Label( buttonText, labelStyle );
		textLabel.setPosition( -textLabel.getWidth() * 0.5f, -textLabel.getHeight() * 0.5f );
		textLabel.setAlignment( Align.center );
		
		Image bgImage1 = new Image( GFX.get(IMG.BOX) );
		final Color c = GameColor.YELLOW1.get();
		bgImage1.setColor(c.r, c.g, c.b, 0.4f);
		bgImage1.setSize( textLabel.getWidth() * 1.1f, textLabel.getHeight() );
		bgImage1.setPosition( -bgImage1.getWidth() * 0.5f, -bgImage1.getHeight() * 0.5f );
		
		HEIGHT = textLabel.getHeight();
		WIDTH = textLabel.getWidth();
		
		this.addActor(bgImage1);
		this.addActor(textLabel);
	}
	
	
	
	public void setClickAction( final Runnable action ) {
		
		this.addAction( Actions.sequence( 
				Actions.scaleTo(0.8f, 0.8f, 0.07f),
				Actions.run(clickSound),
				Actions.scaleTo(1.0f, 1.0f, 0.07f),
				Actions.run(action) ) 
			);
	}

}
