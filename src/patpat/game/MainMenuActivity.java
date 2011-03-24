// Copyright Rok Breulj <sephirok@gmail.com> 2011.
// Distributed under the Boost Software License, Version 1.0.
// (See accompanying LICENSE file or http://www.boost.org/LICENSE_1_0.txt)

package patpat.game;

import patpat.core.StringManager;
import patpat.game.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Main menu activity. The first activity presented to the user.
 */
public class MainMenuActivity extends ListActivity 
		implements OnItemClickListener {
	
	/**
	 * Called when the activity is first created.
	 */
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] options = getResources().getStringArray(
				R.array.mainMenuOptions);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.main, 
				options));
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(this);
	}
	
	/**
	 * Item click handler.
	 */
	@Override public void onItemClick(AdapterView<?> parent, View view, 
			int position, long id) {
		if (id == 0) {
			StringManager.getInstance().set("mode", "play");
			StringManager.getInstance().set("players", 1);
		} else if (id == 1) {
			StringManager.getInstance().set("mode", "play");
			StringManager.getInstance().set("players", 2);
		} else if (id == 2) {
			StringManager.getInstance().set("mode", "highScores");
		}
		startActivity(new Intent(this, SongSelectionActivity.class));
	}

}