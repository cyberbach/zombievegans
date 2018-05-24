package net.overmy.szv.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.overmy.szv.Core;
import net.overmy.szv.logic.PlayerBody.FACE;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.ParticleFX;
import net.overmy.szv.neatresources.GFX.IMG;
import net.overmy.szv.neatresources.GameColor;

public class Player {

	// for override
	void shakeAll() { }
	void dieSomebody() { }

	
	
	
	
	boolean side; // true = left, false = right
	
	public int MAX_LIFE = 10;
	public int life = MAX_LIFE;
	
	public int score = 0;
	public int extra_turns = 0;

	public ArrayList<Magic> magic = null;
	
	public int[] magicScore = null;
	public int[] addArray = null;
	
	public PlayerBody body;
	Vector2 defaultPosition = new Vector2();
	
	public LifeBar lifeBar;

	final float JUMP_SPEED = 0.24f;
	
	public boolean clickBlocking_FLAG = false; // если TRUE, то отключение обработки клика на ВСЕХ клеточках
	
	Group throwAnimationGroup = new Group();
	
	public Group fullGroup = null;

	
	
	
	
	
	
	public void setActive(){
		float size = (Core.WIDTH - Core.HEIGHT) / 8;
		
		final float MAX_SCALE = (float)(life + 1) / (float)11; 
		
		body.clearActions();
		body.addAction(Actions.forever(
				Actions.parallel(
					Actions.sequence(
							Actions.moveTo(defaultPosition.x, defaultPosition.y + size, JUMP_SPEED, Interpolation.circleOut),
							Actions.moveTo(defaultPosition.x, defaultPosition.y, JUMP_SPEED, Interpolation.circleIn)
					),
					Actions.sequence(
							Actions.scaleTo(MAX_SCALE + 0.1f, MAX_SCALE + 0.1f, JUMP_SPEED, Interpolation.circleOut),
							Actions.scaleTo(MAX_SCALE, MAX_SCALE, JUMP_SPEED, Interpolation.circleIn)
					)
				)
			));
		
		for( int i = 0; i < magic.size(); i++ ) {
			magic.get(i).bar.addAction(Actions.alpha(1, Core.randomPercent(0.1f, JUMP_SPEED)));
		}
	}
	
	
	
	public void setInactive(){
		float MAX_SCALE = (float)(life + 1) / (float)11;
		if(life == 0) MAX_SCALE = 0;
		body.clearActions();
		body.addAction(
				Actions.parallel(
						Actions.moveTo(defaultPosition.x, defaultPosition.y, Core.randomPercent(0.1f, JUMP_SPEED), Interpolation.circle),
						Actions.scaleTo(MAX_SCALE, MAX_SCALE, Core.randomPercent(0.1f, JUMP_SPEED), Interpolation.circle)
					));
		
		for( int i = 0; i < magic.size(); i++ ) {
			magic.get(i).bar.addAction(Actions.alpha(0.3f, Core.randomPercent(0.1f, JUMP_SPEED)));
		}
	}
	
	
	
	
	public Player( final boolean leftOrRight, final int bodyType, final Color bodyColor  ) {
		
		side = leftOrRight;

		magicScore = new int[Core.MAX_CELL_VALUES];
		addArray = new int[Core.MAX_CELL_VALUES];
		
		for( int i = 0; i < Core.MAX_CELL_VALUES; i++ ) {
			magicScore[i] = 0;
			addArray[i] = 0;
		}
		
		
		magic = new ArrayList<Magic>();
		
		body = new PlayerBody(bodyType, bodyColor);
		
		final float ds_x = (Core.WIDTH - Core.HEIGHT) / 4;
		final float ds_y = (Core.WIDTH - Core.HEIGHT) / 4.8f;

		if(side)
			defaultPosition.set(ds_x, ds_y);
		else
			defaultPosition.set(Core.WIDTH - ds_x, ds_y);
		
		body.setPosition(defaultPosition.x, defaultPosition.y);
		
		lifeBar = new LifeBar();
		lifeBar.setPosition(defaultPosition.x, lifeBar.bgsizey/1.5f);
	}
	
	final float THROW_SPEED = 0.6f;
	
	
	
	void ThrowFruit( final int n, final Player otherPlayer ) {
		final Runnable selfRemove = new Runnable() {
			@Override
			public void run() {
				throwAnimationGroup.clear();
				
				Gdx.app.log("throw 1="+life, "2="+otherPlayer.life);
				if((life < 1) || (otherPlayer.life < 1)) {
					dieSomebody();
				}
			}
		};
		
		//ParticleFX.setColor(Core.magicColor[n]);
		ParticleFX.setColor(GameColor.getByNumber(GameColor.fruitsOffset()+n));

		final Runnable particlesStarts = new Runnable() {
			@Override
			public void run() {
				ParticleFX.effect.setPosition(otherPlayer.defaultPosition.x, otherPlayer.defaultPosition.y);
				ParticleFX.effect.start();
				
				shakeAll();
				otherPlayer.setInactive();
			}
		};

		final float time = Core.randomPercent(0.7f, THROW_SPEED);

		Image fruit = new Image( GFX.get(IMG.values()[IMG.SMALL1.ordinal()+n]) );
		fruit.setPosition(-fruit.getWidth()/2, -fruit.getHeight()/2);
		fruit.setOrigin(fruit.getWidth()/2, fruit.getHeight()/2);
		fruit.setScale(0.4f, 0.4f);
		fruit.addAction(Actions.parallel(
				Actions.scaleTo( 1, 1, time ),
				Actions.rotateTo( 360 - 720 * Core.rnd.nextFloat(), Core.randomPercent(0.7f, THROW_SPEED) )
			));
		
		final Group tmp = new Group();
		tmp.addActor(fruit);
		tmp.setScale(0, 0);
		tmp.setPosition(defaultPosition.x, defaultPosition.y);
		tmp.addAction(Actions.sequence(
				// TODO throw sound
				Actions.scaleTo(1, 1, 0.1f),
				Actions.moveTo(otherPlayer.defaultPosition.x, otherPlayer.defaultPosition.y, time, Interpolation.circleIn ),
				Actions.run(particlesStarts),
				Actions.run(selfRemove)
			));
		
		throwAnimationGroup.addActor(tmp);
	}
	
	
	
	public Group getGroups() {
		if(fullGroup == null) fullGroup = new Group(); else fullGroup.clear();
		// TODO

		for( int i = 0; i < magic.size(); i++ )
			fullGroup.addActor(magic.get(i).bar);
		fullGroup.addActor(lifeBar);
		fullGroup.addActor(body);
		
		return fullGroup;
	}
	

	
	void addMagic( final int n, final Player otherPlayer ) {
		
		final Magic tmp = new Magic(n);
		
		final MagicBar tempBar = tmp.bar;
		
		tmp.bar.addListener( new ClickListener() {
			@Override
			public void clicked (InputEvent dontCareEvent, float dontCareX, float dontCareY) {
				if( !side ) return;
				if( clickBlocking_FLAG ) return;
				clickBlocking_FLAG = true;
				if(magicScore[n] > 9){
					magicScore[n] = 0;
					tempBar.fillness.setScaleX(0);
					tempBar.addAction(Actions.scaleTo(0.7f, 0.7f, 1, Interpolation.fade));
					body.updateFace(FACE.LAUGH);
					// use magic
					Gdx.app.log("damage", ""+tmp.damage);
					otherPlayer.life -= tmp.damage;
					otherPlayer.updateLife();
					ThrowFruit(n, otherPlayer);
				}
				clickBlocking_FLAG = false;
			}
		});
		
		tmp.bar.addListener(new ActorGestureListener() {
			public void fling (InputEvent event, float velocityX, float velocityY, int button) {
				if( !side ) return;
				if( clickBlocking_FLAG ) return;
				clickBlocking_FLAG = true;
				if(magicScore[n] > 9){
					magicScore[n] = 0;
					tempBar.fillness.setScaleX(0);
					tempBar.addAction(Actions.scaleTo(0.7f, 0.7f, 1, Interpolation.fade));
					body.updateFace(FACE.LAUGH);
					// use magic
					Gdx.app.log("damage", ""+tmp.damage);
					otherPlayer.life -= tmp.damage;
					otherPlayer.updateLife();
					ThrowFruit(n, otherPlayer);
				}
				clickBlocking_FLAG = false;
			}
		});
		
		magic.add( tmp );
		
		updateMagicPositions();
	}
	
	
	
	void updateLife() {
		if(life < 0) life = 0;
		final float dx = (float)life / (float)MAX_LIFE;
		lifeBar.fillness.addAction(Actions.sequence(
				Actions.delay(THROW_SPEED),
				Actions.scaleTo(dx, 1, 1.5f, Interpolation.fade)
			));
		


	}



	boolean useMagic( final Player otherPlayer ) {
		boolean status = false;
		for( int n = 0; n < magicScore.length; n++ )
		if(magicScore[n] > 9){
			magicScore[n] = 0;
			
			for( int i = 0; i < magic.size(); i++ ) {
				if( magic.get(i).type.ordinal() == n ){
					status = true;
					magic.get(i).bar.fillness.setScaleX(0);
					magic.get(i).bar.addAction(Actions.scaleTo(0.7f, 0.7f, 1, Interpolation.fade));
					// use magic
					Gdx.app.log("damage", ""+magic.get(i).damage);
					otherPlayer.life -= magic.get(i).damage;
					otherPlayer.updateLife();
					ThrowFruit(n, otherPlayer);
				}
			}
		}
		return status;
	}
	
	
	
	boolean isMagicReady() {
		boolean status = false;
		for( int n = 0; n < magicScore.length; n++ )
		if(magicScore[n] > 9){
			for( int i = 0; i < magic.size(); i++ ) {
				if( magic.get(i).type.ordinal() == n ){
					status = true;
				}
			}
		}
		return status;
	}
	
	
	
	void updateMagicPositions() {
		for( int i = 0; i < magic.size(); i++ ) {
			float dy = magic.get(i).bar.bgsizey * 1.1f * (i + 1);
			magic.get(i).bar.setPosition(defaultPosition.x, Core.HEIGHT - dy + magic.get(i).bar.bgsizey/2);
			
			magic.get(i).bar.setScale(0.7f, 0.7f);
		}
	}
	
	
	
	public void clear() {
		magicScore = null;
		addArray = null;
		magic = null;
		body.clear();
	}
	
}
