package com.averagecoder.sudoku.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.averagecoder.sudoku.Sudoku;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 896;
        config.title = "Sudoku";
		new LwjglApplication(new Sudoku(), config);
	}
}
