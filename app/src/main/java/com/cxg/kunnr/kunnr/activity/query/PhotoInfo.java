package com.cxg.kunnr.kunnr.activity.query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cxg.kunnr.kunnr.activity.application.XPPApplication;
import com.cxg.kunnr.kunnr.activity.entity.BasePhotoInfo;
import com.cxg.kunnr.kunnr.activity.provider.DataProviderFactory;
import com.j256.ormlite.stmt.QueryBuilder;

public class PhotoInfo extends BasePhotoInfo {
    private static final long serialVersionUID = 4438987701048629672L;

    public static PhotoInfo getByPhotoName(String photo) {
        try {
            List<PhotoInfo> result = OrmHelper.getInstance().getPhotoInfoDao()
                    .queryForEq("photoName", photo);
            if (result.size() != 0) {
                return result.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String getPhotoName(String photo) {
        try {
            List<PhotoInfo> result = OrmHelper.getInstance().getPhotoInfoDao()
                    .queryForEq("photoName", photo);
            if (result != null && result.size() != 0) {
                PhotoInfo info = result.get(0);
                if (XPPApplication.Status.NEW.equals(info.getStatus())) {
                    PhotoInfo.submitPhoto(info);
                }
                String s = info.getEmplid() + "_" + info.getCustid() + "_"
                        + info.getRouteid() + "_" + info.getActid() + "_"
                        + info.getTimestamp() + "_" + info.getPtype() + "_"
                        + info.getSeq();
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String getPhotoName(PhotoInfo info) {
        if (XPPApplication.Status.NEW.equals(info.getStatus())) {
            PhotoInfo.submitPhoto(info);
        }
        String s = info.getEmplid() + "_" + info.getCustid() + "_"
                + info.getRouteid() + "_" + info.getActid() + "_"
                + info.getTimestamp() + "_" + info.getPtype() + "_"
                + info.getSeq();
        return s;
    }

    public static boolean save(PhotoInfo pt) {
        try {
            OrmHelper.getInstance().getPhotoInfoDao().createOrUpdate(pt);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteAll(List<PhotoInfo> list) {
        try {
            OrmHelper.getInstance().getPhotoInfoDao().delete(list);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean submitPhoto(PhotoInfo photoInfo) {
        boolean flag = false;
        if (photoInfo.getStatus().equals(XPPApplication.Status.NEW)) {
            photoInfo.setStatus(XPPApplication.Status.UNSYNCHRONOUS);
            photoInfo.update();
            flag = true;
        }
        return flag;
    }

    public static List<PhotoInfo> findAll() {
        try {
            List<PhotoInfo> result = OrmHelper.getInstance().getPhotoInfoDao()
                    .queryBuilder().query();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<PhotoInfo> findByShop(String custId, XPPApplication.PhotoType ptype) {
        try {
            List<PhotoInfo> result = OrmHelper.getInstance().getPhotoInfoDao()
                    .queryBuilder().where().eq("custid", custId).and()
                    .eq("ptype", ptype).and()
                    .eq("dayType", DataProviderFactory.getDayType()).and()
                    .eq("emplid", DataProviderFactory.getUserId()).query();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<PhotoInfo> findByEmplid(XPPApplication.PhotoType ptype) {
        try {
            List<PhotoInfo> result = OrmHelper.getInstance().getPhotoInfoDao()
                    .queryBuilder().where().eq("ptype", ptype).and()
                    .eq("dayType", DataProviderFactory.getDayType()).and()
                    .eq("emplid", DataProviderFactory.getUserId()).query();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getRecordsCount(String custId, XPPApplication.PhotoType ptype) {
        try {
            List<PhotoInfo> result = OrmHelper.getInstance().getPhotoInfoDao()
                    .queryBuilder().where().eq("custid", custId).and()
                    .eq("ptype", ptype).and()
                    .eq("dayType", DataProviderFactory.getDayType()).and()
                    .eq("emplid", DataProviderFactory.getUserId()).and()
                    .ne("status", XPPApplication.Status.NEW).query();
            return result.size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int findByShop(String custId, List<XPPApplication.PhotoType> ptype) {
        try {
            List<PhotoInfo> result = OrmHelper.getInstance().getPhotoInfoDao()
                    .queryBuilder().where().eq("custid", custId).and()
                    .in("ptype", ptype).and().eq("status", XPPApplication.Status.NEW).and()
                    .eq("dayType", DataProviderFactory.getDayType()).and()
                    .eq("emplid", DataProviderFactory.getUserId()).query();

            for (PhotoInfo photoInfo : result) {
                photoInfo.setStatus(XPPApplication.Status.UNSYNCHRONOUS);
                photoInfo.update();
            }
            return result.size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<PhotoInfo> findAllPhotoByCustId(String custId) {
        try {
            List<PhotoInfo> result = OrmHelper.getInstance().getPhotoInfoDao()
                    .queryBuilder().where().eq("custid", custId).and()
                    .eq("dayType", DataProviderFactory.getDayType()).and()
                    .eq("emplid", DataProviderFactory.getUserId()).query();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<PhotoInfo> findInId(String photoName) {
        try {
            List<String> list = new ArrayList<String>();
            if (photoName != null) {
                String[] str = photoName.split(",");
                list = Arrays.asList(str);
            }
            List<PhotoInfo> result = OrmHelper.getInstance().getPhotoInfoDao()
                    .queryBuilder().where().in("photoName", list).query();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<PhotoInfo> synchronousPhoto(String custId) {
        try {
            QueryBuilder<PhotoInfo, Integer> qb = OrmHelper.getInstance()
                    .getPhotoInfoDao().queryBuilder();
            if (custId != null) {
                qb.where().eq("status", XPPApplication.Status.UNSYNCHRONOUS).and()
                        .eq("dayType", DataProviderFactory.getDayType()).and()
                        .eq("emplid", DataProviderFactory.getUserId()).and()
                        .eq("custid", custId);
            } else {
                qb.where().eq("status", XPPApplication.Status.UNSYNCHRONOUS).and()
                        .eq("dayType", DataProviderFactory.getDayType()).and()
                        .eq("emplid", DataProviderFactory.getUserId());
            }
            List<PhotoInfo> result = OrmHelper.getInstance().getPhotoInfoDao()
                    .query(qb.prepare());
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean update() {
        try {
            return OrmHelper.getInstance().getPhotoInfoDao().update(this) > -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(PhotoInfo photoInfo) {
        try {
            OrmHelper.getInstance().getPhotoInfoDao().delete(photoInfo);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
