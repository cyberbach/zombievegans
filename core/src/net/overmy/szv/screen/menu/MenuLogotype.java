package net.overmy.szv.screen.menu;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

import net.overmy.szv.neatresources.Fonts;
import net.overmy.szv.neatresources.GameTranslation;

public class MenuLogotype extends Group {

	public Label		label;
	
	public MenuLogotype() {
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Fonts.CAVIAN_DREAMS_70;
		//labelStyle.fontColor = Core.color[14];
		
		label = new Label( GameTranslation.TITLE.get(), labelStyle );
		label.setAlignment(Align.center);

		label.setPosition(-label.getWidth()/2, -label.getHeight()/2);
		
		
		
		this.addActor(label);
	}
	
}
