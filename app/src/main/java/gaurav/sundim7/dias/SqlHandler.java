package gaurav.sundim7.dias;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHandler {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "dias";
	// Table Name(s)
	public static final String TABLE_NOTICES = "notices";
	// Commands
	private static final String CREATE_NOTICES_TABLE = "CREATE TABLE " + TABLE_NOTICES
			+ "(_id INTEGER PRIMARY KEY, subject TEXT, date DATE, message TEXT, attachment TEXT);";

	private DataBaseHelper ourHelper;
	private final Context ourCTX;
	public static SQLiteDatabase ourDb;
	public static boolean FLAG_NEW_DATABASE = false;

	public SqlHandler(Context c) {
		ourCTX = c;
	}

	public SqlHandler open() throws SQLException {
		ourHelper = new DataBaseHelper(ourCTX);
		ourDb = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public Cursor getAllNotices() {
		return ourDb.query(TABLE_NOTICES, Notice.KEYS_NOTICE, null, null, null, null, "_id DESC", null);
	}

	public int getLastId(String table) {
		Cursor x = null;
		x = ourDb.query(TABLE_NOTICES, new String[] { Notice.KEY_ID }, null, null, null, null, null, null);

		if (x.getCount() == 0)
			return 0;
		else {
			x.moveToLast();
			final int a = x.getInt(x.getColumnIndex(Notice.KEY_ID));
			return a;
		}
	}

	public boolean saveNoticeRecord(Notice notice) throws Exception{
		boolean insertSuccessful = false;
		final ContentValues contentValues = new ContentValues();
		contentValues.put(Notice.KEY_ID, notice.getId());
		contentValues.put(Notice.KEY_SUBJECT, notice.getSubject());
		contentValues.put(Notice.KEY_DATE, notice.getDate());
		contentValues.put(Notice.KEY_MESSAGE, notice.getMessage());
		contentValues.put(Notice.KEY_ATTACHMENT, notice.getAttachment());
		try {
			insertSuccessful = ourDb.insertOrThrow(TABLE_NOTICES, null, contentValues) > 0;
		}
		catch(Exception e){
			throw new Exception();
		}
		return insertSuccessful;
	}

	public class DataBaseHelper extends SQLiteOpenHelper {
		public DataBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_NOTICES_TABLE);
			FLAG_NEW_DATABASE = true;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS notices");
			this.onCreate(db);
		}
	}

	public String getNoticeIDatPosition(int pos) {
		final Cursor c = ourDb.query(TABLE_NOTICES, new String[] { Notice.KEY_ID }, null, null, null, null,
				null, null);
		c.moveToPosition(pos);
		return c.getString(c.getColumnIndex(Notice.KEY_ID));
	}

	public Notice getNotice(String rowid) {

		final Cursor x = ourDb.rawQuery("SELECT * FROM notices WHERE _id =?;", new String[] { rowid });
		x.moveToFirst();
		final Notice n = new Notice(x.getString(x.getColumnIndex(Notice.KEY_ID)), x.getString(x
				.getColumnIndex(Notice.KEY_SUBJECT)), x.getString(x.getColumnIndex(Notice.KEY_DATE)),
				x.getString(x.getColumnIndex(Notice.KEY_MESSAGE)), x.getString(x
						.getColumnIndex(Notice.KEY_ATTACHMENT)));
		return n;
	}

	public Cursor fetchNoticesBySubject(String string) {
		return ourDb.rawQuery("SELECT * FROM notices WHERE subject LIKE ?;", new String[] { "%" + string
				+ "%" });
	}

}