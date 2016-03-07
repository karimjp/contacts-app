/**
 *This is the Display Activity for the contact list application.
 */

package com.jana.karim.hw2;

import com.jana.karim.hw2.R;
import com.jana.karim.hw2.DisplayFragment.DisplayFragmentListener;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;

public class DisplayActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("TEST", "creating Display");
		setContentView(R.layout.activity_display);
		Log.d("TEST", "created Display");
		supportForActionBarOnApi21();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		DisplayFragment displayFragment = (DisplayFragment) getSupportFragmentManager()
				.findFragmentById(R.id.displayFragment);

		long contactId = getIntent().getLongExtra("contactId", -1);

		Log.d("TEST", "contactId: " + contactId);
		displayFragment.setContactId(contactId);

		displayFragment
				.setDisplayFragmentListener(new DisplayFragmentListener() {
					@Override
					public void onEdit(long id) {

						Log.d("TEST", "ID before passing to edit Fragment: "
								+ id);
						Intent intent = new Intent(DisplayActivity.this,
								EditActivity.class);
						intent.putExtra("contactId", id);
						startActivity(intent);

					}

					@Override
					public void onBack() {

						NavUtils.navigateUpFromSameTask(DisplayActivity.this);
					}
				});
	}

	public void supportForActionBarOnApi21() {
		/*
		 * References:
		 * *https://developer.android.com/reference/android/support/v7
		 * /widget/Toolbar.html
		 * *http://stackoverflow.com/questions/26838730/the-application
		 * -icon-does-not-show-on-action-bar*The use of application icon plus
		 * title as a standard layout*is discouraged on API 21 devices and
		 * newer. In this method*we are enabling the action bar and an icon.
		 */
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setLogo(R.drawable.ic_launcher);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
	}

}
