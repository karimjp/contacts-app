package com.jana.karim.hw2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jana.karim.hw2.R;
import com.jana.karim.hw2.DatePickerDialogFragment.OnDatePickerDialogFragmentDateSetListener;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditFragment extends Fragment implements
		OnDatePickerDialogFragmentDateSetListener {

	public interface EditFragmentListener {
		void onDone(Contact item);

		void onCancel();
	}

	private static final int BIRTHDAY = 1;
	private EditFragmentListener editFragmentListener;

	public void setEditFragmentListener(
			EditFragmentListener editFragmentListener) {
		this.editFragmentListener = editFragmentListener;
	}

	private Contact contact;
	private EditText emailAddress;
	private EditText mobilePhone;
	private EditText workPhone;
	private EditText homePhone;
	private Button birthdayButton;
	private Calendar birthdayData = Calendar.getInstance();
	private TextView birthday;
	private EditText lastName;
	private EditText firstName;

	public void setContactId(long contactId) {
		if (contactId != -1) {
			contact = ContactsContentProvider.findContact(getActivity(),
					contactId);
		} else {
			contact = new Contact("", "", Calendar.getInstance(), "", "", "",
					"");
		}

		// set Text on Edit View
		firstName.setText(contact.getFirstName());
		lastName.setText(contact.getLastName());
		birthday.setText(contact.getBirthdayString());
		homePhone.setText(contact.getWorkPhone());
		workPhone.setText(contact.getWorkPhone());
		mobilePhone.setText(contact.getMobilePhone());
		emailAddress.setText(contact.getEmailAddress());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit, container, false);

		// declaration of Edit layout file
		firstName = (EditText) view.findViewById(R.id.firstName);
		lastName = (EditText) view.findViewById(R.id.lastName);
		birthday = (TextView) view.findViewById(R.id.birthday);
		birthdayButton = (Button) view.findViewById(R.id.birthdayButton);
		homePhone = (EditText) view.findViewById(R.id.homePhone);
		workPhone = (EditText) view.findViewById(R.id.workPhone);
		mobilePhone = (EditText) view.findViewById(R.id.mobilePhone);
		emailAddress = (EditText) view.findViewById(R.id.emailAddress);

		if (savedInstanceState != null) {
			birthdayData
					.setTimeInMillis(savedInstanceState.getLong("birthday"));

		} else {
			birthdayData.set(1966, 11, 15);

		}
		updateBirthdayText(birthday, birthdayData);

		birthdayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialogFragment fragment = DatePickerDialogFragment
						.create(EditFragment.this, BIRTHDAY, birthdayData);
				fragment.show(getActivity().getSupportFragmentManager(),
						"setting birthday");
			}
		});

		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong("birthday", birthdayData.getTimeInMillis());
	}

	private void updateBirthdayText(TextView birthday, Calendar calendar) {
		birthday.setText(calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH));

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.edit, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_cancel:
			if (editFragmentListener == null)
				throw new RuntimeException(
						"You must set an EditFragmentListener");
			editFragmentListener.onCancel();
			return true;
		case R.id.action_done:
			this.contact.setFirstName(firstName.getText().toString());
			this.contact.setLastName(lastName.getText().toString());
			this.contact.setBirthday(stringToCalendarConverter(birthday
					.getText().toString()));
			this.contact.setHomePhone(homePhone.getText().toString());
			this.contact.setWorkPhone(workPhone.getText().toString());
			this.contact.setMobilePhone(mobilePhone.getText().toString());
			this.contact.setEmailAddress(emailAddress.getText().toString());

			ContactsContentProvider.updateContact(getActivity(), this.contact);
			if (editFragmentListener == null)
				throw new RuntimeException(
						"You must set an EditFragmentListener");
			editFragmentListener.onDone(this.contact);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static Calendar stringToCalendarConverter(String dateFormatString) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(df.parse(dateFormatString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}

	@Override
	public void onDateSet(int dateId, int year, int month, int day) {
		switch (dateId) {
		case BIRTHDAY:
			birthdayData.set(year, month, day);
			updateBirthdayText(birthday, birthdayData);
			break;
		default:
			throw new IllegalStateException("unexpected dateId " + dateId);
		}
	}

}
