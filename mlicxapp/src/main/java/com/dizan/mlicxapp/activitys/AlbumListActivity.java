package com.dizan.mlicxapp.activitys;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dizan.mlicxapp.R;
import com.dizan.mlicxapp.adapters.OnliveListAdapter;
import com.dizan.mlicxapp.helps.RealmHelp;
import com.dizan.mlicxapp.models.AlbumModel;

public class AlbumListActivity extends BaseActivity {

    public static final String ALBUM_ID = "albumId";
    private RecyclerView mRvList;
    private OnliveListAdapter mAdapter;
    private String mAlbumId;
    private RealmHelp mRealmHelper;
    private AlbumModel mAlbumModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        //  隐藏statusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        initData();
        initView();
    }

    private void initData() {
        mAlbumId = getIntent().getStringExtra(ALBUM_ID);
        mRealmHelper = new RealmHelp();
        mAlbumModel = mRealmHelper.getAlbum(mAlbumId);
    }


    private void initView() {
            initNavBar(true, "直播间", false);

            mRvList = fd(R.id.rv_list);
            mRvList.setLayoutManager(new LinearLayoutManager(this));
            mRvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mAdapter = new OnliveListAdapter(this, null, mAlbumModel.getList());
            mRvList.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealmHelper.close();
    }
}
