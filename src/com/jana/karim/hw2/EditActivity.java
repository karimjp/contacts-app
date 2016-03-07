/**
 *This is the Edit Activity for the contact list application.
 */
package com.jana.karim.hw2;

import com.jana.karim.hw2.R;
import com.jana.karim.hw2.EditFragment.EditFragmentListener;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.os.Bundle;

public class EditActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		supportForActionBarOnApi21();
		Log.d("TESTING", "EditActivity  onCreate");
		EditFragment editFragment = (EditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.editFragment);

		long contactId = getIntent().getLongExtra("contactId", -1);
		editFragment.setContactId(contactId);

		editFragment.setEditFragmentListener(new EditFragmentListener() {
			@Override
			public void onDone(Contact item) {
				getIntent().putExtra("contactId", item.getId());
				finish();
			}

			@Override
			public void onCancel() {
				finish();
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
