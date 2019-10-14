package com.dizan.mlicxapp.models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class OnliveSourceModel extends RealmObject {
    private RealmList<AlbumModel> album;
    private RealmList<MusicModel> hot;

    public RealmList<AlbumModel> getAlbum() {
        return album;
    }

    public void setAlbum(RealmList<AlbumModel> album) {
        this.album = album;
    }

    public RealmList<MusicModel> getHot() {
        return hot;
    }

    public void setHost(RealmList<MusicModel> hot) {
        this.hot = hot;
    }
}
