package com.novery.database;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class NovSQLiteHelper extends SQLiteOpenHelper {
	public NovSQLiteHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	public static final String DB_NAME = "novery.db";
	public static final String TB_NAME = "userinfo";
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("");
	}

	/**
	 * 当检测与前一次创建数据库版本不一样时，先删除表再创建新表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	}

	
}
