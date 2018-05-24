package net.overmy.szv.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.overmy.szv.Core;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.GFX.IMG;

public class PlayerBody extends Group {
	
	final float size = ((Core.WIDTH - Core.HEIGHT) / 2 * 0.8f) / Core.ASPECT_RATIO;
	
	Group body;
	Group face;
	
	enum FACE {
		IDLE,
		NYA,
		OPEN_MOUTH,
		BLINK,
		LAUGH,
		SAD
	};
	


	
	//public Color bodyColor = Core.zombieColor[1 + Core.rnd.nextInt(Core.zombieColor.length-1)];

	public void setHeadColor( final Color newColor ) {
		playerHead.setColor(newColor);
	}
	

	Image playerHead;
	
	
	public PlayerBody( final int bodyType, final Color bodyColor ) {
		body = new Group();
		face = new Group();
		

		playerHead = new Image( GFX.get(IMG.values()[IMG.BODY1.ordinal() + bodyType]) );
		
		playerHead.setSize(size, size);
		playerHead.setPosition(-playerHead.getWidth()/2, -playerHead.getHeight()/2);
		playerHead.setOrigin(playerHead.getWidth()/2, -playerHead.getHeight()/6);
		playerHead.setColor(bodyColor);

		final int MAX_FACES = 6;
		
		Image playerFace;
		playerFace = new Image( GFX.get(IMG.values()[IMG.FACE1.ordinal() + Core.rnd.nextInt(MAX_FACES)]) );
		playerFace.setSize(size * 0.8f, size * 0.8f);
		playerFace.setPosition(-playerFace.getWidth()/2, -playerFace.getHeight()/2);
		face.addActor(playerFace);
		
		body.addActor(playerHead);
		body.addActor(face);
		body.addAction(Actions.forever(Actions.sequence(
				Actions.rotateTo(4, Core.randomPercent(0.7f, 2), Interpolation.fade),
				Actions.rotateTo(-4, Core.randomPercent(0.7f, 2), Interpolation.fade),
				Actions.rotateTo(6, Core.randomPercent(0.7f, 2), Interpolation.fade),
				Actions.rotateTo(-6, Core.randomPercent(0.7f, 2), Interpolation.fade)
			)));

		
		this.addActor(body);
	}
	
	
	
	
	public PlayerBody() {
		body = new Group();
		face = new Group();
		

		if( Core.rnd.nextBoolean() )
			playerHead = new Image( GFX.get(IMG.BODY1) );
		else
			playerHead = new Image( GFX.get(IMG.BODY2) );
		
		playerHead.setSize(size, size);
		playerHead.setPosition(-playerHead.getWidth()/2, -playerHead.getHeight()/2);
		playerHead.setOrigin(playerHead.getWidth()/2, -playerHead.getHeight()/6);
		//playerHead.setColor(bodyColor);

		final int MAX_FACES = 6;
		
		Image playerFace;
		playerFace = new Image( GFX.get(IMG.values()[IMG.FACE1.ordinal() + Core.rnd.nextInt(MAX_FACES)]) );
		playerFace.setSize(size * 0.8f, size * 0.8f);
		playerFace.setPosition(-playerFace.getWidth()/2, -playerFace.getHeight()/2);
		face.addActor(playerFace);
		
		body.addActor(playerHead);
		body.addActor(face);
		body.addAction(Actions.forever(Actions.sequence(
				Actions.rotateTo(4, Core.randomPercent(0.7f, 2), Interpolation.fade),
				Actions.rotateTo(-4, Core.randomPercent(0.7f, 2), Interpolation.fade),
				Actions.rotateTo(6, Core.randomPercent(0.7f, 2), Interpolation.fade),
				Actions.rotateTo(-6, Core.randomPercent(0.7f, 2), Interpolation.fade)
			)));

		
		this.addActor(body);
	}
	
	
	public void updateFace2( final int n ) {
		
		FACE f = FACE.values()[n];
		face.clear();
		
		Image playerFace;
		playerFace = new Image( GFX.get(IMG.values()[IMG.FACE1.ordinal() + f.ordinal()]) );
		playerFace.setSize(size * 0.8f, size * 0.8f);
		playerFace.setPosition(-playerFace.getWidth()/2, -playerFace.getHeight()/2);
		face.addActor(playerFace);
	}
	
	public void updateFace( final FACE f ) {
		face.clear();
		
		Image playerFace;
		playerFace = new Image( GFX.get(IMG.values()[IMG.FACE1.ordinal() + f.ordinal()]) );
		playerFace.setSize(size * 0.8f, size * 0.8f);
		playerFace.setPosition(-playerFace.getWidth()/2, -playerFace.getHeight()/2);
		face.addActor(playerFace);
	}
	
}
