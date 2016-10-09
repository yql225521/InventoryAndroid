package com.gystudio.db;


import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.rstco.assetmgr.AssetEntity;
import com.rstco.sjpt.entity.CfgEntity;
import com.rstco.sjpt.entity.PhotoEntity;
import com.rstco.sjpt.entity.PubDictEntity;
import com.rstco.sjpt.entity.PubOrganEntity;


public class DataHelper  extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "asset.db";// 数据库名称
	private static final int DATABASE_VERSION = 4;// 版本号

	private Dao<CfgEntity,String> cfgDao=null;
	private Dao<AssetEntity,String> assetsDao=null;
	private Dao<PubDictEntity,String> dictDao=null;
	private Dao<PubOrganEntity,String> orgDao=null;
	private Dao<PhotoEntity,String> photoDao=null;


	public DataHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/* (non-Javadoc)
	 * @see com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase, com.j256.ormlite.support.ConnectionSource)
	 */
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource connectionSource) {
		try{
			Log.e(DataHelper.class.getName(), "开始创建数据库");
			TableUtils.createTable(connectionSource,AssetEntity.class);
			TableUtils.createTable(connectionSource,CfgEntity.class);
			TableUtils.createTable(connectionSource,PhotoEntity.class);
			TableUtils.createTable(connectionSource,PubOrganEntity.class);
			TableUtils.createTable(connectionSource,PubDictEntity.class);
			Log.e(DataHelper.class.getName(), "创建数据库成功");

		}catch(SQLException e){
			Log.e(DataHelper.class.getName(), "创建数据库失败", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, com.j256.ormlite.support.ConnectionSource, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int arg2, int arg3) {
		try{
			TableUtils.dropTable(connectionSource, AssetEntity.class, true);
			TableUtils.dropTable(connectionSource, CfgEntity.class, true);
			TableUtils.dropTable(connectionSource, PubOrganEntity.class, true);
			TableUtils.createTable(connectionSource,PhotoEntity.class);
			TableUtils.dropTable(connectionSource, PubDictEntity.class, true);
			onCreate(db, connectionSource);
			Log.e(DataHelper.class.getName(), "更新数据库成功");
		}catch(SQLException e){
			Log.e(DataHelper.class.getName(), "更新数据库失败", e);
		}

	}

	/* (non-Javadoc)
	 * @see com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
		cfgDao=null;
		assetsDao=null;
		dictDao=null;
		orgDao=null;
		photoDao=null;
	}

	public Dao<CfgEntity, String> getCfgDao() throws SQLException {
		if (cfgDao == null) { 
			cfgDao = getDao(CfgEntity.class); 
		} 
		return cfgDao;
	}

	public Dao<AssetEntity, String> getAssetsDao()  throws SQLException {
		if (assetsDao == null) { 
			assetsDao = getDao(AssetEntity.class); 
		} 
		return assetsDao;
	}

	public Dao<PubDictEntity, String> getDictDao()  throws SQLException {
		if (dictDao == null) { 
			dictDao = getDao(PubDictEntity.class); 
		} 
		return dictDao;
	}

	public Dao<PubOrganEntity, String> getOrgDao() throws SQLException {
		if (orgDao == null) { 
			orgDao = getDao(PubOrganEntity.class); 
		} 
		return orgDao;
	}

	public Dao<PhotoEntity,String> getPhotoDao() throws SQLException{
		if (photoDao == null) { 
			photoDao = getDao(PhotoEntity.class); 
		} 
		return photoDao;
	}

	public void setPhotoDao(Dao<PhotoEntity,String> photoDao) {
		this.photoDao = photoDao;
	}

	//  取配置
	//  取基础数据
	//  取资产列表
	//  更新基础数据
	//  更新配置
	//  保存资产记录
	//  删除资产记录
}
