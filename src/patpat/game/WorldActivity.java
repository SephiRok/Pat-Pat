package patpat.game;

import patpat.core.Mixer;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Activity that wraps the actual gameplay.
 */
public class WorldActivity extends Activity {
	
	/**
	 * Called when the activity is first created.
	 */
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.world);
	}
	
	@Override public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.world_menu, menu);
	    return true;
	}
	
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.mute) {
			if (Mixer.getInstance().getVolume() == 1) {
				Mixer.getInstance().setVolume(0);
				item.setIcon(android.R.drawable.ic_lock_silent_mode);
				item.setTitle(R.string.unmute);
			} else {
				Mixer.getInstance().setVolume(1);
				item.setIcon(android.R.drawable.ic_lock_silent_mode_off);
				item.setTitle(R.string.mute);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override public void onOptionsMenuClosed(Menu menu) {
		Mixer.getInstance().start();
	}
	
	@Override public boolean onPrepareOptionsMenu(Menu menu)  {
		Mixer.getInstance().pause();
		return super.onPrepareOptionsMenu(menu);
	}

}