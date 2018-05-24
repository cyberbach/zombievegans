package net.overmy.szv.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.overmy.szv.MyGdxGame;

public class DesktopLauncher {
	
	static int graphics = 3;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		switch(graphics){
		
			case 1: 
				config.width = 1024; // galaxy tab 2 (1024 554) not 600
				config.height = 554;
				break;
				
			case 2: 
				config.width = 1024; // ебучий квадратный экран
				config.height = 768;
				break;
				
			case 3: 
				config.width = 1920/2; // FullHD
				config.height = 1080/2;
				break;
				
			default:
				config.width = 800; // это норма
				config.height = 480;
				break;
				
		}
		
		config.title = "" + config.width + " / " + config.height;
		
		new LwjglApplication(new MyGdxGame(), config);
	}
}
