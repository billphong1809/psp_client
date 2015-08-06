package phong.android.com.sqlites;

import java.util.ArrayList;
import java.util.List;

import phong.android.com.entity.People;
import phong.android.com.entity.UserLogin;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "friendFavorite";
	private static final String TABLE_USERS = "users";
	private static final String KEY_ID = "id";
	private static final String KEY_NAME_ME = "nameMe";
	private static final String KEY_NAME_FRIEND = "nameFriend";
	private static final String KEY_AVATAR_FRIEND = "avatar";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME_ME
				+ " TEXT," + KEY_NAME_FRIEND + " TEXT," + KEY_AVATAR_FRIEND
				+ " TEXT" + ")";
		db.execSQL(CREATE_USERS_TABLE);

	}

	public void dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

		onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

		// Create tables again
		onCreate(db);
	}

	public synchronized void addUsers(String me, String namefriend,
			String avatarFriend) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME_ME, me);
		values.put(KEY_NAME_FRIEND, namefriend);
		values.put(KEY_AVATAR_FRIEND, avatarFriend);
		db.insert(TABLE_USERS, null, values);
		db.close();
	}

	public synchronized List<UserLogin> getAllUsers(String me) {
		List<UserLogin> listUsers = new ArrayList<UserLogin>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
				+ KEY_NAME_ME + " = " + "'" + me + "'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				UserLogin users = new UserLogin();
				users.setUsersName(cursor.getString(2));
				users.setAvatar(cursor.getString(3));
				listUsers.add(users);
			} while (cursor.moveToNext());
		}

		return listUsers;
	}

	public synchronized void deleteUsers(String usersName) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_NAME_ME + " = '" + usersName + "'", null);

		db.close();
	}

	public int updateUser(People people) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME_FRIEND, people.getUsernameFriend());
		values.put(KEY_AVATAR_FRIEND, people.getThumnaidAvataFriendl());

		return db.update(TABLE_USERS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(people.getIdPeople()) });
	}

	public int getUsersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_USERS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		return cursor.getCount();
	}

	public People getUsers(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
				KEY_NAME_FRIEND, KEY_AVATAR_FRIEND }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		People users = new People(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), null, null, null);

		return users;
	}

}
