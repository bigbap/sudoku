package com.averagecoder.sudoku;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Sudoku extends Game {
	SpriteBatch batch;
	TextureAtlas atlas;
    OrthographicCamera camera;
    Viewport viewport;
    FPSLogger fpsLogger;
    ShapeRenderer shapeRenderer;

    static final int WIDTH = 640;
    static final int HEIGHT = 896;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        atlas = new TextureAtlas(Gdx.files.internal("numbers.pack"));
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        viewport.apply(true);

        fpsLogger = new FPSLogger();

        shapeRenderer = new ShapeRenderer();

		setScreen(new SudokuMain(this));
	}

	@Override
	public void render () {
        fpsLogger.log();

		super.render();
	}

    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
    }

    @Override
    public void dispose(){
        batch.dispose();
        atlas.dispose();
        shapeRenderer.dispose();
    }
}
