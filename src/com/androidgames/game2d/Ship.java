package com.androidgames.game2d;

public class Ship extends DynamicGameObject {
	int life;
	public Ship(float x, float y, float width, float height) {
		super(x, y, width, height);
		life = 3;
	}

}
