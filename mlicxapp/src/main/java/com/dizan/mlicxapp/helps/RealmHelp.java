package com.dizan.mlicxapp.helps;

import android.content.Context;

import com.dizan.mlicxapp.migration.Migration;
import com.dizan.mlicxapp.models.AlbumModel;
import com.dizan.mlicxapp.models.MusicModel;
import com.dizan.mlicxapp.models.OnliveSourceModel;
import com.dizan.mlicxapp.models.UserModel;
import com.dizan.mlicxapp.utils.DataUtils;

import java.io.FileNotFoundException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmHelp {

    private Realm mRealm;

    public RealmHelp() {
        mRealm = Realm.getDefaultInstance();
    }

    /**
     * Realm数据库发生结构性变化（模型或者模型中的字段发生了新增、修改、删除）的时候，我们就需要对数据库进行迁移
     */

    /**
     * 告诉Realm数据需要迁移，并且为Realm设置最新的配置
     */
    public static void migration() {
        RealmConfiguration configuration = getRealmConf();

        // 为Realm设置最新的配置
        Realm.setDefaultConfiguration(configuration);
        // 告诉Realm数据需要迁移
        try {
            Realm.migrateRealm(configuration);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回 RealmConfiguration
     * @return
     */
    private static RealmConfiguration getRealmConf () {
        return new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new Migration())
                .build();
    }

    /**
     * 关闭数据库
     */
    public void close(){
        if(mRealm != null && !mRealm.isClosed()){
            mRealm.close();
        }
    }

    /**
     * 保存用户信息
     */
    public void saveUser(UserModel userModel) {
        mRealm.beginTransaction();
        mRealm.insert(userModel);
        //mRealm.insertOrUpdate(userModel);
        mRealm.commitTransaction();
    }

    /**
     * 返回所有用户
     */
    public List<UserModel> getAllUser() {
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        RealmResults<UserModel> results = query.findAll();
        return results;
    }

    /**
     * 验证用户信息
     */
    public boolean validateUser(String phone, String password) {
        boolean result = false;
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        query.equalTo("phone", phone)
                .equalTo("password", password);
        UserModel userModel = query.findFirst();

        if(userModel != null) {
            result =  true;
        }
        return  result;
    }

    /**
     * 获取当前用户
     */
    public UserModel getUser() {
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        UserModel userModel = query.equalTo("phone", UserHelper.getInstance().getPhone()).findFirst();
        return userModel;
    }

    /**
     * 修改密码
     */
    public void changePassword(String password) {
       UserModel userModel = getUser();
       mRealm.beginTransaction();
       userModel.setPassword(password);
       mRealm.commitTransaction();
    }


    /**
     * 1、用户登录，保存源数据
     */
    public void setOnliveSource(Context context) {
        // 拿到资源文件中的数据
        String onliveSourceJson = DataUtils.getJsonFromAssets(context,"DataSource.json");
        mRealm.beginTransaction();
        mRealm.createObjectFromJson(OnliveSourceModel.class, onliveSourceJson);
        mRealm.commitTransaction();
    }

    /**
     * 2、用户退出，删除源数据
     *      1、RealmResult delete
     *      2、Realm delete 删除这个模型下所有的数据
     */
    public void removeOnliveSource () {
        mRealm.beginTransaction();
        mRealm.delete(OnliveSourceModel.class);
        mRealm.delete(MusicModel.class);
        mRealm.delete(AlbumModel.class);
        mRealm.commitTransaction();
    }

    /**
     * 返回源数据
     */
    public OnliveSourceModel getOnliveSource () {
        return mRealm.where(OnliveSourceModel.class).findFirst();
    }

    /**
     * 返回
     */
    public  AlbumModel getAlbum (String albumId) {
        return mRealm.where(AlbumModel.class).equalTo("albumId", albumId).findFirst();
    }
}
