package patpat.scene;

import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import patpat.game.R;
import patpat.gui.GUI;

/**
 * Essentially buttons that consume Beats when appropriately pressed.
 */
public class BeatDrain extends BitmapEntity {

	private final EntityManager mEntityManager;
	private final int mLane;
	private int mPointerID = -1;

	/**
	 * Constructor.
	 */
	public BeatDrain(final EntityManager entityManager, int lane, int y,
			int color) {
		super(R.drawable.beat_drain);
		mEntityManager = entityManager;
		mLane = lane;
		Rect bounds = copyBounds();
		bounds.offsetTo(mEntityManager.getLanePositionX(lane) 
				- getIntrinsicWidth() / 2, mEntityManager.translateY(y, 
						getIntrinsicHeight()));
		setBounds(bounds);
		setColor(color);
	}

	/**
	 * Returns the lane in which it is in.
	 */
	public int getLane() {
		return mLane;
	}

	/**
	 * Handles input events.
	 */
	public boolean handle() {
		if ((handleKey()) || (handleMotion())) {
			return true;
		}
		return false;
	}

	/**
	 * Handles a KeyEvent.
	 */
	private boolean handleKey() {
		if (GUI.getInstance().getEvent() instanceof KeyEvent) {
			KeyEvent event = (KeyEvent) (GUI.getInstance().getEvent());
			if ((mLane == 0) && (event.getKeyCode() == KeyEvent.KEYCODE_Q)) {
				handleKey(event);
				return true;
			} else if ((mLane == 1)
					&& (event.getKeyCode() == KeyEvent.KEYCODE_W)) {
				handleKey(event);
				return true;
			} else if ((mLane == 2)
					&& (event.getKeyCode() == KeyEvent.KEYCODE_E)) {
				handleKey(event);
				return true;
			}
		}
		return false;
	}

	/**
	 * Handles given KeyEvent.
	 */
	private void handleKey(final KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN)
				&& (event.getRepeatCount() == 0)) {
			mEntityManager.handlePat(this);
			setPressed(true);
		} else if ((event.getAction() == KeyEvent.ACTION_UP)
				|| (event.isCanceled())) {
			setPressed(false);
		}
	}

	/**
	 * Handles a MotionEvent.
	 */
	private boolean handleMotion() {
		if (GUI.getInstance().getEvent() instanceof MotionEvent) {
			MotionEvent event = (MotionEvent) (GUI.getInstance().getEvent());
			int action = event.getActionMasked();
			//event.getEventTime();
			if ((action == MotionEvent.ACTION_DOWN)
					|| (action == MotionEvent.ACTION_POINTER_DOWN)) {
				int pointerIndex = 0;
				if (action == MotionEvent.ACTION_POINTER_DOWN) {
					pointerIndex = event.getActionIndex();
				}
				if (getBounds().contains((int) (event.getX(pointerIndex)),
						(int) (event.getY(pointerIndex)))) {
					mPointerID = event.getPointerId(pointerIndex);
					mEntityManager.handlePat(this);
					setPressed(true);
					return true;
				}
			} else if ((action == MotionEvent.ACTION_UP)
					|| (action == MotionEvent.ACTION_POINTER_UP)) {
				int pointerIndex = 0;
				if (action == MotionEvent.ACTION_POINTER_UP) {
					pointerIndex = event.getActionIndex();
				}
				if (mPointerID == event.getPointerId(pointerIndex)) {
					setPressed(false);
					mPointerID = -1;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Updates logic.
	 */
	public void update(long elapsedTime) {
	}

	/**
	 * Sets the color.
	 */
	public void setColor(int color) {
		mColor = color;
		setPressed(false);
	}

	/**
	 * Sets if is pressed.
	 */
	private void setPressed(boolean pressed) {
		if (pressed) {
			setColorFilter(mColor, PorterDuff.Mode.MULTIPLY);
		} else {
			setColorFilter(mColor & 0xFFCCCCCC, PorterDuff.Mode.MULTIPLY);
		}
	}

}
