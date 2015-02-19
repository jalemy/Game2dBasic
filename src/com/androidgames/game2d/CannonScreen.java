package com.androidgames.game2d;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.androidgames.framework.Game;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.Screen;
import com.androidgames.framework.gl.Vertices;
import com.androidgames.framework.impl.GLGame;
import com.androidgames.framework.impl.GLGraphics;
import com.androidgames.framework.math.Vector2;

public class CannonScreen extends Screen {
	float FRUSTUM_WIDTH = 800;
	float FRUSTUM_HEIGHT = 480;
	GLGraphics glGraphics;
	Vertices vertices;
	Vector2 cannonPos = new Vector2(400f, 320f);
	float cannonAngle = 0;
	Vector2 touchPos = new Vector2();
	
	public CannonScreen(Game game) {
		super(game);
		glGraphics = ((GLGame)game).getGLGraphics();
		vertices = new Vertices(glGraphics, 3, 0, false, false);
		vertices.setVertices(new float[] { -30f, -30f,
										    50f, 0.0f,
										   -30f, 30f}, 0, 6);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent>touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			
			touchPos.x = (event.x / (float)glGraphics.getWidth()) * FRUSTUM_WIDTH;
			touchPos.y = (1 - event.y / (float)glGraphics.getHeight()) * FRUSTUM_HEIGHT;
			
			cannonAngle = touchPos.sub(cannonPos).angle();
			}
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, FRUSTUM_WIDTH, 0, FRUSTUM_HEIGHT, 1, -1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		gl.glTranslatef(cannonPos.x, cannonPos.y, 0);
		gl.glRotatef(cannonAngle, 0, 0, 1);
		vertices.bind();
		vertices.draw(GL10.GL_TRIANGLES, 0, 3);
		vertices.unbind();
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
