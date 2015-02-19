package com.androidgames.game2d;

import com.androidgames.framework.Screen;
import com.androidgames.framework.impl.GLGame;

public class CannonTest extends GLGame {
	
	@Override
	public Screen getStartScreen() {
		return new CannonScreen(this);
	}
}
