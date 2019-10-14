package com.dizan.mlicxapp.activitys;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dizan.mlicxapp.R;
import com.dizan.mlicxapp.adapters.OnliveGridAdapter;
import com.dizan.mlicxapp.adapters.OnliveListAdapter;
import com.dizan.mlicxapp.helps.RealmHelp;
import com.dizan.mlicxapp.models.OnliveSourceModel;
import com.dizan.mlicxapp.views.GridSpaceItemDecoration;
import com.dizan.mlicxapp.views.ImageBannerViewGroup;

public class MainActivity extends BaseActivity {

    private RecyclerView mRvGrid, mRvList;
    private OnliveGridAdapter mGridAdapter;
    private OnliveListAdapter mListAdapter;
    private RealmHelp mRealmHerlper;
    private OnliveSourceModel mOnliveSourceModel;

    private ImageBannerViewGroup mGroup;
    private int[] ids = new int[] {
      R.mipmap.banner,
            R.mipmap.banner1,
            R.mipmap.banner2
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();  // 初始化数据
        initView();  // 初始化
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mRealmHerlper = new RealmHelp();
        mOnliveSourceModel = mRealmHerlper.getOnliveSource();
    }

    /**
     * 初始化View
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

        mGroup = fd(R.id.image_group);
        //mGroup.setNestedScrollingEnabled(false);
        for(int i = 0; i < ids.length;i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(ids[i]);
            mGroup.addView(iv);
        }

        initNavBar(false,"美丽畅想", true);

        mRvGrid = fd(R.id.rv_grid);
        mRvGrid.setLayoutManager(new GridLayoutManager(this, 3));
        mRvGrid.addItemDecoration(new GridSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.albumMarginSize), mRvGrid));
        mRvGrid.setNestedScrollingEnabled(false);
        mGridAdapter = new OnliveGridAdapter(this, mOnliveSourceModel.getAlbum());
        mRvGrid.setAdapter(mGridAdapter);

        mRvList = fd(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvList.setNestedScrollingEnabled(false);
        mListAdapter = new OnliveListAdapter(this, mRvList, mOnliveSourceModel.getHot());
        mRvList.setAdapter(mListAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealmHerlper.close();
    }
}
