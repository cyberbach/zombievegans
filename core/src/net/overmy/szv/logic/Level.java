package net.overmy.szv.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

import net.overmy.szv.Core;
import net.overmy.szv.Settings;
import net.overmy.szv.logic.PlayerBody.FACE;
import net.overmy.szv.neatresources.Fonts;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.SoundFX;
import net.overmy.szv.neatresources.GFX.IMG;
import net.overmy.szv.neatresources.GameColor;
import net.overmy.szv.neatresources.GameTranslation;

public class Level {
	
	public boolean TURN = false;
	
	
	Field field = null;
	public Player hero = null;
	public Player enemy = null;
	

	// поля гуи

	Group scoreAnimationGroup = new Group();
	Group popupAnimationGroup = new Group();
	final float SCORE_SPEED = 1.2f;
	
	

	
	
	
	public Level() {
		
		hero = new Player( true, 1, GameColor.HERO.get() ){
			@Override
			void shakeAll() { shakeLevel(); }
			@Override
			void dieSomebody() { gameOver(); }
		};
		
		final Color enemyColor = Core.rnd.nextBoolean() ? GameColor.ZOMBIE1.get() : GameColor.ZOMBIE2.get();
		
		enemy = new Player( false, Core.levelSettings[Settings.currentLevel].enemyType, enemyColor ){
			@Override
			void shakeAll() { shakeLevel(); }
			@Override
			void dieSomebody() { gameOver(); }
		};
		
		enemy.life = Core.levelSettings[Settings.currentLevel].enemyLife;
		
		createMagicButtons();
		
		field = new Field(){
			@Override
			void afterShowHelp() { scoreAnimationGroup.clear(); }
			@Override
			void animateDeletedCell(int x, int y, int cellType) { convertCellToScore(x, y, cellType); }
			@Override
			boolean swapTurn() { return TURN = swapTurns(); }
			@Override
			void addTurn() { if( TURN ) hero.extra_turns++; else enemy.extra_turns++; }
			@Override
			void patVarning() { addPopup(GameTranslation.NO_VARS.get(), Align.center, 2.5f); }
		};
		
		scoreAnimationGroup.addAction(Actions.sequence(
				Actions.run(new Runnable() {
					@Override
					public void run() {
						field.firstStart();
					}
			
				})
			));
		
		
		addPopup(GameTranslation.LEVEL_SIZE.get() + (Settings.currentLevel+1), Align.center, 4.5f);
		
	}

	private void gameOver() {
		Gdx.app.log("game", "over");
		
		field.gameOverFLAG = true;
		field.animationGroup.clear();
		field.animationGroup.addAction(Actions.run(field.levelDestroy));
		field.helpGroup.clear();
		field.tempGroup.clear();
		
		hero.lifeBar.addAction(Actions.alpha(0, 1));
		enemy.lifeBar.addAction(Actions.alpha(0, 1));
		
		for(int i = 0; i < 3; i++){
			hero.magic.get(i).bar.addAction(Actions.alpha(0, 1));
			enemy.magic.get(i).bar.addAction(Actions.alpha(0, 1));
		}

		if( hero.life > 0 ) { // win
			
			final float WIN_TIME = 5.0f;
			
			hero.life = hero.MAX_LIFE;
			hero.body.updateFace(FACE.LAUGH);
			hero.setActive();
			
			Image light = new Image( GFX.get(IMG.LIGHT) );
			light.setPosition(-light.getWidth()/2, -light.getHeight()/2);
			light.setOrigin(light.getWidth()/2, light.getHeight()/2);
			
			light.addAction(Actions.rotateTo(720, WIN_TIME));
			
			Image light2 = new Image( GFX.get(IMG.ERCIRCLE) );
			light2.setPosition(-light2.getWidth()/2, -light2.getHeight()/2);
			light2.setOrigin(light2.getWidth()/2, light2.getHeight()/2);

			light2.addAction(Actions.rotateTo(-360, WIN_TIME));

			Group tmp1 = new Group();
			tmp1.addActor(light);
			tmp1.setPosition(Core.WIDTH_HALF, Core.HEIGHT_HALF);
			tmp1.addAction(Actions.sequence(
					Actions.scaleTo(1, 1, 0.5f, Interpolation.fade),
					Actions.delay(WIN_TIME - 1.5f),
					Actions.scaleTo(0, 0, 0.5f, Interpolation.fade)
				));

			Group tmp2 = new Group();
			tmp2.addActor(light2);
			tmp2.setPosition(Core.WIDTH_HALF, Core.HEIGHT_HALF);
			tmp2.addAction(Actions.sequence(
					Actions.scaleTo(1, 1, 0.5f, Interpolation.fade),
					Actions.delay(WIN_TIME - 1.5f),
					Actions.scaleTo(4, 4, 0.5f, Interpolation.fade)
				));
			
			
			popupAnimationGroup.addActor(tmp1);
			popupAnimationGroup.addActor(tmp2);
			
			addPopup( GameTranslation.LEVEL_COML.get(), Align.center, WIN_TIME );
			
			scoreAnimationGroup.clear();
			scoreAnimationGroup.addAction(Actions.sequence(
					Actions.run(magicSound),
					Actions.delay(1),
					Actions.run(winSound),
					Actions.delay(3),
					Actions.run(restart)
				));
			
			
			
		} else { // fail
			
			Settings.currentLevel = 0;
			enemy.life = enemy.MAX_LIFE;
			enemy.body.updateFace(FACE.LAUGH);
			enemy.setActive();
			addPopup( GameTranslation.LEVEL_LOOSE.get(), Align.center, 5 );
			
			
			scoreAnimationGroup.clear();
			scoreAnimationGroup.addAction(Actions.sequence(
					Actions.delay(1),
					Actions.run(failSound),
					Actions.delay(3),
					Actions.run(finish)
				));
			
		}
	}
	final Runnable magicSound = new Runnable() {
		@Override
		public void run() {
			SoundFX.play(18);
		}

	};
	final Runnable winSound = new Runnable() {
		@Override
		public void run() {
			SoundFX.play(16);
		}

	};
	final Runnable failSound = new Runnable() {
		@Override
		public void run() {
			SoundFX.play(17);
		}

	};
	final Runnable restart = new Runnable() {
		@Override
		public void run() {
			restart();
		}

	};
	final Runnable finish = new Runnable() {
		@Override
		public void run() {
			finish();
		}

	};
	public void restart() {}
	public void finish() {}
	
	

	void shakeLevel() {
		final float dx = Core.HEIGHT_HALF / 10;
		
		final Action shake1 = Actions.sequence(
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.04f)),
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.03f)),
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.04f)),
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.03f)),
				Actions.moveTo(0, 0, 0.04f)
			);
		
		final Action shake2 = Actions.sequence(
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.03f)),
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.04f)),
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.03f)),
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.04f)),
				Actions.moveTo(0, 0, 0.04f)
			);
		
		final Action shake3 = Actions.sequence(
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.04f)),
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.04f)),
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.03f)),
				Actions.moveTo(Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.1f, dx - dx * 2 * Core.rnd.nextFloat()), Core.randomPercent(0.7f, 0.03f)),
				Actions.moveTo(0, 0, 0.04f)
			);
		
		field.getFieldGroup().addAction( shake1 );
		hero.fullGroup.addAction( shake2 );
		enemy.fullGroup.addAction( shake3 );
		
		SoundFX.play(11 + Core.rnd.nextInt(3));
		
		//SoundFX.explosion1.play();
	}


	void createMagicButtons() {
		boolean[] magicCreateAvailable;
		magicCreateAvailable = new boolean[Core.MAX_CELL_VALUES];
		for( int i = 0; i < Core.MAX_CELL_VALUES; i++ ) magicCreateAvailable[i] = true;

		int n;
		for(int i = 0; i < 3; i++){
			while( true ){
				n = Core.rnd.nextInt(Core.MAX_CELL_VALUES);
				if(magicCreateAvailable[n]) break;
			}
			magicCreateAvailable[n] = false;
			hero.addMagic(n, enemy);
			hero.magic.get(i).damage = i+1;
			hero.magic.get(i).bar.text.setText(""+(hero.magic.get(i).damage));
			Core.cellsPower[i * 2] = n;
		}
		for(int i = 0; i < 3; i++){
			while( true ){
				n = Core.rnd.nextInt(Core.MAX_CELL_VALUES);
				if(magicCreateAvailable[n]) break;
			}
			magicCreateAvailable[n] = false;
			enemy.addMagic(n, hero);
			enemy.magic.get(i).damage = i+1;
			enemy.magic.get(i).bar.text.setText(""+(enemy.magic.get(i).damage));
			Core.cellsPower[i * 2 + 1] = n;
		}
		/*
		String s = "";
		for( int i=0; i<6;i++){
			s += Core.cellsPower[i] + " ";
		}
		Gdx.app.log("magic", ""+s);*/
	}
	
	
	
	public Group getGroups() {
		Group tempGroup = new Group();
		tempGroup.addActor(field.getFieldGroup());
		
		tempGroup.addActor(hero.getGroups());
		tempGroup.addActor(enemy.getGroups());
		tempGroup.addActor(scoreAnimationGroup);
		tempGroup.addActor(popupAnimationGroup);
		tempGroup.addActor(hero.throwAnimationGroup);
		tempGroup.addActor(enemy.throwAnimationGroup);
		return tempGroup;
		
	}

	public boolean swapTurns() {
		//

		if( TURN ){
			if(hero.extra_turns > 0){
				hero.extra_turns--;
				addPopup(GameTranslation.YOUR_EX_TURN.get(), Align.bottom, 3.0f);
				hero.setActive();
				enemy.setInactive();
				hero.clickBlocking_FLAG = false;
				hero.body.updateFace(FACE.BLINK);
				if(hero.isMagicReady()) enemy.body.updateFace(FACE.SAD); else enemy.body.updateFace(FACE.NYA);
				return true;
			}
			addPopup(GameTranslation.EN_TURN.get(), Align.bottom, 2.0f);
			enemy.setActive();
			hero.setInactive();
			hero.clickBlocking_FLAG = true;
			if(enemy.useMagic(hero)) {
				enemy.body.updateFace(FACE.LAUGH); 
				hero.body.updateFace(FACE.SAD); 
			} else {
				enemy.body.updateFace(FACE.OPEN_MOUTH); 
				hero.body.updateFace(FACE.IDLE);
			}
			return false;
		} else {
			if(enemy.extra_turns > 0){
				enemy.extra_turns--;
				addPopup(GameTranslation.EN_EX_TURN.get(), Align.bottom, 3.0f);
				enemy.setActive();
				hero.setInactive();
				hero.clickBlocking_FLAG = true;
				if(enemy.useMagic(hero)) {
					enemy.body.updateFace(FACE.SAD); 
					hero.body.updateFace(FACE.LAUGH); 
				} else {
					enemy.body.updateFace(FACE.IDLE); 
					hero.body.updateFace(FACE.OPEN_MOUTH);
				}
				return false;
			}
			addPopup(GameTranslation.YOUR_TURN.get(), Align.bottom, 2);
			hero.setActive();
			enemy.setInactive();
			hero.clickBlocking_FLAG = false;
			hero.body.updateFace(FACE.OPEN_MOUTH);
			if(hero.isMagicReady()) enemy.body.updateFace(FACE.SAD); else enemy.body.updateFace(FACE.IDLE);
			return true;
		}
	}
	
	
	public void clear() {
		popupAnimationGroup.clear();
		scoreAnimationGroup.clear();
		field.clear();
		hero.clear();
		enemy.clear();
	}
	
	int PopUpCounter = 0;
	
	
	// REST FOR A FOOD
	
	public void addPopup( final String text, final int align, float time ) {
		PopUpCounter++;
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Fonts.CAVIAN_DREAMS_40;
		labelStyle.fontColor = GameColor.DARK_BROWN.get();
		
		Label textLabel = new Label( text, labelStyle );
		textLabel.setPosition(-textLabel.getWidth()/2, -textLabel.getHeight()/2);
		textLabel.setAlignment(Align.center);

		Image bg = new Image( GFX.get(IMG.BOX) );
		bg.setSize(textLabel.getWidth() * 1.4f, textLabel.getHeight() * 1.2f);
		bg.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.9f);
		bg.setPosition(-bg.getWidth()/2, -bg.getHeight()/2);
		bg.setOrigin(bg.getWidth()/2, bg.getHeight()/2);
		
		final Group tmp = new Group();
		
		final Runnable selfRemove = new Runnable() {
			@Override
			public void run() {
				PopUpCounter--;
				if(PopUpCounter == 0) {
					soundBlock = false;
					popupAnimationGroup.clear();
				}
			}
		};
		
		switch( align ){
			case Align.bottomRight: tmp.setPosition(Core.WIDTH_HALF, Core.HEIGHT_HALF/2); break;
			case Align.bottom: tmp.setPosition(Core.WIDTH_HALF, textLabel.getHeight() * 1.2f / 2); break;
			case Align.center: tmp.setPosition(Core.WIDTH_HALF, Core.HEIGHT_HALF); break;
		}
		
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
	
	boolean soundBlock = false;
	
	void PlaySound( final int n ) {
		if(soundBlock) return;
		
		final Runnable snd = new Runnable() {
			@Override
			public void run() {
				SoundFX.play(n);
			}

		};
		
		final Runnable soundUnblock = new Runnable() {
			@Override
			public void run() {
				soundBlock = false;
			}

		};
		
		popupAnimationGroup.addAction(Actions.sequence(
				Actions.run(snd),
				Actions.delay(0.7f),
				Actions.run(soundUnblock)
			));
		
		soundBlock = true;
	}
	
	
	void convertCellToScore( final int x, final int y, final int n ) {
		
		float PERSONA_X;
		float PERSONA_Y;
	

		if( TURN ) {
			PERSONA_X = hero.body.getX();
			PERSONA_Y = hero.body.getY();
			hero.addArray[n] += 1;
		} else {
			PERSONA_X = enemy.body.getX();
			PERSONA_Y = enemy.body.getY();
			enemy.addArray[n] += 1;
		}

		Group tmp = new Group();
		
		Image img = new Image( GFX.get(IMG.values()[IMG.ICON1.ordinal() + n]) );
		
		img.setSize(field.HEIGHT_BY_CELL,field.HEIGHT_BY_CELL);
		img.setPosition(-field.HEIGHT_BY_CELL/2, -field.HEIGHT_BY_CELL/2);
		img.setOrigin(field.HEIGHT_BY_CELL/2, field.HEIGHT_BY_CELL/2);
		img.setScale(0, 0);
		img.setColor(new Color(1,1,1,0));
		img.addAction( Actions.sequence(
				Actions.parallel(
						Actions.scaleTo(1, 1, Core.randomPercent(0.7f, SCORE_SPEED) * 0.5f, Interpolation.circleOut),
						Actions.color(new Color(1,1,1,1),Core.randomPercent(0.7f, SCORE_SPEED) * 0.5f, Interpolation.circleOut)
					),
				Actions.scaleTo(0, 0, Core.randomPercent(0.7f, SCORE_SPEED) * 0.5f, Interpolation.circleIn),
				Actions.run(confirmAddScore)
			));
		
		float dx = x * field.HEIGHT_BY_CELL + field.HEIGHT_BY_CELL/2 + field.DELTA_WIDTH;
		float dy = y * field.HEIGHT_BY_CELL + field.HEIGHT_BY_CELL/2;
		
		tmp.setPosition(dx, dy);
		
		tmp.addAction( Actions.parallel(
				Actions.moveTo(PERSONA_X, PERSONA_Y, Core.randomPercent(0.7f, SCORE_SPEED), Interpolation.fade),
				Actions.rotateTo(360 - 720 * Core.rnd.nextFloat(), SCORE_SPEED)
			));
		
		tmp.addActor(img);
		scoreAnimationGroup.addActor(tmp);
		
	}

	final Runnable confirmAddScore = new Runnable() {
		@Override
		public void run() {
			for( int i = 0; i < Core.MAX_CELL_VALUES; i++ ) {
				if(hero.addArray[i] > 0 ) {
					hero.magicScore[i] +=	1;
					hero.addArray[i] -= 1;
					
					
					if(hero.magicScore[i] >= 10) hero.magicScore[i] = 10;
					
					for( int a = 0; a < hero.magic.size(); a++ ) {
						if(hero.magic.get(a).type.ordinal() == i) {
							hero.magic.get(a).bar.fillness.setScaleX((float)hero.magicScore[i]/10.0f);
							if(hero.magicScore[i] >= 10) { 
								hero.magic.get(a).bar.addAction(Actions.scaleTo(1, 1, 1, Interpolation.fade));
							}
						} 
					}
					
					if( hero.addArray[i] == 0 ){
						for( int a = 0; a < hero.magic.size(); a++ ){
							if(hero.magic.get(a).type.ordinal() == i){
								if(hero.magicScore[i] >= 10) PlaySound(6 + Core.rnd.nextInt(2)); else
									PlaySound(5 + Core.rnd.nextInt(3));
							}
						}
						PlaySound(8 + Core.rnd.nextInt(3));
					}
					
					return;
				};
				if(enemy.addArray[i] > 0 ) {
					enemy.magicScore[i] +=	1;
					enemy.addArray[i] -= 1;

					if(enemy.magicScore[i] >= 10) enemy.magicScore[i] = 10;

					for( int a = 0; a < enemy.magic.size(); a++ ) {
						if(enemy.magic.get(a).type.ordinal() == i) {
							enemy.magic.get(a).bar.fillness.setScaleX((float)enemy.magicScore[i]/10.0f);
							if(enemy.magicScore[i] >= 10) {
								enemy.magic.get(a).bar.addAction(Actions.scaleTo(1, 1, 1, Interpolation.fade));
							}
						}
					}
					
					if( enemy.addArray[i] == 0 ){
						for( int a = 0; a < enemy.magic.size(); a++ ){
							if(enemy.magic.get(a).type.ordinal() == i){
								if(enemy.magicScore[i] >= 10) PlaySound(6 + Core.rnd.nextInt(2)); else
									PlaySound(5 + Core.rnd.nextInt(3));
							}
						}
						PlaySound(8 + Core.rnd.nextInt(3));
					}
					
					return;
				};
			}
			
		}
	};

}
