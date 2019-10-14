package com.dizan.mlicxapp.views;

import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public GridSpaceItemDecoration (int space, RecyclerView parent) {
        mSpace = space;
        getRecycleViewOffsets(parent);
    }

    /**
     *
     * @param outRect Item矩形边界
     * @param view ItemView
     * @param parent RecyclerView
     * @param state RecyclerView的状态
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = mSpace;


    }

    private void getRecycleViewOffsets(RecyclerView parent) {
        /**
         * View margin
         * margin 为 正 ， 则View 会距离边界产生一个距离
         * margin 为 负 ， 则View 会超出边界产生一个距离
         */
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) parent.getLayoutParams();
        layoutParams.leftMargin = -mSpace;
        parent.setLayoutParams(layoutParams);
    }
}
