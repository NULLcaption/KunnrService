package com.cxg.kunnr.kunnr.activity.query;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class OrmHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "xpp.db";
	private static final int DATABASE_VERSION = 31;

	private static OrmHelper instance;
	private Dao<PhotoInfo, Integer> photoInfoDao = null;
	private Dao<UserInfo, Integer> userInfoDao = null;

	private OrmHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static OrmHelper getInstance() {
		return instance;
	}

	public static void createInstance(Context context) {
		if (instance == null)
			instance = new OrmHelper(context);
	}

	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, UserInfo.class);
			TableUtils.createTable(connectionSource, PhotoInfo.class);
		} catch (SQLException e) {
			Log.e(OrmHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(OrmHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, UserInfo.class, true);
			TableUtils.dropTable(connectionSource, PhotoInfo.class, true);

			onCreate(db, connectionSource);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		userInfoDao = null;
		photoInfoDao = null;
	}

	public Dao<UserInfo, Integer> getUserInfoDao() throws SQLException {
		if (userInfoDao == null) {
			userInfoDao = getDao(UserInfo.class);
		}
		return userInfoDao;
	}

	public Dao<PhotoInfo, Integer> getPhotoInfoDao() throws SQLException {
		if (photoInfoDao == null) {
			photoInfoDao = getDao(PhotoInfo.class);
		}
		return photoInfoDao;
	}


}