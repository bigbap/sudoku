package com.averagecoder.sudoku;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Sudoku extends Game {
	SpriteBatch batch;
	TextureAtlas atlasNumbers;
    TextureAtlas atlasNumbersActive;
    OrthographicCamera camera;
    FPSLogger fpsLogger;
    AssetManager manager = new AssetManager();
    static TextureAtlas atlasUI;
    static Viewport viewport;

    static final int WIDTH = 640;
    static final int HEIGHT = 896;

    public Sudoku(){
        fpsLogger = new FPSLogger();
        camera = new OrthographicCamera();
        camera.position.set(WIDTH/2,HEIGHT/2,0);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
    }
	
	@Override
	public void create () {
        manager.load("numbers.pack", TextureAtlas.class);
        manager.load("numbersActive.pack", TextureAtlas.class);
        manager.load("ui.pack", TextureAtlas.class);
        manager.finishLoading();

        batch = new SpriteBatch();
        atlasNumbers = manager.get("numbers.pack", TextureAtlas.class);
        atlasNumbersActive = manager.get("numbersActive.pack", TextureAtlas.class);
        atlasUI = manager.get("ui.pack", TextureAtlas.class);

		setScreen(new SudokuMain(this));
	}

	@Override
	public void render () {
        fpsLogger.log();

        camera.update();

		super.render();
	}

    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
    }

    @Override
    public void dispose(){
        batch.dispose();
        atlasNumbers.dispose();
        atlasNumbersActive.dispose();
    }
}
