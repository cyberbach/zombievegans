package net.overmy.szv.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.overmy.szv.Core;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.GFX.IMG;
import net.overmy.szv.neatresources.GameColor;

public class LifeBar extends Group {

	public Image fillness;

	final float bgsizex = (Core.WIDTH - Core.HEIGHT) / 2 * 0.95f;
	public final float bgsizey = (Core.WIDTH - Core.HEIGHT) / 16 * 0.95f;
	final float fillnesssizey = bgsizey * 0.9f;
	final float fillnesssizex = bgsizex - (bgsizey - fillnesssizey);

	public LifeBar() {
		
		Image bg = new Image( GFX.get(IMG.BOX) );
		bg.setColor(GameColor.ROZOVIY.get());
		bg.setSize(bgsizex, bgsizey);
		bg.setPosition(-bg.getWidth()/2, -bg.getHeight()/2);


		fillness = new Image( GFX.get(IMG.BOX) );
		fillness.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.75f);
		fillness.setSize(fillnesssizex, fillnesssizey);
		fillness.setPosition(-bg.getWidth()/2 + (bgsizey - fillnesssizey)/2, -bg.getHeight()/2 + (bgsizey - fillnesssizey)/2);
		fillness.setOrigin(0, 0);
		fillness.setScaleX(1.0f);

		this.addActor(bg);
		this.addActor(fillness);
	}

}
