package com.androidgames.game2d;

import javax.microedition.khronos.opengles.GL10;

import com.androidgames.framework.Game;
import com.androidgames.framework.Screen;
import com.androidgames.framework.gl.Animation;
import com.androidgames.framework.gl.Camera2D;
import com.androidgames.framework.gl.SpriteBatcher;
import com.androidgames.framework.gl.Texture;
import com.androidgames.framework.gl.TextureRegion;
import com.androidgames.framework.impl.GLGame;
import com.androidgames.framework.impl.GLGraphics;

public class AnimationTest extends GLGame {
	@Override
	public Screen getStartScreen() {
		return new AnimationScreen(this);
	}
	
	class AnimationScreen extends Screen {
		final float WORLD_WIDTH = 9.6f;
		final float WORLD_HEIGHT = 6.4f;
		
		static final int NUM_CAVEMAN = 10;
		GLGraphics glGraphics;
		Caveman[] cavemans;
		SpriteBatcher batcher;
		Camera2D camera;
		Texture texture;
		Animation walkAnim;
		
		public AnimationScreen(Game game) {
			super(game);
			glGraphics = ((GLGame)game).getGLGraphics();
			cavemans = new Caveman[NUM_CAVEMAN];
			for (int i = 0; i < NUM_CAVEMAN; i++) {
				cavemans[i] = new Caveman((float)Math.random(), (float)Math.random(), 1, 1);
			}
			batcher = new SpriteBatcher(glGraphics, NUM_CAVEMAN);
			camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		}

		@Override
		public void update(float deltaTime) {
			int len = cavemans.length;
			for (int i = 0; i < len; i++) {
				cavemans[i].update(deltaTime);
			}
		}

		@Override
		public void present(float deltaTime) {
			GL10 gl = glGraphics.getGL();
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			camera.setViewportAndMatrices();
			
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			
			batcher.beginBatch(texture);
			int len = cavemans.length;
			for (int i = 0; i < len; i++) {
				Caveman caveman = cavemans[i];
				TextureRegion keyFrame =
						walkAnim.getKeyFrame(caveman.walkingTime, Animation.ANIMATION_LOOPING);
				batcher.drawSprite(caveman.position.x, caveman.position.y, 
										caveman.velocity.x < 0 ? 1 : -1, 1, keyFrame);
			}
			batcher.endBatch();
		}

		@Override
		public void pause() {
			
		}

		@Override
		public void resume() {
			texture = new Texture(((GLGame)game), "walkanim.png");
			walkAnim = new Animation(0.2f, new TextureRegion(texture, 0, 0, 64, 64),
										   new TextureRegion(texture, 64, 0, 64, 64),
										   new TextureRegion(texture, 128, 0, 64, 64),
										   new TextureRegion(texture, 192, 0, 64, 64));
		}

		@Override
		public void dispose() {
			
		}
		
	}
}
