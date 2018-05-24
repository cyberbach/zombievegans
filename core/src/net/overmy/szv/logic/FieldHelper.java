package net.overmy.szv.logic;

import java.util.ArrayList;

import com.badlogic.gdx.math.GridPoint2;

public class FieldHelper {
	
	public ArrayList<GridPoints> variants;
	
	
	
	public FieldHelper() {
		
		variants = new ArrayList<GridPoints>();
		
	}
	
	
	
	boolean isVariant( final GridPoint2 gp1, final GridPoint2 gp2 ) {
		
		for( int i = 0; i < variants.size(); i++ ) {
			
			if( gp1.equals(variants.get(i).from) && gp2.equals(variants.get(i).to) ) return true;
			if( gp2.equals(variants.get(i).from) && gp1.equals(variants.get(i).to) ) return true;
			
		}
		
		return false;
	}
	
	
	
	boolean isPresent( final Cell[][] cell ) {
		
		final int ARRAY_SIZE = cell.length;

		for( int y = 0; y < ARRAY_SIZE; y++ )
		for( int x = 0; x < ARRAY_SIZE; x++ ) {
			// test row
			if( (x+1 < ARRAY_SIZE) && (x+2 < ARRAY_SIZE) )
			if( cell[x][y].onField == true )
			if( compare3(cell[x][y].type, cell[x+1][y].type, cell[x+2][y].type) )
				return true;

			// test column
			if( (y+1 < ARRAY_SIZE) && (y+2 < ARRAY_SIZE) )
			if( cell[x][y].onField == true )
			if( compare3(cell[x][y].type, cell[x][y+1].type, cell[x][y+2].type) )
				return true;
		}
		
		return false; // =(
		
	}
	
	
	
	void searchVariants( final Cell[][] cell ) {
		
		final int ARRAY_SIZE = cell.length;

		variants.clear();
		
		for( int y = 0; y < ARRAY_SIZE - 1; y++ )
		for( int x = 0; x < ARRAY_SIZE - 2; x++ ){
			/* 1 1 0
			 * 0 0 1 */
			if(compare3(cell[x][y].type, cell[x+1][y].type, cell[x+2][y+1].type)) variants.add(new GridPoints(x+2, y+1, x+2, y));
			/* 1 0 1
			 * 0 1 0 */
			if(compare3(cell[x][y].type, cell[x+1][y+1].type, cell[x+2][y].type)) variants.add(new GridPoints(x+1, y+1, x+1, y));
			/* 0 1 1
			 * 1 0 0 */
			if(compare3(cell[x][y+1].type, cell[x+1][y].type, cell[x+2][y].type)) variants.add(new GridPoints(x, y+1, x, y));
			/* 0 0 1
			 * 1 1 0 */
			if(compare3(cell[x][y+1].type, cell[x+1][y+1].type, cell[x+2][y].type)) variants.add(new GridPoints(x+2, y, x+2, y+1));
			/* 0 1 0
			 * 1 0 1 */
			if(compare3(cell[x][y+1].type, cell[x+1][y].type, cell[x+2][y+1].type)) variants.add(new GridPoints(x+1, y, x+1, y+1));
			/* 1 0 0
			 * 0 1 1 */
			if(compare3(cell[x][y].type, cell[x+1][y+1].type, cell[x+2][y+1].type)) variants.add(new GridPoints(x, y, x, y+1));
		}
		
		for( int y = 0; y < ARRAY_SIZE - 2; y++ )
		for( int x = 0; x < ARRAY_SIZE - 1; x++ ){
			/* 1 0
			 * 1 0
			 * 0 1 */
			if(compare3(cell[x][y].type, cell[x][y+1].type, cell[x+1][y+2].type)) variants.add(new GridPoints(x+1, y+2, x, y+2));
			/* 1 0
			 * 0 1
			 * 1 0 */
			if(compare3(cell[x][y].type, cell[x+1][y+1].type, cell[x][y+2].type)) variants.add(new GridPoints(x+1, y+1, x, y+1));
			/* 0 1
			 * 1 0
			 * 1 0 */
			if(compare3(cell[x+1][y].type, cell[x][y+1].type, cell[x][y+2].type)) variants.add(new GridPoints(x+1, y, x, y));
			/* 1 0
			 * 0 1
			 * 0 1 */
			if(compare3(cell[x][y].type, cell[x+1][y+1].type, cell[x+1][y+2].type)) variants.add(new GridPoints(x, y, x+1, y));
			/* 0 1
			 * 1 0
			 * 0 1 */
			if(compare3(cell[x+1][y].type, cell[x][y+1].type, cell[x+1][y+2].type)) variants.add(new GridPoints(x, y+1, x+1, y+1));
			/* 0 1
			 * 0 1
			 * 1 0 */
			if(compare3(cell[x+1][y].type, cell[x+1][y+1].type, cell[x][y+2].type)) variants.add(new GridPoints(x, y+2, x+1, y+2));
		}

		for( int y = 0; y < ARRAY_SIZE; y++ )
		for( int x = 0; x < ARRAY_SIZE - 3; x++ ){
			/* 1 0 1 1 */
			if(compare3(cell[x][y].type, cell[x+2][y].type, cell[x+3][y].type)) variants.add(new GridPoints(x, y, x+1, y));
			/* 1 1 0 1 */
			if(compare3(cell[x][y].type, cell[x+1][y].type, cell[x+3][y].type)) variants.add(new GridPoints(x+3, y, x+2, y));
		}

		for( int y = 0; y < ARRAY_SIZE - 3; y++ )
		for( int x = 0; x < ARRAY_SIZE; x++ ){
			/* 1
			 * 0
			 * 1
			 * 1 */
			if(compare3(cell[x][y].type, cell[x][y+2].type, cell[x][y+3].type)) variants.add(new GridPoints(x, y, x, y+1));
			/* 1
			 * 1
			 * 0
			 * 1 */
			if(compare3(cell[x][y].type, cell[x][y+1].type, cell[x][y+3].type)) variants.add(new GridPoints(x, y+3, x, y+2));
		}
	}
	
	
	
	boolean compare3( final CELL_TYPE a, final CELL_TYPE b, final CELL_TYPE c ) {
		
		return (a == b) && (b == c);
		
	}
	
}
