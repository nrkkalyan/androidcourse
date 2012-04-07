package com.nrk.ltu;

import java.util.concurrent.ArrayBlockingQueue;

import android.view.MotionEvent;

public class InputObject {
	public static final byte EVENT_TYPE_TOUCH = 2;
	private ArrayBlockingQueue<InputObject> pool;
	public int eventType;
	public int x;

	public InputObject(ArrayBlockingQueue<InputObject> pool) {
		this.pool = pool;
	}

	public void useEvent(MotionEvent event) {
		eventType = EVENT_TYPE_TOUCH;
		x = (int) event.getX();
	}

	public void useEventHistory(MotionEvent event, int historyItem) {
		eventType = EVENT_TYPE_TOUCH;
		x = (int) event.getHistoricalX(historyItem);
	}

	public void returnToPool() {
		pool.add(this);
	}
}