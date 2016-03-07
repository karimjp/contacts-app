package com.jana.karim.hw2;

import java.util.Calendar;

import com.jana.karim.hw2.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayFragment extends Fragment {

	public interface DisplayFragmentListener {
		void onEdit(long id);

		void onBack();
	}

	private DisplayFragmentListener displayFragmentListener;

	public void setDisplayFragmentListener(
			DisplayFragmentListener displayFragmentListener) {
		this.displayFragmentListener = displayFragmentListener;
	}

	private Contact contact;
	private TextView emailAddress;
	private TextView mobilePhone;
	private TextView workPhone;
	private TextView homePhone;
	private TextView birthday;
	private TextView lastName;
	private TextView firstName;

	public void setContactId(long contactId) {

		if (contactId != -1) {
			contact = ContactsContentProvider.findContact(getActivity(),
					contactId);
			Log.d("TEST", contact.getBirthdayString());
			Log.d("TEST", contact.getFirstName());
		} else {
			// this is not suppose to happen in display
			contact = new Contact("", "", Calendar.getInstance(), "", "", "",
					"");
			// throw new
			// RuntimeException("Error: A new contact ID (-1) was returned in Display Mode.  Contact the developer.");
		}
		Log.d("TEST", "about to set text view");
		// set Text on Edit View
		firstName.setText(contact.getFirstName());
		lastName.setText(contact.getLastName());
		birthday.setText(contact.getBirthdayString());
		homePhone.setText(contact.getHomePhone());
		workPhone.setText(contact.getWorkPhone());
		mobilePhone.setText(contact.getMobilePhone());
		emailAddress.setText(contact.getEmailAddress());
		Log.d("TEST", "finish setting text view");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_display, container,
				false);

		Log.d("TEST", "Creating Display View");
		// declaration of Edit layout file
		firstName = (TextView) view.findViewById(R.id.firstName);
		lastName = (TextView) view.findViewById(R.id.lastName);
		birthday = (TextView) view.findViewById(R.id.birthday);
		homePhone = (TextView) view.findViewById(R.id.homePhone);
		workPhone = (TextView) view.findViewById(R.id.workPhone);
		mobilePhone = (TextView) view.findViewById(R.id.mobilePhone);
		emailAddress = (TextView) view.findViewById(R.id.emailAddress);

		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.display, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			if (displayFragmentListener == null)
				throw new RuntimeException(
						"You must set an DisplayFragmentListener");
			displayFragmentListener.onEdit(this.contact.getId());

			return true;
			// Respond to the action bar's Up/Home button
		case android.R.id.home:
			displayFragmentListener.onBack();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		long contactId = getActivity().getIntent()
				.getLongExtra("contactId", -1);
		Log.d("TEST", "contactId: " + contactId);
		setContactId(contactId);
	}

}
