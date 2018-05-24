package net.overmy.szv.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import net.overmy.szv.Core;
import net.overmy.szv.MyGdxGame;
import net.overmy.szv.MyGdxGame.SCREEN_ID;
import net.overmy.szv.Settings;
import net.overmy.szv.logic.PlayerBody;
import net.overmy.szv.neatresources.Fonts;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.GameColor;
import net.overmy.szv.neatresources.GameTranslation;
import net.overmy.szv.neatresources.GFX.IMG;
import net.overmy.szv.neatresources.SoundFX;

public class ScriptScene extends Base2D {

	Background				bg;
	
	Group popupAnimationGroup = new Group();
	Group scriptGroup = new Group();
	
	
	
	class LevelScript {
		String text;
		int delay;
		public LevelScript( final String newText, final int newDelay ){
			delay = newDelay;
			text = newText;
		}
	}
	
	
	
	LevelScript []script1 = {
			
		new LevelScript(GameTranslation.HI_BABY.get(), 3),	
		new LevelScript(GameTranslation.XZ.get(), 2),
		new LevelScript(GameTranslation.HEY_BOY.get(), 3),	
		new LevelScript(GameTranslation.XZ.get(), 2),
		new LevelScript(GameTranslation.WHAT.get(), 2),
		new LevelScript(GameTranslation.XZ.get(), 1),
		new LevelScript(GameTranslation.IWL_LEARN.get(), 3),
		
	};
	
	LevelScript []script2 = {
			
		new LevelScript(GameTranslation.HI_THERE.get(), 2),
		new LevelScript(GameTranslation.HI_THERE.get(), 3),	
		new LevelScript(GameTranslation.FIGHT.get(), 2),
		new LevelScript(GameTranslation.AKEI.get(), 2),
		
	};
	
	LevelScript []script3 = {
			
		new LevelScript(GameTranslation.KUNG_FU.get(), 3),
		new LevelScript(GameTranslation.NOPE.get(), 3),
		
	};
	
	LevelScript []script4 = {
			
		new LevelScript(GameTranslation.HI_BABY.get(), 2),
		new LevelScript(GameTranslation.YOU_BABY.get(), 3),
		new LevelScript(GameTranslation.SEE.get(), 2),
		
	};
	
	LevelScript []script5 = {
			
		new LevelScript(GameTranslation.HI_THERE.get(), 2),
		new LevelScript(GameTranslation.AKEI.get(), 1),
		new LevelScript(GameTranslation.HI_BABY.get(), 2),
		new LevelScript(GameTranslation.FIGHT.get(), 2),
		
	};
	
	LevelScript []script6 = {
			
		new LevelScript(GameTranslation.FIGHT.get(), 2),
		new LevelScript(GameTranslation.KUNG_FU.get(), 3),
		new LevelScript(GameTranslation.OK.get(), 1),
		
	};
	
	LevelScript []script7 = {
			
		new LevelScript(GameTranslation.FIGHT.get(), 2),
		new LevelScript(GameTranslation.WHAT.get(), 1),
		new LevelScript(GameTranslation.KUNG_FU.get(), 3),
		new LevelScript(GameTranslation.SEE.get(), 1),
		
	};
	
	LevelScript []script8 = {
			
		new LevelScript(GameTranslation.HI_THERE.get(), 1),
		new LevelScript(GameTranslation.HI_BABY.get(), 1),
		new LevelScript(GameTranslation.YOU_BABY.get(), 2),
		
	};
	
	LevelScript []script9 = {
			
		new LevelScript(GameTranslation.HI_BABY.get(), 1),
		new LevelScript(GameTranslation.HI_THERE.get(), 1),
		
	};
	
	LevelScript []script10 = { // game over!
			
		new LevelScript(GameTranslation.I_AM1.get(), 3),
		new LevelScript(GameTranslation.I_AM2.get(), 5),
		new LevelScript(GameTranslation.I_AM3.get(), 4),
		new LevelScript(GameTranslation.GAME_OVER.get(), 6),
		
	};
	
	LevelScript []currentScript;
	
	
	
	int p = 0; // pointer to current script text

	
	PlayerBody pb1, pb2;
	
	final float JUMP_SPEED = 0.24f;
	Vector2 defaultPosition1 = new Vector2();
	Vector2 defaultPosition2 = new Vector2();
	
	
	public ScriptScene(MyGdxGame game) {
		super(game);
		Gdx.app.log("ScriptScene", "constructor");
		
		layerTop.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					SoundFX.play(1);
					layerTop.addAction(Actions.run(runMenuScreen));
				}	
				return true;
			}
		});
		
		layerBottom.addActor( bg = new Background() ); // new background!!!!!!

		// pick up level settings !!!!!!!!!! TODO
		
		switch(Settings.currentLevel){
			case 0: currentScript = script1; break;
			case 1: currentScript = script2; break;
			case 2: currentScript = script3; break;
			case 3: currentScript = script4; break;
			case 4: currentScript = script5; break;
			case 5: currentScript = script6; break;
			case 6: currentScript = script7; break;
			case 7: currentScript = script8; break;
			case 8: currentScript = script9; break;
			case 9: currentScript = script10; break;
			default: currentScript = script1; break;
		}
		
		
		if(Settings.currentLevel != 9) {
			defaultPosition1.set(Core.WIDTH * 0.2f, Core.HEIGHT * 0.1f);
			layerBottom.addAction( Actions.run(processScripts) );
		} else {
			defaultPosition1.set(Core.WIDTH * 0.5f, Core.HEIGHT * 0.15f);
			layerBottom.addAction( Actions.run(processLastScripts) );
		}
		
		layerTop.addActor(popupAnimationGroup);
		layerTop.addActor(scriptGroup);
		
		pb1 = new PlayerBody( 1, GameColor.HERO.get() );
		pb1.setHeadColor(GameColor.HERO.get());
		
		defaultPosition2.set(Core.WIDTH * 0.8f, Core.HEIGHT * 0.1f);
		
		
		pb1.setPosition(defaultPosition1.x, defaultPosition1.y);
		
		layerTop.addActor(pb1);

		final Color enemyColor = Core.rnd.nextBoolean() ? GameColor.ZOMBIE1.get() : GameColor.ZOMBIE2.get();
		
		if(Settings.currentLevel != 9) {
			pb2 = new PlayerBody( 
					Core.levelSettings[Settings.currentLevel].enemyType, 
					enemyColor );
			pb2.setPosition(defaultPosition2.x, defaultPosition2.y);
			layerTop.addActor(pb2);
		}
		
		
		
		layerTop.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				if(Settings.currentLevel < 9) baseGame.switchScreen( SCREEN_ID.GAME );
			}
		});
		
		layerTop.addActor(new FadeOutGroup(Color.BLACK));
	}
	
	
	
	@Override
	public void dispose() {
		Gdx.app.log("ScriptScene", "dispose");
		
		bg.clear();
		
		super.dispose();
	}
	
	
	
	
	
	int PopUpCounter = 0;
	
	
	public void addPopup( final String text, final int x, final int y, float time ) {
		PopUpCounter++;
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Fonts.CAVIAN_DREAMS_40;
		labelStyle.fontColor = GameColor.DARK_GREEN.get();
		
		Label textLabel = new Label( text, labelStyle );
		textLabel.setPosition(-textLabel.getWidth()/2, -textLabel.getHeight()/2);
		textLabel.setAlignment(Align.center);

		Image bg = new Image( GFX.get(IMG.BOX) );
		bg.setSize(textLabel.getWidth() * 1.4f, textLabel.getHeight() * 1.2f);
		bg.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.8f);
		bg.setPosition(-bg.getWidth()/2, -bg.getHeight()/2);
		bg.setOrigin(bg.getWidth()/2, bg.getHeight()/2);
		
		final Group tmp = new Group();
		
		final Runnable selfRemove = new Runnable() {
			@Override
			public void run() {
				PopUpCounter--;
				if(PopUpCounter == 0) popupAnimationGroup.clear();
			}
		};
		
		tmp.setPosition(x, y);
		
		tmp.addActor(bg);
		tmp.addActor(textLabel);
		tmp.setScale(0, 0);
		tmp.addAction(Actions.sequence(
				Actions.scaleTo(1, 1, 0.5f, Interpolation.fade),
				Actions.delay(time - 1.0f),
				Actions.scaleTo(0, 0, 0.5f, Interpolation.fade),
				Actions.run(selfRemove)
			));
		
		popupAnimationGroup.addActor(tmp);
	}
	
	final Runnable runScriptText = new Runnable() {
		@Override
		public void run() {
			
			if(Settings.currentLevel != 9)
			if(p > currentScript.length-1) return;
			
			SoundFX.play(10 + Core.rnd.nextInt(6));
			
			pb1.updateFace2(Core.rnd.nextInt(6));
			pb2.updateFace2(Core.rnd.nextInt(6));
			
			if( p % 2 == 0 ){
				addPopup( currentScript[p].text, Core.WIDTH_HALF/2, (int) (Core.HEIGHT * 0.75f), currentScript[p].delay );
				float size = (Core.WIDTH - Core.HEIGHT) / 8;
				
				pb1.clearActions();
				pb1.addAction(Actions.forever(
						Actions.parallel(
							Actions.sequence(
									Actions.moveTo(defaultPosition1.x, defaultPosition1.y + size, JUMP_SPEED, Interpolation.circleOut),
									Actions.moveTo(defaultPosition1.x, defaultPosition1.y, JUMP_SPEED, Interpolation.circleIn)
							),
							Actions.sequence(
									Actions.scaleTo(1 + 0.1f, 1 + 0.1f, JUMP_SPEED, Interpolation.circleOut),
									Actions.scaleTo(1, 1, JUMP_SPEED, Interpolation.circleIn)
							)
						)
					));
				
				pb2.clearActions();
				pb2.addAction(
						Actions.parallel(
								Actions.moveTo(defaultPosition2.x, defaultPosition2.y, Core.randomPercent(0.1f, JUMP_SPEED), Interpolation.circle),
								Actions.scaleTo(1, 1, Core.randomPercent(0.1f, JUMP_SPEED), Interpolation.circle)
							));

			} else {
				addPopup( currentScript[p].text, Core.WIDTH_HALF/2+Core.WIDTH_HALF, (int) (Core.HEIGHT * 0.75f), currentScript[p].delay );
				float size = (Core.WIDTH - Core.HEIGHT) / 8;
				
				pb2.clearActions();
				pb2.addAction(Actions.forever(
						Actions.parallel(
							Actions.sequence(
									Actions.moveTo(defaultPosition2.x, defaultPosition2.y + size, JUMP_SPEED, Interpolation.circleOut),
									Actions.moveTo(defaultPosition2.x, defaultPosition2.y, JUMP_SPEED, Interpolation.circleIn)
							),
							Actions.sequence(
									Actions.scaleTo(1 + 0.1f, 1 + 0.1f, JUMP_SPEED, Interpolation.circleOut),
									Actions.scaleTo(1, 1, JUMP_SPEED, Interpolation.circleIn)
							)
						)
					));
				
				pb1.clearActions();
				pb1.addAction(
						Actions.parallel(
								Actions.moveTo(defaultPosition1.x, defaultPosition1.y, Core.randomPercent(0.1f, JUMP_SPEED), Interpolation.circle),
								Actions.scaleTo(1, 1, Core.randomPercent(0.1f, JUMP_SPEED), Interpolation.circle)
							));
			}
			
		}
	};	
	

	final Runnable runLastScriptText = new Runnable() {
		@Override
		public void run() {
			
			pb1.updateFace2(Core.rnd.nextInt(5));
			
			if(p > currentScript.length-1) return;
			
			SoundFX.play(10 + Core.rnd.nextInt(6));

			addPopup( currentScript[p].text, Core.WIDTH_HALF, (int) (Core.HEIGHT * 0.75f), currentScript[p].delay );
			float size = (Core.WIDTH - Core.HEIGHT) / 8;
			
			pb1.clearActions();
			pb1.addAction(Actions.forever(
					Actions.parallel(
						Actions.sequence(
								Actions.moveTo(defaultPosition1.x, defaultPosition1.y + size, JUMP_SPEED, Interpolation.circleOut),
								Actions.moveTo(defaultPosition1.x, defaultPosition1.y, JUMP_SPEED, Interpolation.circleIn)
						),
						Actions.sequence(
								Actions.scaleTo(1 + 0.1f, 1 + 0.1f, JUMP_SPEED, Interpolation.circleOut),
								Actions.scaleTo(1, 1, JUMP_SPEED, Interpolation.circleIn)
						)
					)
				));
			
		}
	};	
	
	final Runnable processLastScripts = new Runnable() {
		@Override
		public void run() {
			
			if( p > currentScript.length-1 ){
				Settings.currentLevel = 0;
				scriptGroup.clear();
				scriptGroup.addAction(
						Actions.sequence(
								Actions.delay(Core.FADE_TIME),
								Actions.run(runMenuScreen)
							));
				layerTop.addActor(new FadeInGroup(Color.BLACK));
				return;
			}

			scriptGroup.clear();
			scriptGroup.addAction(
					Actions.sequence(
							Actions.run(runLastScriptText),
							Actions.delay(currentScript[p].delay),
							Actions.run(pPlus),
							
							Actions.run(processLastScripts)
						));
			
		}
	};	
	
	
	final Runnable processScripts = new Runnable() {
		@Override
		public void run() {
			
			if( p > currentScript.length-1 ){
				scriptGroup.clear();
				scriptGroup.addAction(
						Actions.sequence(
								Actions.delay(Core.FADE_TIME),
								Actions.run(runGameScreen)
							));
				layerTop.addActor(new FadeInGroup(Color.BLACK));
				return;
			}

			scriptGroup.clear();
			scriptGroup.addAction(
					Actions.sequence(
							Actions.run(runScriptText),
							Actions.delay(currentScript[p].delay),
							Actions.run(pPlus),
							
							Actions.run(processScripts)
						));
			
		}
	};	
	
	final Runnable pPlus = new Runnable() {
		@Override
		public void run() {
			
			p++;
			
		}
	};	
	
	final Runnable runGameScreen = new Runnable() {
		@Override
		public void run() {
			baseGame.switchScreen( SCREEN_ID.GAME );
		}
	};	
	
	final Runnable runMenuScreen = new Runnable() {
		@Override
		public void run() {
			baseGame.switchScreen( SCREEN_ID.MENU );
		}
	};
	
	
	
	

}
