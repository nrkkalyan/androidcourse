package com.nrk.ltu;

import java.util.concurrent.ArrayBlockingQueue;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLogic extends Thread {

	private SurfaceHolder surfaceHolder;
	private GameView mGameView;
	private int game_state;
	public static final int LOST = 0;
	public static final int WIN = 1;
	public static final int RUNNING = 2;
	private ArrayBlockingQueue<InputObject> inputQueue = new ArrayBlockingQueue<InputObject>(20);
	private Object inputQueueMutex = new Object();

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
		long time_orig = System.currentTimeMillis();
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

					time_interim = System.currentTimeMillis();
					int adj_mov = (int) (time_interim - time_orig);
					processInput();
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

	public void feedInput(InputObject input) {
		synchronized (inputQueueMutex) {
			try {
				inputQueue.put(input);
			} catch (InterruptedException e) {
			}
		}
	}

	private void processInput() {
		synchronized (inputQueueMutex) {
			ArrayBlockingQueue<InputObject> inputQueue = this.inputQueue;
			while (!inputQueue.isEmpty()) {
				try {
					InputObject input = inputQueue.take();
					if (input.eventType == InputObject.EVENT_TYPE_TOUCH) {
						mGameView.processMotionEvent(input);
					}
					input.returnToPool();
				} catch (InterruptedException e) {
				}
			}
		}
	}

}
