package net.overmy.szv.screen.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.overmy.szv.Core;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.GameColor;
import net.overmy.szv.neatresources.GFX.IMG;

public class MenuBG extends Group {
	
	Image[] line;
	final int count = 25;
	
	public MenuBG() {
		
		line = new Image[count];
		
		for( int i = 0; i < count; i++ ){
			final float y = Core.rnd.nextFloat() * Core.HEIGHT * 0.9f;
			final float t = 3.5f + Core.rnd.nextFloat() * 10;
			final float h = Core.HEIGHT_HALF / 20 +  Core.rnd.nextFloat() * Core.HEIGHT_HALF / 2;

			final float startx = Core.rnd.nextFloat() * Core.WIDTH;

			line[i] = new Image( GFX.get(IMG.BOX) );
			line[i].setWidth(Core.WIDTH);
			line[i].setHeight(h);
			line[i].setColor(Color.BLACK);
			line[i].setPosition(startx-Core.WIDTH, y);
			
			line[i].addAction(Actions.forever(Actions.parallel(
					Actions.sequence(
							Actions.moveTo(0, y, t/2),
							Actions.moveTo(Core.WIDTH, y, t/2),
							Actions.moveTo(-Core.WIDTH, y, 0)
						),
					Actions.sequence(
							Actions.color(GameColor.BLUE1.get(), t/2),
							Actions.color(Color.BLACK, t/2),
							Actions.color(Color.BLACK, 0)
						)
				)));
		}
		
		for( int i = 0; i < count; i++ )
			this.addActor( line[i] );
	}
	
}
