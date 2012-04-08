package com.nrk.ltu;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private GameObject paddle;
	private GameObject[] brickBlocks;
	private GameObject ball;

	private GameLogic mGameLogic;
	private InputObject inputObjectPool;
	private int game_width;
	private int game_height;

	private MainActivity gameViewActivity;

	private MediaPlayer mp;

	public GameView(MainActivity mainActivity) {
		super(mainActivity);
		gameViewActivity = mainActivity;
		getHolder().addCallback(this);
		paddle = new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.paddle), 40, 360);

		ball = new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.ball), 0, 80);
		mGameLogic = new GameLogic(getHolder(), this);
		createInputObjectPool();

		int totalNumberOfBricks = 5;
		int x_coord = 0;
		int y_coord = 50;
		brickBlocks = new GameObject[totalNumberOfBricks];
		for (int i = 0; i < totalNumberOfBricks; i++) {
			x_coord += 40;
			brickBlocks[i] = new GameObject(BitmapFactory.decodeResource(getResources(), R.drawable.block), x_coord, y_coord);
		}

		mp = MediaPlayer.create(gameViewActivity, R.raw.bounce);

		setFocusable(true);
		setSoundEffectsEnabled(true);
	}

	private void createInputObjectPool() {
		inputObjectPool = new InputObject();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputObject input = inputObjectPool;
		input.useEvent(event);
		mGameLogic.feedInput(input);

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

		ball.setMoveY(4);
		ball.setMoveX(4);

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
		for (GameObject brick : brickBlocks) {
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

		// Bottom Collision
		if (ball_bottom > game_height) {
			// player loses
			mGameLogic.setGameState(GameLogic.LOST);
			return;
		}

		// paddle collision
		if (paddle.collide(ball)) {
			mp.start();
			if (ball_bottom > paddle.getY() && ball_bottom < paddle.getY() + 50) {
				ball.setMoveY(-ball.getMoveY());
			}
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

		boolean allBricksAreNotDead = false;
		// check for brick collisions
		for (GameObject brick : brickBlocks) {
			if (ball.collide(brick)) {
				brick.setState(GameObject.DEAD);
				mp.start();
				int block_bottom = (int) (brick.getY() + brick.getBitmap().getHeight());
				int block_right = (int) (brick.getX() + brick.getBitmap().getWidth());

				// hits bottom of block
				if (ball_y > block_bottom - 10) {
					ball.setMoveY(-ball.getMoveY());
				}
				// hits top of block
				else if (ball_bottom < brick.getY() + 10) {
					ball.setMoveY(-ball.getMoveY());
				}
				// hits from right
				else if (ball_x > block_right - 10) {
					ball.setMoveX(ball.getMoveX());
				}
				// hits from left
				else if (ball_right < brick.getX() + 10) {
					ball.setMoveX(-ball.getMoveX());
				}
			}
			if (brick.getState() != GameObject.DEAD) {
				allBricksAreNotDead = true;
			}
		}

		if (!allBricksAreNotDead) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			mGameLogic.setGameState(GameLogic.WIN);
			return;
		}

		// perform specific updates
		for (GameObject brick : brickBlocks) {
			brick.update(adj_mov);
		}
		paddle.update(adj_mov);
		ball.update(adj_mov);

	}

	public void processMotionEvent(InputObject input) {
		paddle.setX(input.x);
	}

	public void finish(String status) {
		gameViewActivity.finish(status);
	}
}
