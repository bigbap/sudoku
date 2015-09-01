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

public class SudokuMain extends ScreenAdapter {

    private Sudoku game;
    private SpriteBatch batch;
    private TextureAtlas atlasNumbers;
    TextureAtlas atlasNumbersActive;
    private OrthographicCamera camera;
    private Board board;
    static Array<Nums> numbers = new Array<Nums>();
    static int activeNum = 0;

    public SudokuMain(Sudoku sudoku){
        game = sudoku;
        batch = game.batch;
        atlasNumbers = game.atlasNumbers;
        atlasNumbersActive = game.atlasNumbersActive;
        camera = game.camera;

        board = new Board(atlasNumbers, atlasNumbersActive, camera);
        board.resetBoard(5);

        Vector2 pos = new Vector2((Sudoku.WIDTH / 2) - ((Board.TILE_SIZE * 6) / 2) + Board.TILE_SIZE, Board.TILE_SIZE * 2);
        for (int r = 0; r < 2; r++){
            for (int c = 0; c < 5; c++) {
                if(r == 1 && c == 4)
                    break;

                int num = (r * 5) + c + 1;
                numbers.add(new Nums(pos, Board.TILE_SIZE, num));

                pos.x += Board.TILE_SIZE;
            }
            pos.y -= Board.TILE_SIZE;
            pos.x = (Sudoku.WIDTH / 2) - ((Board.TILE_SIZE * 6) / 2) + Board.TILE_SIZE;
        }
    }

    @Override
    public void render(float delta){
        update(delta);
        draw();
    }

    public void update(float delta){
        board.update(delta);

        /*if(Gdx.input.justTouched()){
            Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(pos);

            for(Nums n: numbers){
                if(n.pos.x <= pos.x && n.pos.x + n.size >= pos.x && n.pos.y <= pos.y && n.pos.y + n.size >= pos.y){
                    activeNum = n.num;
                    n.active = true;
                }
            }
            for(Nums n: numbers){
                if(n.num != activeNum){
                    n.active = false;
                }
            }
        }*/

        if(board.activeTile != null && activeNum > 0 && !board.activeTile.locked){
            board.activeTile.setNum(activeNum);
            board.boardActiveNum = activeNum;
            activeNum = 0;
            board.activeTile = null;
        }
    }

    public void draw(){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for(int i = 0; i < numbers.size; i++){
            numbers.get(i).drawNums();
        }
        batch.draw(Sudoku.atlasUI.findRegion("menu"), (Sudoku.WIDTH / 2) - ((Board.TILE_SIZE * 6) / 2), Board.TILE_SIZE);
        batch.end();

        board.draw(batch);
    }

    public class Nums{
        public Vector2 pos = new Vector2();
        public int size;
        public int num;
        public boolean active = false;

        public Nums(Vector2 p, int s, int n){
            this.pos.x = p.x;
            this.pos.y = p.y;
            this.size = s;
            this.num = n;
        }

        public void drawNums(){
            if(activeNum == num)
                batch.draw(Sudoku.atlasUI.findRegion("selected"), (int)pos.x, (int)pos.y);

            TextureRegion thisNum = this.active ? atlasNumbersActive.findRegion("number" + num) : atlasNumbers.findRegion("number" + num);
            batch.draw(thisNum, (int)pos.x + (size / 2) - (thisNum.getRegionWidth() / 2), (int)pos.y + (size / 2) - (thisNum.getRegionHeight() / 2));
        }
    }
}
