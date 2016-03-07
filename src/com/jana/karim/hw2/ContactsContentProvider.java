package com.jana.karim.hw2;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.jana.karim.hw2.EditFragment;

public class ContactsContentProvider extends ContentProvider {

	// Database Constants
	// Table Name
	private static final String CONTACTS_TABLE = "contacts";
	// Column Names
	public static final String ID = "_id";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String BIRTHDAY = "birthday";
	public static final String HOME_PHONE = "home_phone";
	public static final String WORK_PHONE = "work_phone";
	public static final String MOBILE_PHONE = "mobile_phone";
	public static final String EMAIL_ADDRESS = "email_address";
	// Database Version
	public static final int DB_VERSION = 1;

	private static class OpenHelper extends SQLiteOpenHelper {
		public OpenHelper(Context context) {
			super(context, "CONTACTS", null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d("TEST", "CREATING NEW DATABASE");
			try {
				db.beginTransaction();
				// always keep version 1 creation
				String sql = String
						.format("create table %s (%s integer primary key autoincrement, %s text, %s text, %s text"
								+ ", %s text, %s text, %s text, %s text)",
								CONTACTS_TABLE, ID, FIRST_NAME, LAST_NAME,
								BIRTHDAY, HOME_PHONE, WORK_PHONE, MOBILE_PHONE,
								EMAIL_ADDRESS);
				db.execSQL(sql);
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	private SQLiteDatabase db;

	@Override
	public boolean onCreate() {
		db = new OpenHelper(getContext()).getWritableDatabase();
		return true; // data source opened ok!
	}

	// URI Constants
	public static final int CONTACTS = 1;
	public static final int CONTACT_ITEM = 2;
	public static final String AUTHORITY = "com.jana.karim.hw2";
	public static final String BASE_PATH = "contacts";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/vnd.jana.karim.hw2";
	public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/vnd.jana.karim.hw2";
	private static final UriMatcher URI_MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		URI_MATCHER.addURI(AUTHORITY, BASE_PATH, CONTACTS);
		// if we see content://com.javadude.todo/todo -> return TODOS (1)
		// if we see content://com.jana.karim.hw2/contacts -> return
		// CONTACTS (1)
		URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", CONTACT_ITEM);
		// if we see content://com.javadude.todo/todo/42 -> return TODO_ITEM (2)
		// if we see content://com.jana.karim.hw2/contacts/42 -> return
		// CONTACT_ITEM (2)
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (URI_MATCHER.match(uri)) {
		case CONTACTS: {
			Log.d("TEST", "I am in CONTACTS");
			// get all TODOS
			Log.d("TEST", "Query");
			Log.d("TEST", "projection" + projection.length);
			Cursor c = db.query(CONTACTS_TABLE, projection, selection,
					selectionArgs, null, null, // groupby, having
					sortOrder);

			// NOTE: we must set the notification URI on the cursor
			// so it can listen for changes that we might make!
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
		}
		case CONTACT_ITEM: {
			Log.d("TEST", "I am in CONTACT_ITEM");
			// get specific TODO
			String id = uri.getLastPathSegment();
			Cursor c = db.query(CONTACTS_TABLE, projection, ID + "=?", // DO NOT
																		// simply
																		// say
																		// ID +
																		// "=" +
																		// id!
																		// SQL
																		// INJECTION!
					new String[] { id }, null, null, // groupby, having
					sortOrder);
			// NOTE: we must set the notification URI on the cursor
			// so it can listen for changes that we might make!
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
		}
		default:
			return null; // unknown
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (URI_MATCHER.match(uri)) {
		case CONTACTS:
			return CONTENT_DIR_TYPE;
		case CONTACT_ITEM:
			return CONTENT_ITEM_TYPE;
		default:
			return null; // unknown
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long id = db.insert(CONTACTS_TABLE, null, values);
		// the null is the "null column hack"
		// -- if inserting an empty row, specify the name of a nullable column
		Uri insertedUri = Uri.withAppendedPath(CONTENT_URI, "" + id);
		getContext().getContentResolver().notifyChange(insertedUri, null);
		return insertedUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int numDeleted = db.delete(CONTACTS_TABLE, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return numDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int numUpdated = db.update(CONTACTS_TABLE, values, selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return numUpdated;
	}

	// static helper methods
	public static Contact findContact(Context context, long id) {
		Uri uri = Uri.withAppendedPath(CONTENT_URI, "" + id);
		String[] projection = { ID, FIRST_NAME, LAST_NAME, BIRTHDAY,
				HOME_PHONE, WORK_PHONE, MOBILE_PHONE, EMAIL_ADDRESS };
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(uri, projection,
					ID + "=" + id, null, FIRST_NAME + " ASC");
			if (cursor == null || !cursor.moveToFirst())
				return null;

			return new Contact(
					cursor.getLong(0),
					cursor.getString(1),
					cursor.getString(2),
					EditFragment.stringToCalendarConverter(cursor.getString(3)),
					cursor.getString(4), cursor.getString(5), cursor
							.getString(6), cursor.getString(7));
		} finally {
			// BE SURE TO CLOSE THE CURSOR!!!
			if (cursor != null)
				cursor.close();
		}
	}

	public static void updateContact(Context context, Contact contact) {
		Uri uri = Uri.withAppendedPath(CONTENT_URI, "" + contact.getId());
		ContentValues values = new ContentValues();
		values.put(FIRST_NAME, contact.getFirstName());
		values.put(LAST_NAME, contact.getLastName());
		values.put(BIRTHDAY, contact.getBirthdayString());
		values.put(HOME_PHONE, contact.getHomePhone());
		values.put(WORK_PHONE, contact.getWorkPhone());
		values.put(MOBILE_PHONE, contact.getMobilePhone());
		values.put(EMAIL_ADDRESS, contact.getEmailAddress());
		if (contact.getId() == -1) {
			Log.d("TEST", "about to insert " + uri);
			Uri insertedUri = context.getContentResolver().insert(uri, values);
			Log.d("TEST", "inserted" + uri);
			String idString = insertedUri.getLastPathSegment();
			long id = Long.parseLong(idString);
			contact.setId(id);
		} else {
			context.getContentResolver().update(uri, values,
					ID + "=" + contact.getId(), null);
		}
	}

}
