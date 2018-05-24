package net.overmy.szv.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.overmy.szv.Core;
import net.overmy.szv.Settings;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.GFX.IMG;
import net.overmy.szv.neatresources.GameColor;

public class Background extends Group {
	
	final int MAX_GROUND = 7;
	final int MAX_TREES = 9;
	final int MAX_CLOUDS = 5;
	final int MAX_SNOW = 25;
	
	final float CLOUDS_SPEED = 15.0f;
	final float SNOW_SPEED = 8.0f;
	
	
	
	
	
	public Background() {
		

		int currentLevel = Settings.currentLevel;
		
		Image sky = new Image(GFX.get(IMG.SKY));
		
		
		sky.setSize(Core.WIDTH, Core.HEIGHT);

		this.addActor( sky );

		switch(currentLevel){
			case 0: 
				sky.setColor(GameColor.LIGHT_BLUE.get());
				this.addActor( createMountains(GameColor.LIGHT_BLUE.get()) );
				this.addActor( createTrees(0, GameColor.SNOW_BLUE.get()) );
				this.addActor( createSubGround(GameColor.SNOW_BLUE.get()) );
				this.addActor( createSnow() );
				break;
				
			case 1: 
				sky.setColor(GameColor.LIGHT_YELLOW.get());
				this.addActor( createMountains(GameColor.BLUE_AQUA.get()) );
				this.addActor( createTrees(1, GameColor.BLUE_AQUA.get()) );
				this.addActor( createSubGround(GameColor.SNOW_BLUE.get()) );
				this.addActor( createClouds() );
				this.addActor( createSnow() );
				break;
				
			case 2: 
				sky.setColor(GameColor.SNOW_BLUE.get());
				this.addActor( createMountains(GameColor.DARK_BLUE.get()) );
				this.addActor( createTrees(0, GameColor.BLUE_AQUA.get()) );
				this.addActor( createSubGround(GameColor.SNOW_BLUE.get()) );
				this.addActor( createClouds() );
				break;
				
			case 3: 
				sky.setColor(GameColor.SNOW_BLUE.get());
				this.addActor( createMountains(GameColor.DARK_BLUE.get()) );
				this.addActor( createTrees(0, GameColor.GREEN1.get()) );
				this.addActor( createSubGround(GameColor.SNOW_BLUE.get()) );
				this.addActor( createClouds() );
				break;
				
			case 4: 
				sky.setColor(GameColor.BLUE1.get());
				this.addActor( createMountains(GameColor.DARK_BROWN.get()) );
				this.addActor( createTrees(0, GameColor.GREEN2.get()) );
				this.addActor( createSubGround(GameColor.DARK_BROWN.get()) );
				this.addActor( createClouds() );
				this.addActor( createLeaves(16) );
				break;
				
			case 5: // leto
				sky.setColor(GameColor.LIGHT_BLUE.get());
				this.addActor( createMountains(GameColor.DARK_BROWN.get()) );
				this.addActor( createTrees(1, GameColor.GREEN3.get()) );
				this.addActor( createSubGround(GameColor.DARK_BROWN.get()) );
				this.addActor( createClouds() );
				this.addActor( createLeaves(11) );
				break;
				
			case 6: 
				sky.setColor(GameColor.SAND.get());
				this.addActor( createTrees(2, GameColor.DARK_GREEN.get()) );
				this.addActor( createSubGround(GameColor.YELLOW1.get()) );
				this.addActor( createClouds() );
				break;
				
			case 7: 
				sky.setColor(GameColor.DARK_BLUE.get());
				this.addActor( createMountains(GameColor.YELLOW1.get()) );
				this.addActor( createTrees(2, GameColor.DARK_GREEN.get()) );
				this.addActor( createSubGround(GameColor.YELLOW1.get()) );
				this.addActor( createClouds() );
				break;
				
			case 8: 
				sky.setColor(GameColor.SNOW_BLUE.get());
				this.addActor( createMountains(GameColor.DARK_GREEN.get()) );
				this.addActor( createTrees(0, GameColor.YELLOW1.get()) );
				this.addActor( createSubGround(GameColor.DARK_GREEN.get()) );
				this.addActor( createClouds() );
				this.addActor( createLeaves(15) );
				break;
				
			case 9: 
				sky.setColor(GameColor.SNOW_BLUE.get());
				this.addActor( createMountains(GameColor.DARK_BLUE.get()) );
				this.addActor( createTrees(0, GameColor.LIGHT_BLUE.get()) );
				this.addActor( createSubGround(GameColor.DARK_BLUE.get()) );
				this.addActor( createClouds() );
				this.addActor( createSnow() );
				break;
				
			default:  break;
		}
		
		
	}


	Group createLeaves( final int color ) {
		Group tmp = new Group();
		
		for( int i = 0; i < MAX_SNOW; i++ ) {
			Group flakeGroup = new Group();
			
			Image leaf = new Image(GFX.get(IMG.values()[IMG.LEAF1.ordinal() + Core.rnd.nextInt(3)]));
			
			final float size = (Core.HEIGHT * 0.02f) + (Core.HEIGHT * 0.06f) * Core.rnd.nextFloat();
			final float x = Core.WIDTH * Core.rnd.nextFloat();
			final float y = Core.HEIGHT * Core.rnd.nextFloat();
			
			leaf.setSize(size, size);
			leaf.setOrigin(size/2, size/2);
			leaf.setColor(GameColor.getByNumber(color));
			//snowFlake.setPosition(-size/2, -size/2);
			
			leaf.addAction(Actions.forever(Actions.sequence(
					Actions.rotateTo(180.0f - 360.0f * Core.rnd.nextFloat(), Core.randomPercent(0.6f, 6), Interpolation.fade),
					Actions.rotateTo(0, Core.randomPercent(0.6f, 6), Interpolation.fade)
				)));
			
			final float ds = (float)x / (float)(Core.WIDTH+size);
			
			final float speed = Core.randomPercent(0.3f, SNOW_SPEED);
			
			flakeGroup.setPosition(x, y);
			flakeGroup.addAction(
					Actions.sequence(
						Actions.moveTo(x - Core.randomPercent(0.7f, 100), -size, speed * ds, Interpolation.linear),
						
						Actions.forever(Actions.sequence(
								Actions.moveTo(x, Core.HEIGHT + size, 0),
								Actions.moveTo(x - Core.randomPercent(0.7f, 100), -size, speed, Interpolation.linear)
							)
					)));
			
			flakeGroup.addActor(leaf);
			
			tmp.addActor(flakeGroup);
		}
		
		return tmp;
	}	
	
	

	Group createSnow() {
		Group tmp = new Group();
		
		for( int i = 0; i < MAX_SNOW; i++ ) {
			Group flakeGroup = new Group();
			
			Image snowFlake = new Image(GFX.get(IMG.values()[IMG.FLAKE1.ordinal() + Core.rnd.nextInt(5)]));
			
			final float size = (Core.HEIGHT * 0.02f) + (Core.HEIGHT * 0.06f) * Core.rnd.nextFloat();
			final float x = Core.WIDTH * Core.rnd.nextFloat();
			final float y = Core.HEIGHT * Core.rnd.nextFloat();
			
			snowFlake.setSize(size, size);
			snowFlake.setOrigin(size/2, size/2);
			//snowFlake.setPosition(-size/2, -size/2);
			
			snowFlake.addAction(Actions.forever(Actions.sequence(
					Actions.rotateTo(180.0f - 360.0f * Core.rnd.nextFloat(), Core.randomPercent(0.6f, 6), Interpolation.fade),
					Actions.rotateTo(0, Core.randomPercent(0.6f, 6), Interpolation.fade)
				)));
			
			final float ds = (float)x / (float)(Core.WIDTH+size);
			
			final float speed = Core.randomPercent(0.3f, SNOW_SPEED);
			
			flakeGroup.setPosition(x, y);
			flakeGroup.addAction(
					Actions.sequence(
						Actions.moveTo(x - Core.randomPercent(0.7f, 100), -size, speed * ds, Interpolation.linear),
						
						Actions.forever(Actions.sequence(
								Actions.moveTo(x, Core.HEIGHT + size, 0),
								Actions.moveTo(x - Core.randomPercent(0.7f, 100), -size, speed, Interpolation.linear)
							)
					)));
			
			flakeGroup.addActor(snowFlake);
			
			tmp.addActor(flakeGroup);
		}
		
		return tmp;
	}	
	
	
	
	
	Group createClouds() {
		Group tmp = new Group();
		
		for( int i = 0; i < MAX_CLOUDS; i++ ) {
			Image cloud = new Image(GFX.get(IMG.CLOUD));
			
			final float size = (Core.HEIGHT * 0.2f) + (Core.HEIGHT * 0.6f) * Core.rnd.nextFloat();
			final float x = Core.WIDTH * Core.rnd.nextFloat();
			final float y = Core.HEIGHT_HALF + Core.HEIGHT_HALF * 0.7f * Core.rnd.nextFloat();
			
			cloud.setPosition(x, y);
			cloud.setSize(size, size/2);
			
			final float ds = (float)x / (float)(Core.WIDTH+size);
			
			final float speed = Core.randomPercent(0.7f, CLOUDS_SPEED);
			
			cloud.addAction(
					Actions.sequence(
						Actions.moveTo(-size, y, speed * ds, Interpolation.linear),
						Actions.forever(Actions.sequence(
								Actions.moveTo(Core.WIDTH, y, 0),
								Actions.moveTo(-size, y, speed, Interpolation.linear)
							)
					)));
			
			tmp.addActor(cloud);
		}
		
		return tmp;
	}	
	
	
	
	
	Group createMountains( final Color color ) {
		Group tmp = new Group();
		
		final float dx = (float)Core.WIDTH / (float)MAX_GROUND;
		
		for( int i = 0; i < MAX_GROUND; i++ ) {
			Image ground = new Image(GFX.get(IMG.MOUNTAIN));
			
			final float size = (Core.HEIGHT * 0.2f) + (Core.HEIGHT * 0.6f) * Core.rnd.nextFloat();
			final float x = -size / 4 + i * dx;
			
			ground.setPosition(x, 0);
			ground.setSize(size, size);
			
			ground.setColor(color);
			
			tmp.addActor(ground);
		}
		
		return tmp;
	}
	
	
	Group createSubGround( final Color color ) {
		Group tmp = new Group();
		
		Image downGround = new Image(GFX.get(IMG.BOX));
		downGround.setSize(Core.WIDTH, Core.HEIGHT_HALF / 7);
		
		downGround.setColor(color);
		
		tmp.addActor(downGround);
		
		return tmp;
	}
	
	
	Group createTrees( final int nTree, final Color color ) {
		Group tmp = new Group();
		
		final float dx = (float)Core.WIDTH / (float)MAX_GROUND;
		
		for( int i = 0; i < MAX_TREES; i++ ) {
			Image tree = new Image(GFX.get(IMG.values()[IMG.TREE.ordinal() + nTree]));
			
			tree.setColor(color);
			
			final float size = (Core.HEIGHT * 0.05f) + (Core.HEIGHT * 0.25f) * Core.rnd.nextFloat();
			final float x = -size / 4 + i * dx;
			
			tree.setPosition(x, Core.HEIGHT_HALF / 7);
			tree.setSize(size, size);
			
			tmp.addActor(tree);
		}
		
		return tmp;
	}
	
	
	
	Group createTrees2( final Color color ) {
		Group tmp = new Group();
		
		final float dx = (float)Core.WIDTH / (float)MAX_TREES;
		
		for( int i = 0; i < MAX_TREES; i++ ) {
			Image tree = new Image(GFX.get(IMG.TREE2));
			
			tree.setColor(color);
			
			final float size = (Core.HEIGHT * 0.2f) + (Core.HEIGHT * 0.6f) * Core.rnd.nextFloat();
			final float x = -size / 4 + i * dx;
			
			tree.setPosition(x, 0);
			tree.setSize(size, size);
			
			tmp.addActor(tree);
		}
		
		return tmp;
	}

	
	
	Group createTrees3( final int color ) {
		Group tmp = new Group();
		
		final float dx = (float)Core.WIDTH / (float)MAX_TREES;
		
		for( int i = 0; i < MAX_TREES; i++ ) {
			Image tree = new Image(GFX.get(IMG.TREE3));
			
			tree.setColor(GameColor.getByNumber(color));
			
			final float size = (Core.HEIGHT * 0.2f) + (Core.HEIGHT * 0.6f) * Core.rnd.nextFloat();
			final float x = -size / 4 + i * dx;
			
			tree.setPosition(x, 0);
			tree.setSize(size, size);
			
			tmp.addActor(tree);
		}
		
		return tmp;
	}


}
