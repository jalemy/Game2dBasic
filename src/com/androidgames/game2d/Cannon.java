package com.androidgames.game2d;

import com.androidgames.framework.GameObject;

public class Cannon extends GameObject {
	public float angle;
	
	public Cannon(float x, float y, float width, float height) {
		super(x, y, width, height);
		angle = 0;
	}
}
