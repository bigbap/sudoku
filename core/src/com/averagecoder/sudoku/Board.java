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
    private TextureAtlas atlasNumbers;
    TextureAtlas atlasNumbersActive;
    private Array<Tiles> tiles = new Array<Tiles>();
    OrthographicCamera camera;
    Tiles activeTile = null;
    int boardActiveNum = 0;

    static final int TILE_SIZE = 64;

    public Board(TextureAtlas an, TextureAtlas ana, OrthographicCamera cam){
        atlasNumbers = an;
        atlasNumbersActive = ana;

        String puzzleStr = resetBoard(5);
        currBoard = puzzleStr.substring(0, 81);
        boardKey = puzzleStr.substring(81, 81*2);
        camera = cam;

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

        return lines.get(r.nextInt(lines.size()));
    }

    public void update(float delta){
        if(Gdx.input.justTouched()){
            Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Sudoku.viewport.unproject(pos);

            for(SudokuMain.Nums n: SudokuMain.numbers){
                if(n.pos.x <= pos.x && n.pos.x + n.size >= pos.x && n.pos.y <= pos.y && n.pos.y + n.size >= pos.y){
                    SudokuMain.activeNum = n.num;
                    n.active = true;
                }
            }
            for(SudokuMain.Nums n: SudokuMain.numbers){
                if(n.num != SudokuMain.activeNum){
                    n.active = false;
                }
            }

            for(Tiles t: tiles){
                if(t.pos.x <= pos.x && t.pos.x + t.size >= pos.x && t.pos.y <= pos.y && t.pos.y + t.size >= pos.y && SudokuMain.activeNum > 0){
                    activeTile = t;
                    t.active = true;
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

        batch.begin();

        for(int i = 0; i < tiles.size; i++){
            tiles.get(i).drawNums(batch);
        }

        batch.draw(Sudoku.atlasUI.findRegion("bg"), TILE_SIZE / 2, 256);

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
        boolean locked = false;

        public Tiles(Vector2 p, int s, int n){
            this.pos.x = p.x;
            this.pos.y = p.y;
            this.size = s;
            this.num = n;
            this.locked = n!= 0;
        }

        public void drawNums(SpriteBatch batch){
            if(num > 0){
                if(this.num == boardActiveNum)
                    batch.draw(Sudoku.atlasUI.findRegion("selected"), (int)pos.x, (int)pos.y);
                else if(locked)
                    batch.draw(Sudoku.atlasUI.findRegion("locked"), (int)pos.x, (int)pos.y);

                TextureRegion thisNum = this.active && !this.locked ? atlasNumbersActive.findRegion("number" + num) : atlasNumbers.findRegion("number" + num);
                batch.draw(thisNum, (int)pos.x + (size / 2) - (thisNum.getRegionWidth() / 2), (int)pos.y + (size / 2) - (thisNum.getRegionHeight() / 2));
            }
        }

        public void setNum(int n){
            num = n;
        }
    }
}
