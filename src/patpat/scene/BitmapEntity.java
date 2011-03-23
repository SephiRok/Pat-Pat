package patpat.scene;

import patpat.gui.GUI;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;

/**
 * Entity represented by an image.
 */
public class BitmapEntity extends BitmapDrawable {
	
	int mColor = 0xFFFFFFFF;
	
	/**
	 * Constructs an entity from bitmap resource id.
	 */
	public BitmapEntity(int id) {
		super(BitmapFactory.decodeResource(
				GUI.getInstance().getContext().getResources(), id));
		setBounds(0, 0, getIntrinsicWidth(), getIntrinsicHeight());
	}
	
	/**
	 * Sets the color.
	 */
	public void setColor(int color) {
		mColor = color;
		setColorFilter(mColor, PorterDuff.Mode.MULTIPLY);
	}
	
}
