package com.nrk.ltu;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLogic extends Thread {

	private SurfaceHolder surfaceHolder;
	private GameView mGameView;
	private int game_state;
	public static final int LOST = 0;
	public static final int WIN = 1;
	public static final int RUNNING = 2;
	private int x;

	public GameLogic(SurfaceHolder surfaceHolder, GameView mGameView) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.mGameView = mGameView;
	}

	public void setGameState(int gamestate) {
		this.game_state = gamestate;
	}

	@Override
	public void run() {
		long time_orig = System.nanoTime();
		long time_interim;
		Canvas canvas = null;

		while (game_state == RUNNING) {
			canvas = null;
			try {

				canvas = this.surfaceHolder.lockCanvas();

				synchronized (surfaceHolder) {
					try {
						Thread.sleep(30);
					} catch (InterruptedException e1) {
					}

					time_interim = System.nanoTime();
					int adj_mov = (int) (time_interim - time_orig);
					mGameView.processMotionEvent(x);
					time_orig = time_interim;
					this.mGameView.onDraw(canvas);
					mGameView.update(adj_mov);
				}
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}

		if (game_state == LOST) {
			mGameView.finish("Oh no you lost :-( ");
		}

		if (game_state == WIN) {
			mGameView.finish("Congratulations you win :-)");
		}
	}

	public void setX(int input) {
		x = input;
	}

}
