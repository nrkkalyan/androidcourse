package com.nrk.ltu;

import java.util.concurrent.ArrayBlockingQueue;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private SpriteObject paddle;
	private SpriteObject[] brickBlocks;
	private SpriteObject ball;

	private GameLogic mGameLogic;
	private ArrayBlockingQueue<InputObject> inputObjectPool;
	private int game_width;
	private int game_height;

	private Resources res;
	private int[] x_coords;
	private int[] y_coords;
	private int block_count;

	private GameViewActivity gameViewActivity;

	private MediaPlayer mp;

	public GameView(GameViewActivity activity) {
		super(activity);
		gameViewActivity = activity;
		getHolder().addCallback(this);
		paddle = new SpriteObject(BitmapFactory.decodeResource(getResources(), R.drawable.paddle), 40, 350);

		ball = new SpriteObject(BitmapFactory.decodeResource(getResources(), R.drawable.ball), 0, 80);
		mGameLogic = new GameLogic(getHolder(), this);
		createInputObjectPool();

		res = getResources();
		block_count = res.getInteger(R.integer.blocknumber);
		x_coords = res.getIntArray(R.array.x);
		y_coords = res.getIntArray(R.array.y);
		brickBlocks = new SpriteObject[block_count];
		for (int i = 0; i < block_count; i++) {
			brickBlocks[i] = new SpriteObject(BitmapFactory.decodeResource(getResources(), R.drawable.block), x_coords[i], y_coords[i]);
		}

		mp = MediaPlayer.create(gameViewActivity, R.raw.bounce);

		setFocusable(true);
		setSoundEffectsEnabled(true);
	}

	private void createInputObjectPool() {
		inputObjectPool = new ArrayBlockingQueue<InputObject>(20);
		for (int i = 0; i < 20; i++) {
			inputObjectPool.add(new InputObject(inputObjectPool));
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			int hist = event.getHistorySize();
			if (hist > 0) {
				for (int i = 0; i < hist; i++) {
					InputObject input = inputObjectPool.take();
					input.useEventHistory(event, i);
					mGameLogic.feedInput(input);
				}
			}
			InputObject input = inputObjectPool.take();
			input.useEvent(event);
			mGameLogic.feedInput(input);
		} catch (InterruptedException e) {
		}
		try {
			Thread.sleep(16);
		} catch (InterruptedException e) {
		}
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mGameLogic.setGameState(GameLogic.RUNNING);
		mGameLogic.start();

		ball.setMoveY(5);
		ball.setMoveX(5);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mp.release();
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (canvas == null) {
			return;
		}
		canvas.drawColor(Color.WHITE);
		ball.draw(canvas);
		paddle.draw(canvas);
		for (SpriteObject brick : brickBlocks) {
			brick.draw(canvas);
		}
		game_width = canvas.getWidth();
		game_height = canvas.getHeight();
	}

	public void update(int adj_mov) {

		int ball_bottom = (int) (ball.getY() + ball.getBitmap().getHeight());
		int ball_right = (int) (ball.getX() + ball.getBitmap().getWidth());
		int ball_y = (int) ball.getY();
		int ball_x = (int) ball.getX();

		// paddle collision
		if (paddle.collide(ball)) {
			mp.start();
			if (ball_bottom > paddle.getY() && ball_bottom < paddle.getY() + 20) {
				ball.setMoveY(-ball.getMoveY());
			}
		}

		// Bottom Collision
		if (ball_bottom > game_height) {
			ball.setMoveY(-ball.getMoveY());
			// player loses
			mGameLogic.setGameState(GameLogic.LOST);
			return;
		}

		// Top collision
		if (ball_y < 0) {
			ball.setMoveY(-ball.getMoveY());
		}

		// Right-side collision
		if (ball_right > game_width) {
			ball.setMoveX(-ball.getMoveX());
		}

		// Left-side collision
		if (ball_x < 0) {
			ball.setMoveX(-ball.getMoveX());
		}

		// check for brick collisions
		for (SpriteObject brick : brickBlocks) {
			if (ball.collide(brick)) {
				brick.setState(SpriteObject.DEAD);
				mp.start();
				int block_bottom = (int) (brick.getY() + brick.getBitmap().getHeight());
				int block_right = (int) (brick.getX() + brick.getBitmap().getWidth());

				// hits bottom of block
				if (ball_y > block_bottom - 5) {
					ball.setMoveY(ball.getMoveY());
				}
				// hits top of block
				else if (ball_bottom < brick.getY() + 5) {
					ball.setMoveY(-ball.getMoveY());
				}
				// hits from right
				else if (ball_x > block_right - 5) {
					ball.setMoveX(ball.getMoveX());
				}
				// hits from left
				else if (ball_right < brick.getX() + 5) {
					ball.setMoveX(-ball.getMoveX());
				}
			}
		}

		boolean allBricksAreNotDead = false;
		for (SpriteObject brick : brickBlocks) {
			if (brick.getState() != SpriteObject.DEAD) {
				allBricksAreNotDead = true;
				break;
			}
		}
		if (!allBricksAreNotDead) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			mGameLogic.setGameState(GameLogic.WIN);
			return;
		}

		// perform specific updates
		for (SpriteObject brick : brickBlocks) {
			brick.update(adj_mov);
		}
		paddle.update(adj_mov);
		ball.update(adj_mov);

	}

	public void processMotionEvent(InputObject input) {
		paddle.setX(input.x);
	}

	public void processKeyEvent(InputObject input) {

	}

	public void processOrientationEvent(float orientation[]) {

		float roll = orientation[2];
		if (roll < -40) {
			// sprite.setMoveX(2);
		} else if (roll > 40) {
			// sprite.setMoveX(-2);
		}

	}

	public void finish(String status) {
		gameViewActivity.finish(status);
	}
}
