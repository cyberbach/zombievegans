package net.overmy.szv.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import net.overmy.szv.Core;
import net.overmy.szv.neatresources.Fonts;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.GFX.IMG;
import net.overmy.szv.neatresources.GameColor;

public class MagicBar extends Group {

	public Image fillness;
	public Label text;

	final float bgsizex = (Core.WIDTH - Core.HEIGHT) / 2 * 0.95f;
	final float bgsizey = (Core.WIDTH - Core.HEIGHT) / 4 * 0.95f;
	final float fillnesssizey = bgsizey * 0.9f;
	final float fillnesssizex = bgsizex - (bgsizey - fillnesssizey);

	public MagicBar( final int n ) {
		
		Image bg = new Image( GFX.get(IMG.BOX) );
		bg.setColor(GameColor.values()[GameColor.fruitsOffset() + n].get());
		bg.setSize(bgsizex, bgsizey);
		bg.setPosition(-bg.getWidth()/2, -bg.getHeight()/2);


		fillness = new Image( GFX.get(IMG.BOX) );
		fillness.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.5f);
		fillness.setSize(fillnesssizex, fillnesssizey);
		fillness.setPosition(-bg.getWidth()/2 + (bgsizey - fillnesssizey)/2, -bg.getHeight()/2 + (bgsizey - fillnesssizey)/2);
		fillness.setOrigin(0, 0);
		fillness.setScaleX(0.0f);
		
		Image icon = new Image( GFX.get(IMG.values()[IMG.SMALL1.ordinal() + n]) );
		icon.setSize(bgsizey, bgsizey);
		icon.setPosition(bg.getWidth()/2 - bgsizey, -bg.getHeight()/2);
		icon.setOrigin(icon.getWidth()/2, icon.getHeight()/2);
		icon.addAction(Actions.forever(Actions.sequence(
				Actions.delay(Core.randomPercent(0.1f, 3.0f)),
				Actions.rotateTo(-360, 1, Interpolation.fade),
				Actions.rotateTo(0, 0)
			)));
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Fonts.CAVIAN_DREAMS_40;
		labelStyle.fontColor = Color.WHITE;
		
		text = new Label( "0", labelStyle );
		text.setPosition( -bg.getWidth()/2 + text.getWidth()/2, -text.getHeight()/2 );


		this.addActor(bg);
		this.addActor(fillness);
		this.addActor(icon);
		this.addActor(text);
	}

}
