package com.nrk.ltu;

import java.util.concurrent.ArrayBlockingQueue;

import android.app.Activity;
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
	private SpriteObject[] block;
	private SpriteObject ball;

	private GameLogic mGameLogic;
	private ArrayBlockingQueue<InputObject> inputObjectPool;
	private int game_width;
	private int game_height;

	private Resources res;
	private int[] x_coords;
	private int[] y_coords;
	private int block_count;

	private Activity context;

	private MediaPlayer mp;

	public GameView(Activity con) {
		super(con);
		context = con;
		getHolder().addCallback(this);
		paddle = new SpriteObject(BitmapFactory.decodeResource(getResources(), R.drawable.paddle), 40, 390);

		ball = new SpriteObject(BitmapFactory.decodeResource(getResources(), R.drawable.ball), 100, 50);
		mGameLogic = new GameLogic(getHolder(), this);
		createInputObjectPool();

		res = getResources();
		block_count = res.getInteger(R.integer.blocknumber);
		x_coords = res.getIntArray(R.array.x);
		y_coords = res.getIntArray(R.array.y);
		block = new SpriteObject[block_count];
		for (int i = 0; i < block_count; i++) {
			block[i] = new SpriteObject(BitmapFactory.decodeResource(getResources(), R.drawable.block), x_coords[i], y_coords[i]);
		}

		mp = MediaPlayer.create(context, R.raw.bounce);

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
		mp.start();
		ball.setMoveY(-10);
		ball.setMoveX(10);

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
		for (int i = 0; i < block_count; i++) {
			block[i].draw(canvas);
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
			ball.setMoveY(-ball.getMoveY());
			// player loses
			mGameLogic.setGameState(GameLogic.STOP);
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

		// paddle collision
		if (paddle.collide(ball)) {
			if (ball_bottom > paddle.getY() && ball_bottom < paddle.getY() + 20) {
				ball.setMoveY(-ball.getMoveY());
			}
		}

		// check for brick collisions
		for (int i = 0; i < block_count; i++) {
			if (ball.collide(block[i])) {
				block[i].setState(block[i].DEAD);
				mp.start();
				int block_bottom = (int) (block[i].getY() + block[i].getBitmap().getHeight());
				int block_right = (int) (block[i].getX() + block[i].getBitmap().getWidth());

				// hits bottom of block
				if (ball_y > block_bottom - 10) {
					ball.setMoveY(ball.getMoveY());
				}
				// hits top of block
				else if (ball_bottom < block[i].getY() + 10) {
					ball.setMoveY(-ball.getMoveY());
				}
				// hits from right
				else if (ball_x > block_right - 10) {
					ball.setMoveX(ball.getMoveX());
				}
				// hits from left
				else if (ball_right < block[i].getX() + 10) {
					ball.setMoveX(-ball.getMoveX());
				}

			}
		}

		// perform specific updates
		for (int i = 0; i < block_count; i++) {
			block[i].update(adj_mov);
		}
		paddle.update(adj_mov);
		ball.update(adj_mov);

	}

	public void processMotionEvent(InputObject input) {
		paddle.setX(input.x);
		// paddle.setY(input.y);

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

	public void finish() {
		this.context.finish();
	}
}
