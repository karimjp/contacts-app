package com.jana.karim.hw2;

import java.util.Calendar;

public class Contact {

	private long id = -1;
	private String firstName;
	private String lastName;
	private Calendar birthday;
	private String homePhone;
	private String workPhone;
	private String mobilePhone;
	private String emailAddress;

	public Contact(String firstName, String lastName, Calendar birthday,
			String homePhone, String workPhone, String mobilePhone,
			String emailAddress) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.homePhone = homePhone;
		this.workPhone = workPhone;
		this.mobilePhone = mobilePhone;
		this.emailAddress = emailAddress;
	}

	public Contact(long id, String firstName, String lastName,
			Calendar birthday, String homePhone, String workPhone,
			String mobilePhone, String emailAddress) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.homePhone = homePhone;
		this.workPhone = workPhone;
		this.mobilePhone = mobilePhone;
		this.emailAddress = emailAddress;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Calendar getBirthday() {
		return birthday;
	}

	public String getBirthdayString() {
		return (birthday.get(Calendar.YEAR) + "-"
				+ (birthday.get(Calendar.MONTH) + 1) + "-" + birthday
					.get(Calendar.DAY_OF_MONTH));
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


}
