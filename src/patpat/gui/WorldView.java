package patpat.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * World view -- the last-in-line system-managed UI component. Receives system 
 * events.
 */
public class WorldView extends SurfaceView implements SurfaceHolder.Callback {
	
	private Thread mGUIThread;
	
	/**
	 * Constructor.
	 */
	public WorldView(Context context) {
		super(context);
		init(context);
	}
	
	/**
	 * Constructor for XML.
	 */
	public WorldView(Context context, AttributeSet attributes) {
		super(context, attributes);
		init(context);
	}
	
	/**
	 * Initializes the view.
	 */
	private void init(Context context) {
		getHolder().addCallback(this);
		GUI.getInstance().setContext(context);
		GUI.getInstance().setSurfaceHolder(getHolder());
		/*GUI.getInstance().setDisplay(((WindowManager)(getSystemService(
				Context.WINDOW_SERVICE))).getDefaultDisplay());*/
		GUI.getInstance().setWorldView(this);
		setFocusableInTouchMode(true);
		requestFocus();
		mGUIThread = new Thread(GUI.getInstance());
	}
	
	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		GUI.getInstance().pushEvent(new KeyEvent(event));
		return false;
	}
	
	@Override public boolean onKeyUp(int keyCode, KeyEvent event) {
		GUI.getInstance().pushEvent(new KeyEvent(event));
		return false;
	}
	
	@Override public boolean onTouchEvent(MotionEvent event) {
		GUI.getInstance().pushEvent(MotionEvent.obtain(event));
		synchronized(this) {
			try {
	            wait(1000);
	        } catch (InterruptedException e) {
	        }
		}
		return true;
	}

	@Override public void surfaceChanged(SurfaceHolder holder, int format, 
			int width, int height) {}

	@Override public void surfaceCreated(SurfaceHolder holder) {
		GUI.getInstance().setHeight(getHeight());
		GUI.getInstance().setWidth(getWidth());
		GUI.getInstance().setQuit(false);
		mGUIThread.start();
	}

	@Override public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
        GUI.getInstance().setQuit(true);
        while (retry) {
            try {
                mGUIThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}

}