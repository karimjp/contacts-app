/**
 *This is the Main Activity for the contact list application.
 *All the code in this application is very similar to the
 *examples provided by Scott Stanchfield in the online classes.
 *The code implements a simple contacts application that allows you 
 *to add, edit and display contacts.  Also, it has multi-fragment display
 *when horizontal layout is detected and a contact has been selected. The
 *application has been tested for all the requirements in an android tablet
 *and the developer considers it has no bugs. I was testing in a smartphone
 *and decided to root it due to inability to check the database but something
 *went wrong, my usb cable does not appear to work now and had to change to a tablet to continue.
 *
 *@author Karim Jana
 */

package com.jana.karim.hw2;

import com.jana.karim.hw2.R;
import com.jana.karim.hw2.DisplayFragment.DisplayFragmentListener;
import com.jana.karim.hw2.EditFragment.EditFragmentListener;
import com.jana.karim.hw2.ListFragment.ListFragmentListener;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {
	private EditFragment editFragment;
	private ListFragment listFragment;
	private DisplayFragment displayFragment;
	private long contactSelectedId;
	private final static long NOTHING_SELECTED = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		supportForActionBarOnApi21();
		Log.d("TESTING", "Main Activity On Create -- layout set");

		listFragment = (ListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.listFragment);
		editFragment = (EditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.editFragment);
		displayFragment = (DisplayFragment) getSupportFragmentManager()
				.findFragmentById(R.id.displayFragment);
		contactSelectedId = 0;
		Log.d("TESTING", "Main Activity On Create Fragments set");

		final boolean displayActivityShowing = displayFragment != null
				&& displayFragment.isInLayout();
		final boolean showListOnly = contactSelectedId == NOTHING_SELECTED;
		final boolean dualMode = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

		Log.d("TESTING", "Dual Mode: " + String.valueOf(dualMode));

		listFragment.setListFragmentListener(new ListFragmentListener() {
			@Override
			public void onEdit(long id) {
				Log.d("TESTING", "OnEdit Listener");
				if (dualMode) {
					editFragment.setContactId(id);
				} else {
					Intent intent = new Intent(MainActivity.this,
							EditActivity.class);
					intent.putExtra("contactId", id);
					startActivity(intent);
				}
			}

			@Override
			public void onCreate() {
				Log.d("TESTING", "OnCreate Fragment Listener");
				if (dualMode) {

					if (displayActivityShowing) {
						hideDisplayFragment();
						showEditFragment();
						setEditFragmentListener();
					}
					editFragment.setContactId(-1);

				} else {
					Intent intent = new Intent(MainActivity.this,
							EditActivity.class);
					intent.putExtra("contactId", (long) -1);
					startActivity(intent);
				}
			}

			@Override
			public void onDisplay(long id) {
				if (!(dualMode)) {
					Log.d("TESTING", "OnDisplay Fragment Listener");
					Intent intent = new Intent(MainActivity.this,
							DisplayActivity.class);
					intent.putExtra("contactId", id);
					startActivity(intent);
				}
			}

			@Override
			public void onClick(long id) {

				contactSelectedId = id;

				if (dualMode) {
					Log.d("TEST", "Contact selected id: " + contactSelectedId);
					showDisplayFragment(contactSelectedId);
					setDisplayFragmentListener();
				}
			}
		});

		Log.d("TEST",
				"The user has not selected something : "
						+ String.valueOf(showListOnly));
		Log.d("TEST", "the list passed id is : " + contactSelectedId);
		Log.d("TEST", "the constant : " + NOTHING_SELECTED);

		if (dualMode) {
			// do this until we know if user selected a contact
			if (showListOnly) {
				hideEditFragment();
				hideDisplayFragment();
			}

		}

	}

	public void setEditFragmentListener() {
		editFragment.setEditFragmentListener(new EditFragmentListener() {
			@Override
			public void onDone(Contact item) {
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.hide(editFragment);
				transaction.show(displayFragment);
				transaction.commit();
				displayFragment.setContactId(item.getId());
			}

			@Override
			public void onCancel() {
				editFragment.setContactId(listFragment.getSelectedId());

				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.hide(editFragment);
				transaction.show(displayFragment);
				transaction.commit();
			}
		});
	}

	public void setDisplayFragmentListener() {
		displayFragment
				.setDisplayFragmentListener(new DisplayFragmentListener() {
					@Override
					public void onEdit(long id) {
						editFragment.setContactId(id);
						FragmentTransaction transaction = getSupportFragmentManager()
								.beginTransaction();
						transaction.hide(displayFragment);
						transaction.show(editFragment);
						transaction.commit();
						setEditFragmentListener();
					}

					@Override
					public void onBack() {

					}
				});
	}

	public void hideEditFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.hide(editFragment);
		transaction.commit();
	}

	public void showEditFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.show(editFragment);
		transaction.commit();
	}

	public void hideDisplayFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.hide(displayFragment);
		transaction.commit();

	}

	public void showDisplayFragment(long contactId) {
		displayFragment.setContactId(contactId);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.show(displayFragment);
		transaction.commit();

	}

	public void supportForActionBarOnApi21() {
		/**
		 * References:
		 * https://developer.android.com/reference/android/support/v7
		 * /widget/Toolbar.html
		 * http://stackoverflow.com/questions/26838730/the-application
		 * -icon-does-not-show-on-action-bar The use of application icon plus
		 * title as a standard layout is discouraged on API 21 devices and
		 * newer. In this method we are enabling the action bar and an icon.
		 */
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setLogo(R.drawable.ic_launcher);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
	}
}
