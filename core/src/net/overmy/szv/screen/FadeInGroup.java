package net.overmy.szv.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.overmy.szv.Core;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.GFX.IMG;

public class FadeInGroup extends Group {

	public FadeInGroup( final Color color ) {
		final Group tmp = new Group();
		
		final Runnable clear = new Runnable() {
			@Override
			public void run() {
				tmp.clear();
			}
		};	

		Image img = new Image(GFX.get(IMG.BOX));
		img.setColor(new Color(color.r, color.g, color.b, 0));
		img.setSize(Core.WIDTH,Core.HEIGHT);
		tmp.addActor(img);
		
		img.addAction(Actions.sequence(
				Actions.color(new Color(color.r, color.g, color.b, 1), Core.FADE_TIME),
				Actions.delay(Core.FADE_TIME),
				Actions.run(clear)
			));
		
		this.addActor(tmp);
	}

	
}
