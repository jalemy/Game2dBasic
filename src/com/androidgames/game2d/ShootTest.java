package com.androidgames.game2d;

import java.util.ArrayList;
import java.util.List;

import com.androidgames.framework.Game;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.Screen;
import com.androidgames.framework.gl.SpatialHashGrid;
import com.androidgames.framework.gl.Vertices;
import com.androidgames.framework.impl.GLGame;
import com.androidgames.framework.impl.GLGraphics;
import com.androidgames.framework.math.Vector2;

public class ShootTest extends GLGame {
	@Override
	public Screen getStartScreen() {
		return new ShootScreen(this);
	}
	
	class ShootScreen extends Screen {
		final float WORLD_WIDTH = 480;
		final float WORLD_HEIGHT = 800;
		GLGraphics glGraphics;
		Ship ship;
		List<DynamicGameObject> bullet;
		List<DynamicGameObject> targets;
		SpatialHashGrid grid;
		
		Vertices shipVertices;
		Vertices bulletVertices;
		Vertices targetVertices;
		
		Vector2 touchPos = new Vector2();
		
		public ShootScreen(Game game) {
			super(game);
			glGraphics = ((GLGame)game).getGLGraphics();
			
			ship = new Ship(WORLD_WIDTH / 2, 50, 30, 30);
			grid = new SpatialHashGrid(WORLD_WIDTH, WORLD_HEIGHT, 100);
			bullet = new ArrayList<DynamicGameObject>();
			
			shipVertices = new Vertices(glGraphics, 3, 0, false, false);
			shipVertices.setVertices(new float[] { -30f, -30f,
										            30f, 0.0f,
										           -30f,  30f}, 0, 6);
			
			bulletVertices = new Vertices(glGraphics, 4, 6, false, false);
			bulletVertices.setVertices(new float[] { -10f, -10f,
													  10f, -10f,
													  10f,  10f,
													 -10f,  10f}, 0, 8);
			bulletVertices.setIndices(new short[] { 0, 1, 2, 2, 3, 0}, 0, 6);
			
			targetVertices = new Vertices(glGraphics, 4, 6, false, false);
			targetVertices.setVertices(new float[] { -15f, -15f,
												      15f, -15f,
												      15f,  15f,
												     -15f,  15f}, 0, 8);
			targetVertices.setIndices(new short[] { 0, 1, 2, 2, 3, 0}, 0, 6);
		}

		@Override
		public void update(float deltaTime) {
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			game.getInput().getKeyEvents();
			
			int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				touchPos.x = (event.x / (float)glGraphics.getWidth()) * WORLD_WIDTH;
				touchPos.y = (1 - event.y / (float)glGraphics.getHeight()) * WORLD_HEIGHT;
				
				if (event.type == TouchEvent.TOUCH_UP) {
					
				}
			}
		}

		@Override
		public void present(float deltaTime) {
			// TODO 自動生成されたメソッド・スタブ
			
		}

		@Override
		public void pause() {
			
		}

		@Override
		public void resume() {
			
		}

		@Override
		public void dispose() {
			
		}
		
	}
}
