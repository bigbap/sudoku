package com.averagecoder.sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by User on 2015/08/31.
 */
public class SudokuMain extends ScreenAdapter {

    private Sudoku game;
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private OrthographicCamera camera;
    private Board board;
    ShapeRenderer shapeRenderer;
    private Array<Nums> numbers = new Array<Nums>();
    private int activeNum = 0;

    public SudokuMain(Sudoku sudoku){
        game = sudoku;
        batch = game.batch;
        atlas = game.atlas;
        camera = game.camera;
        shapeRenderer = game.shapeRenderer;

        board = new Board(atlas, camera, shapeRenderer);
        board.resetBoard(5);

        Vector2 pos = new Vector2((game.WIDTH / 2) - ((Board.TILE_SIZE * 5) / 2), Board.TILE_SIZE * 2);
        for (int r = 0; r < 2; r++){
            for (int c = 0; c < 5; c++) {
                if(r == 1 && c == 4)
                    break;

                int num = (r * 5) + c + 1;
                numbers.add(new Nums(pos, Board.TILE_SIZE, num));

                pos.x += Board.TILE_SIZE;
            }
            pos.y -= Board.TILE_SIZE;
            pos.x = (game.WIDTH / 2) - ((Board.TILE_SIZE * 5) / 2);
        }
    }

    @Override
    public void render(float delta){
        update(delta);
        draw();
    }

    public void update(float delta){
        board.update(delta);

        if(Gdx.input.justTouched()){
            Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(pos);

            for(Nums n: numbers){
                if(n.pos.x < pos.x && n.pos.x + n.size > pos.x && n.pos.y < pos.y && n.pos.y + n.size > pos.y){
                    activeNum = n.num;
                    n.active = true;
                }
            }
            for(Nums n: numbers){
                if(n.num != activeNum){
                    n.active = false;
                }
            }
        }

        if(board.activeTile != null){
            board.activeTile.setNum(activeNum);
        }
    }

    public void draw(){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setAutoShapeType(true);
        for(int i = 0; i < numbers.size; i++){
            numbers.get(i).drawSquares();
        }
        shapeRenderer.end();
        batch.begin();
        for(int i = 0; i < numbers.size; i++){
            numbers.get(i).drawNums();
        }
        batch.end();

        board.draw(batch);
    }

    private class Nums{
        private Vector2 pos = new Vector2();
        private int size;
        private int num;
        private boolean active = false;

        public Nums(Vector2 p, int s, int n){
            this.pos.x = p.x;
            this.pos.y = p.y;
            this.size = s;
            this.num = n;
        }

        public void drawSquares(){
            if(active) {
                shapeRenderer.setColor(0.9f, 0.9f, 0.5f, 1);
                shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

                shapeRenderer.rect((int) pos.x, (int) pos.y, size, size);

                shapeRenderer.set(ShapeRenderer.ShapeType.Line);
            }

            shapeRenderer.setColor(0, 0, 0, 1);
            shapeRenderer.rect((int) pos.x, (int) pos.y, size, size);
        }

        public void drawNums(){
            TextureRegion thisNum = Board.numbers.get(num - 1);
            batch.draw(thisNum, (int)pos.x + (size / 2) - (thisNum.getRegionWidth() / 2), (int)pos.y + (size / 2) - (thisNum.getRegionHeight() / 2));
        }
    }
}
