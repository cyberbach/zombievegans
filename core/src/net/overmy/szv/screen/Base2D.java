package net.overmy.szv.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import net.overmy.szv.Core;
import net.overmy.szv.MyGdxGame;
import net.overmy.szv.neatresources.ParticleFX;

public class Base2D implements Screen, InputProcessor {

	protected MyGdxGame				baseGame;
	
	protected Stage					layerTop = null; // сверху
	protected Stage					layerBottom = null; // снизу

	protected Color					bgColor;
	
	FPSLogger fpsLogger;
	
	
	public Base2D( final MyGdxGame game ) {
		Gdx.app.log("Base2D", "constructor");
		
		this.baseGame = game;
		
		bgColor = new Color();
		bgColor.set( Color.BLACK );

		layerBottom = new Stage();
		layerTop = new Stage();
		
        Gdx.input.setCatchBackKey( true );
		Gdx.input.setCatchMenuKey( true );
		
		Gdx.input.setInputProcessor( this );
		
		fpsLogger = new FPSLogger();
	}


	
	@Override
	public void show() {
	}

	public void act( float actDelta ) {
		
		fpsLogger.log();
		
	}

	
	@Override
	public void render(float delta) {
		
		act(delta);
		
		if(layerBottom != null) layerBottom.act();
		if(layerTop != null) layerTop.act();
		
		if(ParticleFX.effect != null) ParticleFX.effect.update(delta);

		Gdx.gl.glViewport( 0, 0, Core.WIDTH, Core.HEIGHT );
		Gdx.gl.glClearColor( bgColor.r, bgColor.g, bgColor.b, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		
		if(layerBottom != null) layerBottom.draw();
		if(layerTop != null) {
			layerTop.draw();
			layerTop.getBatch().begin();
			if(ParticleFX.effect != null) ParticleFX.effect.draw(layerTop.getBatch());
			layerTop.getBatch().end();
		}
	}

	@Override
	public void resize(int width, int height) {
		Core.resize(width, height);
		
		// Adaptive camera
		if(layerBottom != null) layerBottom.getViewport().update(width, height);
		if(layerTop != null) layerTop.getViewport().update(width, height);
	}
	
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		Gdx.app.log("Base2D", "dispose\n");
		
		layerTop.clear();
		layerBottom.clear();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(layerBottom != null) layerBottom.keyDown(keycode);
		if(layerTop != null) layerTop.keyDown(keycode);
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(layerBottom != null) layerBottom.keyUp(keycode);
		if(layerTop != null) layerTop.keyUp(keycode);
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if(layerBottom != null) layerBottom.keyTyped(character);
		if(layerTop != null) layerTop.keyTyped(character);
		
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(layerBottom != null) layerBottom.touchDown(screenX, screenY, pointer, button);
		if(layerTop != null) layerTop.touchDown(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(layerBottom != null) layerBottom.touchDown(screenX, screenY, pointer, button);
		if(layerTop != null) layerTop.touchUp(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(layerBottom != null) layerBottom.touchDragged(screenX, screenY, pointer);
		if(layerTop != null) layerTop.touchDragged(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
