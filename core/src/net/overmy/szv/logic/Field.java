package net.overmy.szv.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.overmy.szv.Core;
import net.overmy.szv.neatresources.GFX;
import net.overmy.szv.neatresources.SoundFX;
import net.overmy.szv.neatresources.GFX.IMG;
import net.overmy.szv.neatresources.GameColor;

public class Field {

	public boolean gameOverFLAG = false;
	
	// MUST BE OVERRIDED
	boolean swapTurn() { return false; }
	void afterShowHelp() {}
	void animateDeletedCell(int x, int y, int ordinal) { }
	void addTurn() {}
	void patVarning() {}

	// sizes
	public final int HEIGHT_BY_CELL = Core.HEIGHT / Core.MAX_FIELD_CELLS;
	
	final float DELTA_WIDTH = (Core.WIDTH - Core.HEIGHT) / 2;
	
	// animation speed
	final float ACTIVE_BLINK_SPEED = 0.3f;
	final float FALLDOWN_SPEED = 0.7f;
	final float EMERSION_SPEED = 0.5f;
	final float HIDE_SPEED = 0.3f;
	final float SHIFT_SPEED = 0.4f;
	final float HELP_DELAY = 4.5f;
	
	final float ENEMY_THINK_1TURN = 1.8f;
	final float ENEMY_THINK_2TURN = 1.2f;
	
	int enemyMove = 0; // это случайный ход из возможных вариантов перемещени€


	Cell[][] cell = null; // массив рабочих клеток
	
	FieldHelper helper; // поиск возможных вариантов дл€ хода и дл€ удалени€ одинаковых клеток
	
	// action groups
	Group tempGroup = new Group();
	Group animationGroup = new Group();
	Group helpGroup = new Group();

	// for drop cells
	ArrayList<GridPoints> swapGroupsCounter = new ArrayList<GridPoints>();

	boolean clickBlocking_FLAG = false; // если TRUE, то отключение обработки клика на ¬—≈’ клеточках
	
	final GridPoint2 INACTIVE_CELL = new GridPoint2(-2, -2);
	
	GridPoint2 clicked1 = new GridPoint2(INACTIVE_CELL);
	GridPoint2 clicked2 = new GridPoint2(INACTIVE_CELL);

	
	
	public Field() {
		cell = new Cell[Core.MAX_FIELD_CELLS][Core.MAX_FIELD_CELLS];
		helper = new FieldHelper();
		
		initField();
	}
	
	void startAnimation(Action... actions) { 
		animationGroup.clear();
		animationGroup.addAction(Actions.sequence(actions));
	}
	

	void initField() {
		for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
		for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
			cell[x][y] = new Cell();
			cell[x][y].face = new Group();
			cell[x][y].face.setPosition(-1000, -1000);
			
			cell[x][y].bg = new Group();
			cell[x][y].bg.addActor(createCellBG());
			cell[x][y].bg.addAction(Actions.alpha(0, 0));
			final float dx = x * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2 + DELTA_WIDTH;
			final float dy = y * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2; 
			cell[x][y].bg.setPosition(dx, dy);
		}
	}
	
	public void firstStart() { // Start from LEVEL after show INTRO or STORY screens
		startAnimation(
				Actions.run(setAllCellsToFalse),
				Actions.run(genNewField),
				Actions.run(setAllCellsToTrue),
				Actions.run(updateGroups),
				Actions.run(levelEmersion),
				Actions.delay(EMERSION_SPEED),
				Actions.run(processCells)
			);
	}
	
	
	private Image createCellImage( final int n ) {
		
		Image img = new Image( GFX.get(IMG.values()[IMG.ICON1.ordinal() + n]) );
		img.setSize( HEIGHT_BY_CELL * 0.9f, HEIGHT_BY_CELL * 0.9f );
		img.setPosition( -HEIGHT_BY_CELL * 0.9f/2, -HEIGHT_BY_CELL * 0.9f/2 );
		
		return img;
		
	}

	
	
	private Image createCellBG() {
		
		Image img = new Image( GFX.get(IMG.BOX) );
		img.setSize( HEIGHT_BY_CELL * 0.96f, HEIGHT_BY_CELL * 0.96f );
		img.setColor( GameColor.SAND.get().r, GameColor.SAND.get().g, GameColor.SAND.get().b, 0.85f ); // 0.65 - не видно на светлом фоне
		img.setPosition( -HEIGHT_BY_CELL * 0.96f/2, -HEIGHT_BY_CELL * 0.96f/2 );
		
		return img;
		
	}
	
	
	public Group getFieldGroup() {
		tempGroup.clear();
		
		for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
		for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ )
			tempGroup.addActor( cell[x][y].bg );
		
		for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
		for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ )
			tempGroup.addActor( cell[x][y].face );
		
		tempGroup.addActor(animationGroup);
		tempGroup.addActor(helpGroup);
		
		return tempGroup;
		
	}
	
	
	
	public void clear(){
		for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
		for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
			cell[x][y].face.clear();
			cell[x][y].face = null;
		}
		cell = null;
		
		tempGroup = null;
		animationGroup = null;
	}

	
	boolean compare3( final CELL_TYPE a, final CELL_TYPE b, final CELL_TYPE c ) {
		
		return (a == b) && (b == c);
		
	}
	
	enum FLING_DIRECTION {
		UP,
		DOWN,
		LEFT,
		RIGHT
	};

	void createFace( Group newFace, final int x, final int y, final int faceNumber ) {
		newFace.clear();
		newFace.addActor( createCellImage(faceNumber) );
		newFace.addListener( new ClickListener() {
			@Override
			public void clicked (InputEvent dontCareEvent, float dontCareX, float dontCareY) {
				clickCell(x, y);
			}
		});
		
		newFace.addListener(new ActorGestureListener() {
			public void fling (InputEvent event, float velocityX, float velocityY, int button) {
				
				FLING_DIRECTION dir = null;

				if( Math.abs(velocityX) > Math.abs(velocityY) )
					if(velocityX > 0) dir = FLING_DIRECTION.RIGHT; else dir = FLING_DIRECTION.LEFT;
				else
					if(velocityY > 0) dir = FLING_DIRECTION.UP; else dir = FLING_DIRECTION.DOWN;
				
				flingCell( x, y, dir );
				
				//Gdx.app.log("Example", "fling (" + velocityX + ", " + velocityY + ") " + dir.toString());
			}
		});
	}

	
	
	void flingCell( final int x, final int y, final FLING_DIRECTION direction ) {
		
		switch( direction ){
			case DOWN:
				if( y-1 < 0 ) return;
				clickCell(x, y); clickCell(x, y-1);
				break;
			case UP:
				if( y >= Core.MAX_FIELD_CELLS-1 ) return;
				clickCell(x, y); clickCell(x, y+1);
				break;
			case LEFT:
				if( x-1 < 0 ) return;
				clickCell(x, y); clickCell(x-1, y);
				break;
			case RIGHT:
				if( x >= Core.MAX_FIELD_CELLS-1 ) return;
				clickCell(x, y); clickCell(x+1, y);
				break;
			default:
				break;
		}
		
	}




	CELL_TYPE genNewCellType() {
		// 012 34 5
		/*int r = Core.rnd.nextInt(Core.MAX_CELL_VALUES);
		
		if( r < 3) {
			return CELL_TYPE.values()[Core.cellsPower[0 + Core.rnd.nextInt(2)]];
		} else if( r < 5) {
			return CELL_TYPE.values()[Core.cellsPower[2 + Core.rnd.nextInt(2)]];
		} else
		return CELL_TYPE.values()[Core.cellsPower[4 + Core.rnd.nextInt(2)]];*/
		
		// генерить от того, как раздали клетки персонажам. больше слабых фруктов, меньше сильных
		return CELL_TYPE.values()[Core.rnd.nextInt(Core.MAX_CELL_VALUES)];
	}
	
	void clickCell( final int x, final int y ) {
		
		if( clickBlocking_FLAG ) return;
		
		clicked2.set( x, y );
		
		if( !clicked1.equals(INACTIVE_CELL) ){
			Group g1 = cell[clicked1.x][clicked1.y].face;
			g1.clearActions();
			g1.setScale(1, 1);
		}

		Group g2 = cell[clicked2.x][clicked2.y].face;
		g2.clearActions();
		g2.setScale(1, 1);

		
		if( !clicked1.equals(clicked2) )
		if( ((clicked1.x - 1 == clicked2.x) && (clicked1.y == clicked2.y)) || // clicked1 near the clicked2 ?
			((clicked1.x + 1 == clicked2.x) && (clicked1.y == clicked2.y)) ||
			((clicked1.y - 1 == clicked2.y) && (clicked1.x == clicked2.x)) ||
			((clicked1.y + 1 == clicked2.y) && (clicked1.x == clicked2.x)) ) {
			
			helper.searchVariants( cell );
			
			if( helper.isVariant(clicked2, clicked1) ){
				SoundFX.play(1);
				helpGroup.clear();
				animationGroup.clear();
				animationGroup.addAction(Actions.sequence(
						Actions.run(blockClick),
						Actions.run(swap),
						Actions.delay(SHIFT_SPEED),
						Actions.run(swap2groups),
						Actions.run(clearClicked1),
						Actions.run(processCells)
					));
				
			} else {
				SoundFX.play(2);
				animationGroup.clear();
				animationGroup.addAction(Actions.sequence(
						Actions.run(blockClick),
						Actions.run(fakeSwap),
						Actions.run(clearClicked1),
						Actions.delay(SHIFT_SPEED*2),
						Actions.run(unblockClick)
					));
				
			}
			
		} else {
			clicked1.set( clicked2 );
			cell[clicked1.x][clicked1.y].face.addAction( Actions.forever(Actions.sequence(
					Actions.scaleTo(0.9f, 0.9f, ACTIVE_BLINK_SPEED, Interpolation.fade),
					Actions.scaleTo(1, 1, ACTIVE_BLINK_SPEED, Interpolation.fade)
				)) );
			SoundFX.play(0);
		}
		
		//Gdx.app.log("clicked1 " + clicked1.toString(), "clicked2 " + clicked2.toString());

	}

	
	
	
	
	// FINALS // FINALS // FINALS // FINALS // FINALS // FINALS // FINALS // FINALS // FINALS //
	
	
	
	final Runnable showHelp = new Runnable() {
		@Override
		public void run() {
			
			final int allVariants = helper.variants.size();
			
			if(allVariants == 0) return;
			
			final int n = Core.rnd.nextInt(allVariants);
			
			cell[helper.variants.get(n).from.x][helper.variants.get(n).from.y].bg.addAction(
				Actions.sequence(
					Actions.scaleTo(0, 0, SHIFT_SPEED ),
					Actions.scaleTo(1, 1, SHIFT_SPEED )
				));
			cell[helper.variants.get(n).to.x][helper.variants.get(n).to.y].bg.addAction(
				Actions.sequence(
					Actions.delay( ACTIVE_BLINK_SPEED/2 ),
					Actions.scaleTo(0, 0, SHIFT_SPEED ),
					Actions.scaleTo(1, 1, SHIFT_SPEED )
				));
			
			afterShowHelp();
			
			SoundFX.play(14 + Core.rnd.nextInt(2));
		}

		
	};
	
	
	
	final Runnable removeSameCells = new Runnable() {
		@Override
		public void run() {
			// vertical
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
				
				CELL_TYPE currentCellType = cell[x][0].type;
				
				for( int y = 0; y < Core.MAX_FIELD_CELLS-2; y++ ) {
					
					if( currentCellType != cell[x][y].type ) currentCellType = cell[x][y].type;
					
					if( compare3( cell[x][y].type, cell[x][y+1].type, cell[x][y+2].type ) ){
						cell[x][y].onField = false;
						cell[x][y+1].onField = false;
						cell[x][y+2].onField = false;
						
					}
				}
			}
			
			// horizontal
			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ ) {
				
				CELL_TYPE currentCellType = cell[0][y].type;
				
				for( int x = 0; x < Core.MAX_FIELD_CELLS-2; x++ ) {
					
					if( currentCellType != cell[x][y].type ) currentCellType = cell[x][y].type;
					
					if( compare3( cell[x][y].type, cell[x+1][y].type, cell[x+2][y].type ) ){
						cell[x][y].onField = false;
						cell[x+1][y].onField = false;
						cell[x+2][y].onField = false;
					}
				}
			}
			
			int[] sameScore = new int[Core.MAX_CELL_VALUES];
			for( int i = 0; i < Core.MAX_CELL_VALUES; i++ ) sameScore[i] = 0;
			
			// hide animation
			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ )
				if( cell[x][y].onField == false ) {
					sameScore[cell[x][y].type.ordinal()]++; // считаем €чейки одинаковых типов
					animateDeletedCell(x, y, cell[x][y].type.ordinal());
					// show extra-turn message ???
					cell[x][y].face.addAction( Actions.sequence( 
							Actions.scaleTo(0, 0, Core.randomPercent(0.7f, HIDE_SPEED))
						));
				}
			
			for( int i = 0; i < Core.MAX_CELL_VALUES; i++ ){
				if(sameScore[i] > 4) addTurn();
			}
		}

	};

	

	final Runnable blockClick = new Runnable() { @Override public void run() { clickBlocking_FLAG = true; } };
	final Runnable unblockClick = new Runnable() { @Override public void run() { clickBlocking_FLAG = false; } };
	
	
	
	final Runnable dropNewCells = new Runnable() {
		@Override
		public void run() {
			
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ )
			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ ){
				
				if( cell[x][y].onField == false ) {
					final float EXTRA_HEIGHT = HEIGHT_BY_CELL/2;
					final float fromX = x * HEIGHT_BY_CELL + EXTRA_HEIGHT + DELTA_WIDTH;
					final float fromY = (Core.MAX_FIELD_CELLS + y) * HEIGHT_BY_CELL + HEIGHT_BY_CELL + HEIGHT_BY_CELL/2;
					final float toX = fromX;
					final float toY = y * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2;
					
					cell[x][y].face.setPosition(-1000, -1000);
					cell[x][y].type = genNewCellType();
					createFace(cell[x][y].face, x, y, cell[x][y].type.ordinal());
					cell[x][y].face.setPosition(fromX, fromY);
					
					cell[x][y].face.setScale(1, 1);
					cell[x][y].face.addAction( Actions.moveTo(toX, toY, Core.randomPercent(0.7f, FALLDOWN_SPEED), Interpolation.fade));
					cell[x][y].onField = true;
					
					
				}
			}
			
		}
	};	
	
	
	
	final Runnable processCells = new Runnable() {
		@Override
		public void run() {
			if(gameOverFLAG) return;
			if( helper.isPresent(cell) ){
				SoundFX.play(3);
				startAnimation(
						Actions.run(blockClick),
						Actions.run(removeSameCells),
						Actions.delay(HIDE_SPEED),
						Actions.run(dropCells),
						Actions.run(dropNewCells),
						Actions.delay(FALLDOWN_SPEED),
						Actions.run(updateGroups),
						Actions.run(unblockClick),
						Actions.run(processCells)
					);
			} else {
				startAnimation( Actions.run(testForPATs) );
			}
			
		}
	};
	
	
	
	final Runnable dropCells = new Runnable() {
		@Override
		public void run() {
			if(gameOverFLAG) return;

			swapGroupsCounter.clear();
			
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
				int emptyCount = 0;
				int emptyStack = 0;
				
				for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ ){
					
					boolean readyToShift = false;
					if( cell[x][y].onField == false ) {
						if( readyToShift ) {
							emptyStack += emptyCount;
							emptyCount = 0;
						}
						emptyCount++;
						readyToShift = false;
					} else
						if(emptyCount > 0) readyToShift = true;
					
					if( readyToShift) {
						final float speed = FALLDOWN_SPEED * 0.8f + Core.rnd.nextFloat() * FALLDOWN_SPEED * 0.2f;
						cell[x][y].face.addAction( Actions.moveTo(cell[x][y].face.getX(), (y - emptyCount - emptyStack) * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2, speed, Interpolation.fade) );
						cell[x][y - emptyCount - emptyStack].type = cell[x][y].type;
						cell[x][y - emptyCount - emptyStack].onField = true;
						cell[x][y].onField = false;
						swapGroupsCounter.add( new GridPoints(x, y, x, y - emptyCount - emptyStack) );
					}
					
				}
			}
			
			// swap groups
			for( int i = 0; i < swapGroupsCounter.size(); i++ ) {
				Group temp = cell[swapGroupsCounter.get(i).from.x][swapGroupsCounter.get(i).from.y].face;
				cell[swapGroupsCounter.get(i).from.x][swapGroupsCounter.get(i).from.y].face = cell[swapGroupsCounter.get(i).to.x][swapGroupsCounter.get(i).to.y].face;
				cell[swapGroupsCounter.get(i).to.x][swapGroupsCounter.get(i).to.y].face = temp; 
			}
			
		}
	};

	
	
	final Runnable enemyTurn1 = new Runnable() {
		@Override
		public void run() {
			clickBlocking_FLAG = false;
			clickCell(
					helper.variants.get(enemyMove).from.x, 
					helper.variants.get(enemyMove).from.y );
			clickBlocking_FLAG = true;
		}
	};
	
	
	
	final Runnable enemyTurn2 = new Runnable() {
		@Override
		public void run() {
			clickBlocking_FLAG = false;
			clickCell(
					helper.variants.get(enemyMove).to.x,
					helper.variants.get(enemyMove).to.y );
			clickBlocking_FLAG = true;
		}
	};	
	

	
	final Runnable clearClicked1 = new Runnable() {
		@Override
		public void run() {
			clicked1.set( INACTIVE_CELL );
		}
	};

	
	
	final Runnable fakeSwap = new Runnable() {
		@Override
		public void run() {
			Group g1 = cell[clicked1.x][clicked1.y].face;
			Group g2 = cell[clicked2.x][clicked2.y].face;

			g1.addAction( Actions.sequence(
					Actions.moveTo(g2.getX(), g2.getY(), Core.randomPercent(0.7f, SHIFT_SPEED), Interpolation.fade),
					Actions.moveTo(g1.getX(), g1.getY(), Core.randomPercent(0.7f, SHIFT_SPEED), Interpolation.fade)
				) );

			g2.addAction( Actions.sequence(
					Actions.moveTo(g1.getX(), g1.getY(), Core.randomPercent(0.7f, SHIFT_SPEED), Interpolation.fade),
					Actions.moveTo(g2.getX(), g2.getY(), Core.randomPercent(0.7f, SHIFT_SPEED), Interpolation.fade)
				) );
			
			clicked1.set( INACTIVE_CELL );
		}
	};
	
	

	final Runnable swap2groups = new Runnable() {
		@Override
		public void run() {
			Group temp = cell[clicked1.x][clicked1.y].face;
			cell[clicked1.x][clicked1.y].face = cell[clicked2.x][clicked2.y].face;
			cell[clicked2.x][clicked2.y].face = temp; 

			boolean temp2 = cell[clicked1.x][clicked1.y].onField;
			cell[clicked1.x][clicked1.y].onField = cell[clicked2.x][clicked2.y].onField;
			cell[clicked2.x][clicked2.y].onField = temp2;
		}
	};

	
	
	final Runnable swap = new Runnable() {
		@Override
		public void run() {
			Group g1 = cell[clicked1.x][clicked1.y].face;
			Group g2 = cell[clicked2.x][clicked2.y].face;

			g1.addAction( Actions.sequence(
					Actions.moveTo(g2.getX(), g2.getY(), Core.randomPercent(0.7f, SHIFT_SPEED), Interpolation.fade)
				) );

			g2.addAction( Actions.sequence(
					Actions.moveTo(g1.getX(), g1.getY(), Core.randomPercent(0.7f, SHIFT_SPEED), Interpolation.fade)
				) );
			
			CELL_TYPE ct = cell[clicked1.x][clicked1.y].type;
			cell[clicked1.x][clicked1.y].type = cell[clicked2.x][clicked2.y].type;
			cell[clicked2.x][clicked2.y].type = ct;
		}
	};
	
	
	
	final Runnable testForPATs = new Runnable() { // PAT in chess - is NO VARIANT situation
		@Override
		public void run() {
			if(gameOverFLAG) return;
			
			helper.searchVariants(cell);
			
			if( helper.variants.size() == 0 ){
				startAnimation(
						Actions.run(blockClick),
						Actions.run(patVarning),
						Actions.run(levelDestroy),
						Actions.delay(EMERSION_SPEED),
						Actions.run(setAllCellsToFalse),
						Actions.run(genNewField),
						Actions.run(setAllCellsToTrue),
						Actions.run(updateGroups),
						Actions.run(levelEmersion),
						Actions.delay(EMERSION_SPEED),
						Actions.run(unblockClick),
						Actions.run(testForPATs)
					);
				
			} else {
				if( swapTurn() ) {//TURN = TURN ? false : true;
					helpGroup.clear();
					helpGroup.addAction(Actions.forever(Actions.sequence(
							Actions.delay(Core.randomPercent(0.7f, HELP_DELAY)),
							Actions.run(showHelp)
						)));
		
					clickBlocking_FLAG = false;
				} else {
					helper.searchVariants(cell);
					enemyMove = Core.rnd.nextInt(helper.variants.size());
					clickBlocking_FLAG = true;
					helpGroup.clear();
					helpGroup.addAction(Actions.sequence(
							Actions.run(blockClick),
							Actions.delay(Core.randomPercent(0.1f, ENEMY_THINK_1TURN)),
							Actions.run(enemyTurn1),
							Actions.delay(Core.randomPercent(0.1f, ENEMY_THINK_2TURN)),
							Actions.run(enemyTurn2),
							Actions.run(unblockClick),
							Actions.run(testForPATs)
						));
				}
			}
			
		}
	};	

	
	
	final Runnable genNewField = new Runnable() {
		@Override
		public void run() {
			if(gameOverFLAG) return;
			Gdx.app.log("genNewField", "");
			while(true){
				for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
				for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
					cell[x][y].type = genNewCellType();
					cell[x][y].onField = true;
				}
				helper.searchVariants(cell);
				Gdx.app.log("NewField", "variants " + helper.variants.size());
				Gdx.app.log("NewField", "can delete now = " + helper.isPresent(cell));
				if( (helper.variants.size() != 0) && !helper.isPresent(cell) ) break;
				
				for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
				for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
					cell[x][y].onField = false;
				}
			}
			
		}
	};

	
	
	final Runnable genNewCells = new Runnable() {
		@Override
		public void run() {
			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
				cell[x][y].type = genNewCellType();
			}
		}
	};
	
	
	
	final Runnable genCells_IF_FALSE = new Runnable() {
		@Override
		public void run() {
			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ )
				if( cell[x][y].onField == false ) {					
					cell[x][y].type = genNewCellType();
				}
		}
	};
	
	
	
	final Runnable setAllCellsToFalse = new Runnable() {
		@Override
		public void run() {
			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ )
				cell[x][y].onField = false;
		}
	};
	
	
	
	final Runnable setAllCellsToTrue = new Runnable() {
		@Override
		public void run() {
			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ )
				cell[x][y].onField = true;
		}
	};

	
	
	final Runnable levelEmersion = new Runnable() {
		@Override
		public void run() {
			
			final float EXTRA_HEIGHT = HEIGHT_BY_CELL/2;
			
			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
				final float fromX = x * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2 + DELTA_WIDTH;
				final float fromY = (Core.MAX_FIELD_CELLS+y) * HEIGHT_BY_CELL + EXTRA_HEIGHT + HEIGHT_BY_CELL/2;
				final float toX = x * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2 + DELTA_WIDTH;
				final float toY = y * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2;
				cell[x][y].face.setPosition(fromX, fromY);
				cell[x][y].face.addAction(Actions.moveTo(toX, toY, Core.randomPercent(0.7f, EMERSION_SPEED), Interpolation.fade));
				cell[x][y].bg.addAction(Actions.alpha(1, Core.randomPercent(0.7f, EMERSION_SPEED)));
			}
			
		}
	};

	
	final Runnable patVarning = new Runnable() {
		@Override
		public void run() {

			patVarning();
			
		}
	};
	
	final Runnable levelDestroy = new Runnable() {
		@Override
		public void run() {
			
			final float EXTRA_HEIGHT = HEIGHT_BY_CELL/2;

			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
				final float fromX = x * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2 + DELTA_WIDTH;
				final float fromY = y * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2;
				final float toX = x * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2 + DELTA_WIDTH;
				final float toY = (-Core.MAX_FIELD_CELLS+y) * HEIGHT_BY_CELL - EXTRA_HEIGHT + HEIGHT_BY_CELL/2;
				cell[x][y].face.setPosition(fromX, fromY);
				cell[x][y].face.addAction(Actions.moveTo(toX, toY, Core.randomPercent(0.7f, EMERSION_SPEED), Interpolation.fade));
				cell[x][y].bg.addAction(Actions.alpha(0, Core.randomPercent(0.7f, EMERSION_SPEED)));
			}
			
		}
	};



	final Runnable updateGroupListeners = new Runnable() {
		@Override
		public void run() {
			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
				final int newX = x;
				final int newY = x;
				cell[x][y].face.clearListeners();
				cell[x][y].face.addListener( new ClickListener() {
					@Override
					public void clicked (InputEvent dontCareEvent, float dontCareX, float dontCareY) {
						clickCell(newX, newY);
					}
				});
			}
		}
	};
	
	
	
	final Runnable updateGroups = new Runnable() {
		@Override
		public void run() {
			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
				if(cell[x][y].onField == true) {
					createFace(cell[x][y].face, x, y, cell[x][y].type.ordinal());
				}
				
			}
		}
	};

	
	
	final Runnable updateGroupPositions = new Runnable() {
		@Override
		public void run() {
			for( int y = 0; y < Core.MAX_FIELD_CELLS; y++ )
			for( int x = 0; x < Core.MAX_FIELD_CELLS; x++ ) {
				if(cell[x][y].onField == true){
					cell[x][y].face.setPosition(x * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2 + DELTA_WIDTH, y * HEIGHT_BY_CELL + HEIGHT_BY_CELL/2);
					cell[x][y].face.setScale(1, 1);
				}
			}
		}
	};	
	
}
