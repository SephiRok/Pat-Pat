package patpat.core;

import android.os.SystemClock;

/**
 * Controls tickrate (application updates per second) by delaying execution.
 */
public class TickrateManager extends Object {
	
	/**
	 * Default max tickrate.
	 */
	public static final int MAXTICKRATE_DEFAULT = 59;

	/**
	 * Unlimited max tickrate; results in no delay.
	 */
	public static final int MAXTICKRATE_UNLIMITED = 0;

	private double mMark = SystemClock.uptimeMillis();
	private int mMaxTickrate = MAXTICKRATE_DEFAULT;
	
	/**
	 * Constructor.
	 */
	public TickrateManager() {}

	/** 
	 * Delays execution by the required amount to ensure mMaxTickrate.
	 */
	public void delay() {
		if (mMaxTickrate > 0) {
			mMark += 1000.0 / mMaxTickrate;
		}
		// FIXME: Blows up if uptimeMillis wraps.
		mMark = Math.max(mMark, (double)(SystemClock.uptimeMillis()));
		if (Math.round(mMark) > SystemClock.uptimeMillis()) {
			try {
				Thread.sleep(Math.round(mMark - SystemClock.uptimeMillis()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the maximum ticks per second.
	 */
	public void setMaxTickrate(int maxTickrate) {
		mMaxTickrate = maxTickrate;
	}

}
