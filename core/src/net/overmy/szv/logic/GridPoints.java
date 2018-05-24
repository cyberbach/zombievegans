package net.overmy.szv.logic;

import com.badlogic.gdx.math.GridPoint2;

public class GridPoints {
	
	protected GridPoint2 from;
	protected GridPoint2 to;
	
	public GridPoints( final int f1, final int f2, final int t1, final int t2 ){
		
		from = new GridPoint2(f1, f2);
		to = new GridPoint2(t1, t2);
		
	}
	
}
