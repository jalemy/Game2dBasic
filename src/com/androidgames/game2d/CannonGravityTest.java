package com.androidgames.game2d;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.androidgames.framework.Game;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.Screen;
import com.androidgames.framework.gl.Vertices;
import com.androidgames.framework.impl.GLGame;
import com.androidgames.framework.impl.GLGraphics;
import com.androidgames.framework.math.Vector2;

public class CannonGravityTest extends GLGame {
	@Override
	public Screen getStartScreen() {
		return new CannonGravityScreen(this);
	}
	
	class CannonGravityScreen extends Screen {
		float FRUSTUM_WIDTH = 800f;
		float FRUSTUM_HEIGHT = 480f;
		GLGraphics glGraphics;
		Vertices cannonVertices;
		Vertices ballVertices;
		Vector2 cannonPos = new Vector2(50, 50);
		float cannonAngle = 0;
		Vector2 touchPos = new Vector2();
		Vector2 ballPos = new Vector2(-10, -10);
		Vector2 ballVelocity = new Vector2(0, 0);
		Vector2 gravity = new Vector2(0, -10);
		
		public CannonGravityScreen(Game game) {
			super(game);
			glGraphics = ((GLGame)game).getGLGraphics();
			cannonVertices = new Vertices(glGraphics, 3, 0, false, false);
			cannonVertices.setVertices(new float[] { -30f, -30f,
													  50f, 0f,
													 -30f, 30f }, 0, 6);
			ballVertices = new Vertices(glGraphics, 4, 6, false, false);
			ballVertices.setVertices(new float[] { -6f, -6f,
												    6f, -6f,
												    6f, 6f,
												   -6f, 6f }, 0, 8);
			ballVertices.setIndices(new short[] {0, 1, 2, 2, 3, 0 }, 0, 6);
		}

		@Override
		public void update(float deltaTime) {
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			game.getInput().getKeyEvents();
			
			int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				
				touchPos.x = (event.x / (float)glGraphics.getWidth()) * FRUSTUM_WIDTH;
				touchPos.y = (1 - event.y / (float)glGraphics.getHeight()) * FRUSTUM_HEIGHT;
				cannonAngle = touchPos.sub(cannonPos).angle();
				
				if (event.type == TouchEvent.TOUCH_UP) {
					float radians = cannonAngle * Vector2.TO_RADIANS;
					float ballSpeed = touchPos.len() / 5;
					ballPos.set(cannonPos);
					ballVelocity.x = FloatMath.cos(radians) * ballSpeed;
					ballVelocity.y = FloatMath.sin(radians) * ballSpeed;
				}
			}
			
			ballVelocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
			ballPos.add(ballVelocity.x * deltaTime, ballVelocity.y * deltaTime);
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
			gl.glColor4f(1, 1, 1, 1);
			cannonVertices.bind();
			cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
			cannonVertices.unbind();
			
			gl.glLoadIdentity();
			gl.glTranslatef(ballPos.x, ballPos.y, 0);
			gl.glColor4f(1, 0, 0, 1);
			ballVertices.bind();
			ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
			ballVertices.unbind();
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
