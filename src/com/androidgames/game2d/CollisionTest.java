package com.androidgames.game2d;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.androidgames.framework.Game;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.Screen;
import com.androidgames.framework.gl.SpatialHashGrid;
import com.androidgames.framework.gl.Vertices;
import com.androidgames.framework.impl.GLGame;
import com.androidgames.framework.impl.GLGraphics;
import com.androidgames.framework.math.OverlapTester;
import com.androidgames.framework.math.Vector2;

public class CollisionTest extends GLGame {
	@Override
	public Screen getStartScreen() {
		return new CollisionScreen(this);
	}
	
	class CollisionScreen extends Screen {
		final int NUM_TARGETS = 50;
		final float WORLD_WIDTH = 800;
		final float WORLD_HEIGHT = 480;
		GLGraphics glGraphics;
		Cannon cannon;
		DynamicGameObject ball;
		List<GameObject> targets;
		SpatialHashGrid grid;
		
		Vertices cannonVertices;
		Vertices ballVertices;
		Vertices targetVertices;
		
		Vector2 touchPos = new Vector2();
		Vector2 gravity = new Vector2(0, (float)(-9.8 * 5.0));
		
		public CollisionScreen(Game game) {
			super(game);
			glGraphics = ((GLGame)game).getGLGraphics();
			
			cannon = new Cannon(30, 30, 30, 30);
			ball = new DynamicGameObject(30, 30, 5f, 5f);
			targets = new ArrayList<GameObject>(NUM_TARGETS);
			grid = new SpatialHashGrid(WORLD_WIDTH, WORLD_HEIGHT, 100);
			for (int i = 0; i < NUM_TARGETS; i++) {
				GameObject target = new GameObject((float)Math.random() * WORLD_WIDTH,
												   (float)Math.random() * WORLD_HEIGHT,
												   20f, 20f);
				grid.insertStaticObject(target);
				targets.add(target);
			}
			
			cannonVertices = new Vertices(glGraphics, 3, 0, false, false);
			cannonVertices.setVertices(new float[] { -30f, -30f,
													  30f, 0.0f,
													 -30f,  30f }, 0, 6);
			
			ballVertices = new Vertices(glGraphics, 4, 6, false, false);
			ballVertices.setVertices(new float[] { -2.5f, -2.5f,
													2.5f, -2.5f,
													2.5f,  2.5f,
												   -2.5f,  2.5f }, 0, 8);
			ballVertices.setIndices(new short[] { 0, 1, 2, 2, 3, 0}, 0, 6);
			
			targetVertices = new Vertices(glGraphics, 4, 6, false, false);
			targetVertices.setVertices(new float[] { -10f, -10f,
													  10f, -10f,
													  10f,  10f,
													 -10f,  10f }, 0, 8);
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
				
				cannon.angle = touchPos.sub(cannon.position).angle();
				
				if (event.type == TouchEvent.TOUCH_UP) {
					float radians = cannon.angle * Vector2.TO_RADIANS;
					float ballSpeed = (float) (touchPos.len() * 0.5);
					ball.position.set(cannon.position);
					ball.velocity.x = FloatMath.cos(radians) * ballSpeed;
					ball.velocity.y = FloatMath.sin(radians) * ballSpeed;
					ball.bounds.lowerLeft.set(ball.position.x - 2.5f, ball.position.y - 2.5f);
				}
			}
			
			ball.velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
			ball.position.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);
			ball.bounds.lowerLeft.add(ball.velocity.x * deltaTime,
									  ball.velocity.y * deltaTime);
			
			List<GameObject> colliders = grid.getPotentialColliders(ball);
			len = colliders.size();
			for (int i = 0; i < len; i++) {
				GameObject collider = colliders.get(i);
				if (OverlapTester.overlapRectangles(ball.bounds, collider.bounds)) {
					grid.removeObject(collider);
					targets.remove(collider);
				}
			}
		}


		@Override
		public void present(float deltaTime) {
			GL10 gl = glGraphics.getGL();
			gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrthof(0, WORLD_WIDTH, 0, WORLD_HEIGHT, 1, -1);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			
			gl.glColor4f(0, 1, 0, 1);
			targetVertices.bind();
			int len = targets.size();
			for (int i = 0; i < len; i++) {
				GameObject target = targets.get(i);
				gl.glLoadIdentity();
				gl.glTranslatef(target.position.x,  target.position.y, 0);
				targetVertices.draw(GL10.GL_TRIANGLES, 0, 6);
			}
			targetVertices.unbind();
			
			gl.glLoadIdentity();
			gl.glTranslatef(ball.position.x, ball.position.y, 0);
			gl.glColor4f(1, 0, 0, 1);
			ballVertices.bind();
			ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
			ballVertices.unbind();
			
			gl.glLoadIdentity();
			gl.glTranslatef(cannon.position.x, cannon.position.y, 0);
			gl.glRotatef(cannon.angle, 0, 0, 1);
			gl.glColor4f(1, 1, 1, 1);
			cannonVertices.bind();
			cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
			cannonVertices.unbind();
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
