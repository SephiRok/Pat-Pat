// Copyright Rok Breulj <sephirok@gmail.com> 2011.
// Distributed under the Boost Software License, Version 1.0.
// (See accompanying LICENSE file or http://www.boost.org/LICENSE_1_0.txt)

package patpat.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import patpat.core.Mixer;
import patpat.core.StringManager;
import patpat.core.TickrateManager;
import patpat.core.Timer;
import patpat.game.R;
import patpat.scene.EntityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Graphical user interface. Runs separate from the main (event) thread.
 */
public class GUI implements Runnable {
	
	private Bitmap mBackground;
	private Context mContext;
	private long mElapsedTime = 0;
	private ArrayList<EntityManager> mEntityManager = new ArrayList<EntityManager>();
	private Object mEvent;
	private Queue<Object> mEventQueue = new LinkedList<Object>();
	private int mFPSCounter = 0;
	private String mFPSLabel = "";
	private Timer mFPSTimer = new Timer(1000);
	private int mHeight;
	private boolean mQuit = false;
	private SurfaceHolder mSurfaceHolder;
	private final TickrateManager mTickrateManager = new TickrateManager();
	private long mTicks = 0;
	private final Paint mDefaultPaint;
	private int mWidth;
	private WorldView mWorldView;
	
	/**
	 * Singleton implementation.
	 */
	private static class Singleton {
		public static final GUI INSTANCE = new GUI();
	}
	
	/**
	 * Constructor. Context is not yet set, do not try to access it or any
	 * Resources.
	 */
	private GUI() {
		mTickrateManager.setMaxTickrate(TickrateManager.MAXTICKRATE_UNLIMITED);
		mFPSTimer.start();
		mDefaultPaint = new Paint();
		mDefaultPaint.setColor(Color.WHITE);
		mDefaultPaint.setAntiAlias(true);
	}
	
	/**
	 * Draws all objects.
	 */
	private void draw() {
		Canvas canvas = null;
		canvas = mSurfaceHolder.lockCanvas(null);
		if (canvas != null) {
			canvas.drawBitmap(mBackground, 0, 0, null);
			canvas.drawText(mFPSLabel, 0, GUI.getInstance().getHeight() / 2, 
					mDefaultPaint);
			for (EntityManager entityManager : mEntityManager) {
				entityManager.draw(canvas);
			}
			mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
	}
	
	/**
	 * Returns the context.
	 */
	public Context getContext() {
		return mContext;
	}
	
	/**
	 * Returns dimension of given resource id in pixels.
	 */
	public int getDimensionPixelSize(int id) {
		return mContext.getResources().getDimensionPixelSize(id);
	}
	
	/**
	 * Returns current event.
	 */
	public Object getEvent() {
		return mEvent;
	}
	
	/**
	 * Returns height.
	 */
	public int getHeight() {
		return mHeight;
	}
	
	/**
	 * Returns singleton instance.
	 */
	public static GUI getInstance() {
		return Singleton.INSTANCE;
	}
	
	/**
	 * Returns default paint.
	 */
	public Paint getDefaultPaint() {
		return mDefaultPaint;
	}
	
	/**
	 * Returns width.
	 */
	public int getWidth() {
		return mWidth;
	}
	
	/**
	 * Handles input events.
	 */
	private void handle() {
		for (EntityManager entityManager : mEntityManager) {
			if (entityManager.handle()) {
				return;
			}
		}
	}
	
	/**
	 * Sets oldest unhandled event in queue as current event and returns if 
	 * it is not null.
	 */
	synchronized private boolean hasEvent() {
		mEvent = mEventQueue.poll();
		return mEvent != null;
	}
	
	/**
	 * Initializes the GUI.
	 */
	private void init() {
		mDefaultPaint.setTextSize(getDimensionPixelSize(R.dimen.defaultText));
		mBackground = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.background);
		for (int i = 0; i < StringManager.getInstance().getInt("players"); 
				++i) {
			mEntityManager.add(new EntityManager(i));
		}
		Mixer.getInstance().prepare(StringManager.getInstance().get("song") 
				+ "/" + StringManager.getInstance().get("song") + ".mp3");
		Mixer.getInstance().start();
		mTicks = SystemClock.uptimeMillis();
	}
	
	/**
	 * Pushes an event to the GUI to handle.
	 */
	synchronized public void pushEvent(final Object event) {
		mEventQueue.add(event);
	}
	
	/**
	 * Sets the WorldView -- reference to the main (event) thread.
	 */
	public void setWorldView(final WorldView worldView) {
		mWorldView = worldView;
	}
	
	private void quit() {
		Mixer.getInstance().stop();
		mEntityManager.clear();
	}
	
	/**
	 * Runs the main loop.
	 */
	@Override public void run() {
		init();
		while (!mQuit) {
			while (hasEvent()) {
				handle();
			}
			update();
			draw();
			synchronized(mWorldView) {
				mWorldView.notify();
			}
			mTickrateManager.delay();
		}
		quit();
	}
	
	/**
	 * Sets the context under which the GUI is running.
	 */
	public void setContext(final Context context) {
		mContext = context;
	}
	
	/**
	 * Sets the height.
	 */
	public void setHeight(int height) {
		mHeight = height;
	}
	
	/**
	 * Sets if the GUI should leave the main loop.
	 */
	public void setQuit(boolean quit) {
		mQuit = quit;
	}
	
	/**
	 * Sets the surface on which to draw.
	 */
	public void setSurfaceHolder(final SurfaceHolder surfaceHolder) {
		mSurfaceHolder = surfaceHolder;
	}
	
	/**
	 * Sets the width.
	 */
	public void setWidth(int width) {
		mWidth = width;
	}
	
	/**
	 * Updates logic.
	 */
	private void update() {
		updateElapsedTime();
		updateFPSCounter();
		int songTime = Mixer.getInstance().getCurrentPosition();
		for (EntityManager entityManager : mEntityManager) {
			entityManager.update(songTime);
		}
	}
	
	/**
	 * Updates elapsed time.
	 */
	private void updateElapsedTime() {
		long ticks = SystemClock.uptimeMillis();
		if (ticks > mTicks) {
			mElapsedTime = ticks - mTicks;
		} else {
			mElapsedTime = Long.MAX_VALUE - mTicks + ticks;
		}
		mTicks = ticks;
	}
	
	/**
	 * Updates FPS counter.
	 */
	private void updateFPSCounter() {
		++mFPSCounter;
		mFPSTimer.update(mElapsedTime);
		if (mFPSTimer.isTriggered()) {
			//mFPSLabel = "FPS: " + Integer.toString(mFPSCounter);
			mFPSTimer.reset();
			mFPSCounter = 0;
		}
	}
	
}
