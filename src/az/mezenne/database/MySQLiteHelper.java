package az.mezenne.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "rates.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_RATE = "mezenne";
	public static final String TABLE_ARTICLE = "article";

	public static final String ID = "id";
	public static final String DATE = "DATE1";
	public static final String VALUTA = "valuta1";
	public static final String TRADE = "TRADE1";
	public static final String PETROL = "PETROL1";

	public static final String ARTICLE_ID = "id";
	public static final String TITLE = "title"; // Title of article
	public static final String NAME = "name"; // Auther's name
	public static final String IMAGE = "image"; // Auther's image

	// Database creation sql statement
	private static final String CREATE_TABLE_RATE = "CREATE TABLE "
			+ TABLE_RATE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ VALUTA + " text, " + TRADE + " text, " + PETROL + " text, "
			+ DATE + " text);";

	private static final String CREATE_TABLE_ARTICLE = "CREATE TABLE "
			+ TABLE_ARTICLE + "(" + ARTICLE_ID + " INTEGER PRIMARY KEY, "
			+ TITLE + " text, " + NAME + " text, " + IMAGE + " text);";

	private MySQLiteHelper db;

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_RATE);
		database.execSQL(CREATE_TABLE_ARTICLE);
		Log.i("onCreate", "Database created");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
		onCreate(db);
	}

	public void addDivs(Divs divs) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(VALUTA, divs.getValuta());
		values.put(TRADE, divs.getTrade());
		values.put(PETROL, divs.getPetrol());
		values.put(DATE, divs.getDate());

		db.insert(TABLE_RATE, null, values);
		db.close();

	}

	public void addArticle(Article article) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ARTICLE_ID, article.getId());
		values.put(TITLE, article.getTitle());
		values.put(NAME, article.getName());
		values.put(IMAGE, article.getImage());

		db.insert(TABLE_ARTICLE, null, values);
		db.close();
	}

	public List<Divs> getAllDivs() {
		List<Divs> divs = new ArrayList<Divs>();

		String selectQuery = "SELECT * from " + TABLE_RATE;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Divs div = new Divs();
				div.setId(Integer.parseInt(cursor.getString(0)));
				div.setValuta(cursor.getString(1));
				div.setTrade(cursor.getString(2));
				div.setPetrol(cursor.getString(3));
				div.setDate(cursor.getString(4));

				divs.add(div);
			} while (cursor.moveToNext());
		}

		return divs;

	}

	public List<Article> getAllArticle() {
		List<Article> articles = new ArrayList<Article>();

		String selectQuery = "SELECT * from " + TABLE_ARTICLE + " ORDER BY ID  DESC;";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Article article = new Article();
				article.setId(Integer.parseInt(cursor.getString(0)));
				article.setTitle(cursor.getString(1));
				article.setName(cursor.getString(2));
				article.setImage(cursor.getString(3));

				articles.add(article);
			} while (cursor.moveToNext());
		}

		return articles;

	}

	public int updateDiv(Divs divs) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(VALUTA, divs.getValuta());
		values.put(TRADE, divs.getTrade());
		values.put(PETROL, divs.getPetrol());
		values.put(DATE, divs.getDate());

		return db.update(TABLE_RATE, values, ID + " = ?",
				new String[] { String.valueOf(divs.getId()) });
	}

	public void deleteDiv(Divs divs) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_RATE, ID + " = ?",
				new String[] { String.valueOf(divs.getId()) });
		db.close();
	}

	public int getDivsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_RATE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	public int getArticleCount() {
		String countQuery = "SELECT  * FROM " + TABLE_ARTICLE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	public Divs getDiv(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_RATE, new String[] { ID, VALUTA, TRADE,
				PETROL, DATE }, ID + "=?", new String[] { String.valueOf(id) },
				null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Divs div = new Divs(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4));
		// return div
		return div;
	}

	public Article getArticle(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ARTICLE, new String[] { ARTICLE_ID,
				TITLE, NAME, IMAGE }, ARTICLE_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();

			Article article = new Article(
					Integer.parseInt(cursor.getString(0)), cursor.getString(1),
					cursor.getString(2), cursor.getString(3));
			// return article
			return article;
		} else {
			return null;
		}
	}

	public boolean isAdded(Article a, Context context) {
		db = new MySQLiteHelper(context);
		List<az.mezenne.database.Article> articles = db.getAllArticle();
		for (Article article : articles) {
			if (a.equals(article))
				return true;
		}
		return false;
	}

	public long getMaxId() {
		String query = "SELECT MAX(id) AS max_id FROM " + TABLE_ARTICLE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		int id = 0;
		if (cursor.moveToFirst()) {
			do {
				id = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		return id;
	}
}