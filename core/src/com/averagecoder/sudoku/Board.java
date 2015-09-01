package com.averagecoder.sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

    private String boardKey;
    private String currBoard;
    private TextureAtlas atlas;
    private ShapeRenderer shapeRenderer;
    private Array<Tiles> tiles = new Array<Tiles>();
    OrthographicCamera camera;
    Tiles activeTile = null;

    static final int TILE_SIZE = 64;
    static Array<TextureRegion> numbers = new Array<TextureRegion>();

    public Board(TextureAtlas a, OrthographicCamera cam, ShapeRenderer s){
        atlas = a;

        String puzzleStr = resetBoard(5);
        currBoard = puzzleStr.substring(0, 81);
        boardKey = puzzleStr.substring(81, 81*2);
        shapeRenderer = s;
        camera = cam;

        for(int i = 1; i < 10; i++){
            numbers.add(atlas.findRegion("number"+i));
        }

        Vector2 pos = new Vector2(TILE_SIZE / 2, 256);
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++) {
                char num = currBoard.charAt((9 * r) + c);
                tiles.add(new Tiles(pos, TILE_SIZE, Character.getNumericValue(num)));

                pos.x += TILE_SIZE;
            }
            pos.y += TILE_SIZE;
            pos.x = TILE_SIZE / 2;
        }
    }

    public String resetBoard(int difficulty){
        FileHandle file = Gdx.files.internal("data/puzzles.txt");
        BufferedReader reader = new BufferedReader(file.reader());
        List<String> lines = new ArrayList<String>();
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while( line != null ) {
            lines.add(line);
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Choose a random one from the list
        Random r = new Random();
        String randomString = lines.get(r.nextInt(lines.size()));

        return randomString;
    }

    public void update(float delta){
        if(Gdx.input.justTouched()){
            Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(pos);

            for(Tiles t: tiles){
                if(t.pos.x < pos.x && t.pos.x + t.size > pos.x && t.pos.y < pos.y && t.pos.y + t.size > pos.y){
                    activeTile = t;
                    t.active = true;
                    System.out.println(t.num);
                }
            }
            for(Tiles t: tiles){
                if(t != activeTile){
                    t.active = false;
                }
            }
        }
    }

    public void draw(SpriteBatch batch){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setAutoShapeType(true);
        for(int i = 0; i < tiles.size; i++){
            tiles.get(i).drawSquares();
        }

        Vector2 pos = new Vector2(TILE_SIZE / 2, 256);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect((int) pos.x, (int) pos.y, TILE_SIZE * 9, TILE_SIZE * 9);
        shapeRenderer.rect((int) pos.x - 1, (int) pos.y - 1, (TILE_SIZE * 9) + 2, (TILE_SIZE * 9) + 2);

        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++){
                shapeRenderer.rect((int)pos.x + 1, (int)pos.y + 1, (TILE_SIZE * 3) - 2, (TILE_SIZE * 3) - 2);
                pos.x += TILE_SIZE * 3;
            }
            pos.y += TILE_SIZE * 3;
            pos.x = TILE_SIZE / 2;
        }

        shapeRenderer.end();

        batch.begin();
        for(int i = 0; i < tiles.size; i++){
            tiles.get(i).drawNums(batch);
        }
        batch.end();
    }

    public void addNumber(char number, Vector2 pos){
        int index = ((int)pos.y * 9) + (int)pos.x;

        /*StringBuilder key = new StringBuilder(currBoard);
        key.setCharAt(index, number);

        currBoard = key.toString();*/

        tiles.get(index).num = number;
    }

    public class Tiles{
        private Vector2 pos = new Vector2();
        private int size;
        private int num;
        private boolean active = false;

        public Tiles(Vector2 p, int s, int n){
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

        public void drawNums(SpriteBatch batch){
            if(num > 0){
                TextureRegion thisNum = numbers.get(num-1);
                batch.draw(thisNum, (int)pos.x + (size / 2) - (thisNum.getRegionWidth() / 2), (int)pos.y + (size / 2) - (thisNum.getRegionHeight() / 2));
            }
        }

        public void setNum(int n){
            num = n;
        }
    }
}
