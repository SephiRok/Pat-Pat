package patpat.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Application start point.
 */
public class PatPat extends Activity {
	
	/**
	 * Called when the activity is first created.
	 */
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startActivity(new Intent(this, MainMenuActivity.class));
		finish();
	}

}