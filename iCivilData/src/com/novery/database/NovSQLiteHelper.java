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
	 * �������ǰһ�δ������ݿ�汾��һ��ʱ����ɾ�����ٴ����±�
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	}

	
}
