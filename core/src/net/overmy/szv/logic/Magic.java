package net.overmy.szv.logic;

public class Magic {
	
	int damage = 0;
	CELL_TYPE type;
	int fill = 0;
	int MAX_FILL = 10;
	MagicBar bar;
	
	public Magic( int type ) {
		this.type = CELL_TYPE.values()[type];
		bar = new MagicBar(type);
	}

}
