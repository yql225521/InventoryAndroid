/**
 * 文件名：DataManager.java
 *
 * 版本信息：
 * 日期：2014-7-14
 * Copyright 高原工作室  Corporation 2014 
 * 版权所有
 *
 */
package com.gystudio.db;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gystudio.utils.ExtDate;
import com.gystudio.utils.ToolDateTime;
import com.gystudio.utils.ToolFile;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.rstco.assetmgr.AssetEntity;
import com.rstco.sjpt.entity.CfgEntity;
import com.rstco.sjpt.entity.PhotoEntity;
import com.rstco.sjpt.entity.PubDictEntity;
import com.rstco.sjpt.entity.PubOrganEntity;

/**
 * 
 * 项目名称：baseadr
 * 类名称：DataManager
 * 类描述：
 * 创建人：yuanbf
 * 创建时间：2014-7-14 上午10:38:36
 * @version 
 * 
 */
public class DataManager {

	public void deleteDicts(Dao<PubDictEntity,String>entitydao,String userid) throws SQLException{

		DeleteBuilder<PubDictEntity,String> delBuilder=entitydao.deleteBuilder();
		delBuilder.where().eq("userId", userid).prepare();
		delBuilder.delete();

	}

	public void deleteOrgans(Dao<PubOrganEntity,String>entitydao,String userid) throws SQLException{
		DeleteBuilder<PubOrganEntity,String> delBuilder=entitydao.deleteBuilder();
		//delBuilder.where().eq("userId", userid).prepare();
		delBuilder.delete();
	}

	public void deleteAssets(Dao<AssetEntity,String>entitydao,String userid) throws SQLException{
		DeleteBuilder<AssetEntity,String> delBuilder=entitydao.deleteBuilder();
		delBuilder.where().eq("userId", userid).prepare();
		delBuilder.delete();
	}
	
	public void deleteAssetsWithOrgan(Dao<AssetEntity,String>entitydao,String userid,String organCode) throws SQLException{
		DeleteBuilder<AssetEntity,String> delBuilder=entitydao.deleteBuilder();
		delBuilder.where().eq("userId", userid).and().eq("organCode", organCode).prepare();
		delBuilder.delete();
	}
	

	public void saveDictList(Dao<PubDictEntity,String>entitydao,String userid,List<PubDictEntity> dicts) throws SQLException{
		this.deleteDicts(entitydao, userid);
		for (PubDictEntity pubDictEntity : dicts) {
			pubDictEntity.setUserId(userid);
			pubDictEntity.setPid(userid+"_"+pubDictEntity.getID());// 手机库id
			entitydao.createOrUpdate(pubDictEntity);
		}
	}

	public List<PubDictEntity> findDicts(Dao<PubDictEntity,String> entitydao,String userId,String type)throws SQLException{
		Map<String,Object> fmap=new HashMap<String,Object>();
		fmap.put("userId", userId);
		fmap.put("type", type);
		return entitydao.queryForFieldValues(fmap);
	}
	
	public List<PhotoEntity> findPhotos(Dao<PhotoEntity,String> entitydao,String userId,String type)throws SQLException{
		Map<String,Object> fmap=new HashMap<String,Object>();
		fmap.put("userId", userId);
		fmap.put("type", type);
		return entitydao.queryForFieldValues(fmap);
	}
	
	public void deletePhotos(Dao<PhotoEntity,String>entitydao,String userid,String type) throws SQLException{
		DeleteBuilder<PhotoEntity,String> delBuilder=entitydao.deleteBuilder();
		delBuilder.where().eq("userId", userid).and().eq("type", type).prepare();
		delBuilder.delete();
	}
	
	public void deletePhoto(Dao<PhotoEntity,String>entitydao,String id) throws SQLException{ 
		PhotoEntity p=entitydao.queryForId(id);
		if(p!=null){
			String filepath = ToolFile.gainSDCardPath() + "/ams/photo/"+p.getAssetCode()+"/"
					+ ToolDateTime.gainCurrentDate("yyyyMMddhhmmss") + ".jpg"; 
			File outFile = new File(filepath);  
			if (outFile.exists()) {
				System.out.println("delete file==>"+outFile.getPath());
				outFile.delete();
			} 
		} 
		entitydao.deleteById(id);
	}
	
	public void savePhoto(Dao<PhotoEntity,String>entitydao, PhotoEntity pobj,String userId)throws SQLException{ 
		pobj.setUserId(userId);
		pobj.setId(userId+"_"+pobj.getAssetCode());
		entitydao.createOrUpdate(pobj); 
	}

	public void saveOrganList(Dao<PubOrganEntity,String>entitydao,String userid,List<PubOrganEntity> organs)throws SQLException{
		this.deleteOrgans(entitydao, userid);
		for (PubOrganEntity pubOrganEntity : organs) {
			pubOrganEntity.setUserId(userid);
			if(StringUtils.isBlank(pubOrganEntity.getOrganType())){
				pubOrganEntity.setOrganType("0");
			}
			pubOrganEntity.setPid(userid+"_"+pubOrganEntity.getOrganID());// 手机库id
			entitydao.createOrUpdate(pubOrganEntity);
		}
	}
 
	public List<PubOrganEntity> findOrgans(Dao<PubOrganEntity,String> entitydao,String userId,String organType) throws SQLException{
		Map<String,Object> fmap=new HashMap<String,Object>();
		fmap.put("userId", userId); 
		if(StringUtils.isBlank(organType)){
			fmap.put("organType", "0");
		}else{
			fmap.put("organType", organType);
		}
		return entitydao.queryForFieldValues(fmap);
	}
	
	public boolean isExistPdAsset(Dao<AssetEntity,String> entitydao,String assetCode,String userId)throws SQLException{
		Map<String,Object> fmap=new HashMap<String,Object>();
		fmap.put("userId", userId);
		fmap.put("dt", "1");
		fmap.put("assetCode", assetCode);
		List<AssetEntity> assets=entitydao.queryForFieldValues(fmap);
		if(assets.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	public List<PubOrganEntity> findOrgansWithAsset(Dao<PubOrganEntity,String> entitydao,Dao<AssetEntity,String> astEntitydao,String userId) throws SQLException{
 
		StringBuffer sb=new StringBuffer();
		sb.append(" select  o.organID as organID,o.organCode as organCode,o.organName as organName,count(a.assetCode) as num from pub_organ o,asset a where o.organCode= a.organCode ");
		sb.append(" and a.dt='1' and a.userId='").append(userId).append("'").append(" group by o.organCode,o.organName  order by o.organCode   ");
 
		
		GenericRawResults<PubOrganEntity> rawResults = entitydao.queryRaw( sb.toString(),
			new RawRowMapper<PubOrganEntity>() { 
				public PubOrganEntity mapRow(String[] columnNames, String[] resultColumns) {
					return new PubOrganEntity(resultColumns[0],resultColumns[1], resultColumns[2],Double.parseDouble(resultColumns[3])); 
				} 
			}); 
		return rawResults.getResults();
	}

	public synchronized String updateUpid(Dao<AssetEntity,String> entitydao,String userId,String organCode)throws SQLException{
		UpdateBuilder<AssetEntity,String> astUpdateBuilder=entitydao.updateBuilder();
		ExtDate nowdate= new ExtDate();
		String upid=organCode+"-"+nowdate.format("yyyyMMddHHmmssSSS");
		astUpdateBuilder.where().eq("dt", "1").and().eq("organCode", organCode).and().eq("userId", userId);
		astUpdateBuilder.updateColumnValue("upid", upid);
		astUpdateBuilder.update();
		return upid;
	}
	
	public String findUpid(Dao<AssetEntity,String> entitydao,String userId,String organCode )throws SQLException{
		QueryBuilder<AssetEntity,String> queryBuilder=entitydao.queryBuilder();
		queryBuilder.where().eq("dt", "1").and().eq("userId", userId).and().eq("organCode", organCode).and().isNotNull("upid");
		
		AssetEntity t1=queryBuilder.queryForFirst();
		if(null==t1){
			return "";
		}else{
			return t1.getUpid();
		}
	}
	
	public void deleteAssetsWithUpid(Dao<AssetEntity,String>entitydao,String userid,String upid) throws SQLException{
		DeleteBuilder<AssetEntity,String> delBuilder=entitydao.deleteBuilder();
		delBuilder.where().eq("dt", "1").and().eq("upid", upid).and().eq("userId", userid).prepare();
		delBuilder.delete();
	}
	
	
	public List<AssetEntity> findPdAssets(Dao<AssetEntity,String> entitydao,String userId,String organCode)throws SQLException{
		Map<String,Object> fmap=new HashMap<String,Object>();
		fmap.put("userId", userId);
		fmap.put("dt", "1");
		fmap.put("organCode", organCode);
		return entitydao.queryForFieldValues(fmap);
	}
	
	public AssetEntity findAssetData(Dao<AssetEntity,String> entitydao,String assetCode){
		QueryBuilder<AssetEntity,String> queryBuilder=entitydao.queryBuilder();
		System.out.println("assetCode===>"+assetCode);
		try {
			AssetEntity rd=entitydao.queryForFirst(queryBuilder.where().eq("assetCode", assetCode).prepare());
			if(rd==null){
				rd=entitydao.queryForFirst(queryBuilder.where().eq("finCode", assetCode).prepare());
			}
			System.out.println("rd===>"+rd);
			return rd;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
			return null;
		}
	}
	
	public Page<AssetEntity> findAssetPage(Dao<AssetEntity,String> entitydao,Page page,Map<String,Object> paramMap)throws SQLException{
		Map<String,Object> fmap=new HashMap<String,Object>();
		QueryBuilder<AssetEntity,String> queryBuilder=entitydao.queryBuilder();
		QueryBuilder<AssetEntity,String> cBuilder=entitydao.queryBuilder();
		String category=(String)paramMap.get("category");//类别
		String assetName=(String)paramMap.get("assetName");//类别
		String storage=(String)paramMap.get("storage");//存放地点 
		String assetCode=(String)paramMap.get("assetCode");//盘点状态 
		String organCode=(String)paramMap.get("organCode");//盘点状态 
		String starNum1=(String)paramMap.get("starNum1");//盘点状态 
		String starNum2=(String)paramMap.get("starNum2");//盘点状态 
		String mgrOrganCode=(String)paramMap.get("mgrOrganCode");//盘点状态 
		String type=(String)paramMap.get("type");//盘点状态
		Where<AssetEntity, String> wheres = queryBuilder.where(); 
		Where<AssetEntity, String> cwheres = cBuilder.where(); 
		cBuilder.setCountOf(true); 
		wheres.isNotNull("assetCode");
		cwheres.isNotNull("assetCode");
		if(StringUtils.isNotBlank(assetCode)){
			wheres.and().eq("assetCode", "%"+assetCode+"%");
			cwheres.and().eq("assetCode", "%"+assetCode+"%");
		}
		if(StringUtils.isNotBlank(assetName)){
			wheres.and().like("assetName","%"+assetName+"%");
			cwheres.and().like("assetName","%"+assetName+"%");
		}
		if(StringUtils.isNotBlank(organCode)){
			wheres.and().eq("organCode", organCode);
			cwheres.and().eq("organCode", organCode);
		}
		if(StringUtils.isNotBlank(mgrOrganCode)){
			wheres.and().eq("mgrOrganCode", mgrOrganCode);
			cwheres.and().eq("mgrOrganCode", mgrOrganCode);
		}  
		if(StringUtils.isNotBlank(category)){
			wheres.and().eq("cateID", category);
			cwheres.and().eq("cateID", category);
		}  
		if(StringUtils.isNotBlank(starNum1)){
			if("none".equals(starNum1)){
				wheres.and().isNull("starNum");
				cwheres.and().isNull("starNum");
			}else{
				try{
					Double d=Double.parseDouble(starNum1);
					wheres.and().ge("starNum", d);
					cwheres.and().ge("starNum", d);
				}catch(Exception ex){
					ex.printStackTrace();
				} 
			}
			
		}  
		if(StringUtils.isNotBlank(starNum2)){
			if("none".equals(starNum1)){
				wheres.and().isNull("starNum");
				cwheres.and().isNull("starNum");
			}else{
				try{
					Double d=Double.parseDouble(starNum2);
					wheres.and().le("starNum", d);
					cwheres.and().le("starNum", d);
				}catch(Exception ex){
					ex.printStackTrace();
				} 
			} 
		}  
		page.setResult(queryBuilder.offset(page.getFirst()-1).limit(page.getPageSize()).query()); 
		page.setTotalCount( entitydao.countOf(cBuilder.prepare())); 
		System.out.println("rcount==>"+page.getTotalCount());
		return page;
	}

	public void savePdAsset(Dao<AssetEntity,String>entitydao,String userid, AssetEntity asset,String mgrOrganCode,String organCode,String addr,String simId)throws SQLException{
		asset.setUserId(userid);
		asset.setOrganCode(organCode);
		asset.setMgrOrganCode(mgrOrganCode);
		asset.setAddr(addr);
		asset.setSimId(simId);
		asset.setDt("1");
		asset.setPid(userid+"_"+asset.getAssetCode());// 手机库id
		entitydao.createOrUpdate(asset);
	}
	public void deleteQtyAssets(Dao<AssetEntity,String>entitydao) throws SQLException{
		DeleteBuilder<AssetEntity,String> delBuilder=entitydao.deleteBuilder();
		delBuilder.where().eq("dt", "2").prepare();
		delBuilder.delete();
	}
	
	
	public void saveQueryAsset(Dao<AssetEntity,String>entitydao,List<AssetEntity> assets,String userid)throws SQLException{
		
		for (AssetEntity asset : assets) {
			asset.setUserId(userid);
			asset.setAddr("");
			asset.setSimId("");
			asset.setDt("2");
			asset.setPid("qty2_"+asset.getAssetCode());// 手机库id
			entitydao.createOrUpdate(asset);
		} 
	} 

	public CfgEntity getCfg(Dao<CfgEntity,String>entitydao,String userid,String name)throws SQLException{
		return entitydao.queryForId(userid+"_"+name);
	}

	public void saveCfg(Dao<CfgEntity,String>entitydao,String userId,String name,String val)throws SQLException{
		CfgEntity cfg=new CfgEntity();
		cfg.setCfgId(userId+"_"+name);
		cfg.setCfgName(name);
		cfg.setUserId(userId);
		cfg.setVal(val);
		entitydao.createOrUpdate(cfg);

	}


}
