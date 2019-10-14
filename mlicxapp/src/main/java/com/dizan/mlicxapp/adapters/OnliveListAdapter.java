package com.dizan.mlicxapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dizan.mlicxapp.R;
import com.dizan.mlicxapp.models.MusicModel;

import java.util.List;


public class OnliveListAdapter extends RecyclerView.Adapter<OnliveListAdapter.ViewHolder> {

    private Context mContext;
    private View mItemView;
    private RecyclerView mRv;
    private boolean isCalcaulationRyHeight;
    private List<MusicModel> mDataSource;

    public OnliveListAdapter (Context context, RecyclerView recyclerView, List<MusicModel> dataSource) {

        mContext = context;
        mRv = recyclerView;
        this.mDataSource = dataSource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mItemView = LayoutInflater.from(mContext).inflate(R.layout.item_list_onlive, viewGroup, false);
        return new ViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        setRecyclerViewHeight();

        MusicModel musicModel = mDataSource.get(i);


        Glide.with(mContext)
                .load(musicModel.getPoster())
                .into(viewHolder.ivIcon);

        viewHolder.tvName.setText(musicModel.getName());
        viewHolder.tvAuthor.setText(musicModel.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    /**
     * 1、获取ItemView的高度
     * 2、ItemView的数量
     * 3、使用itemViewHeight * itemViewNum = RecyclerView 的高度
     */
    private void setRecyclerViewHeight () {

        if(isCalcaulationRyHeight || mRv == null) return;

        isCalcaulationRyHeight = true;

        //  获取ItemView的高度
        RecyclerView.LayoutParams itemViewLP = (RecyclerView.LayoutParams) mItemView.getLayoutParams();
        //  itemView 的数量
        int itemCount = getItemCount();
        //  使用itemViewHeight * itemViewNum = RecyclerView 的高度
        int recyclerViewHeight = itemViewLP.height * itemCount;
        // 设置RecyclerView的高度
        LinearLayout.LayoutParams rvLP = (LinearLayout.LayoutParams) mRv.getLayoutParams();
        rvLP.height = recyclerViewHeight;
        mRv.setLayoutParams(rvLP);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        ImageView ivIcon;
        TextView tvName, tvAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAuthor = itemView.findViewById(R.id.tv_author);
        }
    }
}
