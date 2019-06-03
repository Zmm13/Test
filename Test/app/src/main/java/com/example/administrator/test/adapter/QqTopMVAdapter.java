package com.example.administrator.test.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.test.R;
import com.example.administrator.test.databinding.ItemQqTop100MusicBinding;
import com.example.administrator.test.databinding.ItemTopMvBinding;
import com.example.administrator.test.entity.MVInfo;
import com.example.administrator.test.entity.QQMusic;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.ItemAnimationTool;
import com.example.administrator.test.utils.StaticBaseInfo;

import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/6
 * PackageName com.example.administrator.test.adapter
 */
public abstract class QqTopMVAdapter extends RecyclerView.Adapter<QqTopMVAdapter.ViewHolder>{
    private Context context;
    private List<MVInfo> list;
    private int firstPosition = -1;

    public void setFirstPosition(int firstPosition) {
        this.firstPosition = firstPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    private int lastPosition = -1;
    //    private int playPosition = -1;
    public QqTopMVAdapter(Context context, List<MVInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public QqTopMVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_top_mv, null, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p = viewHolder.getAdapterPosition();
                if (p >= 0 && p < list.size()) {
                    onItemClick(list.get(p), p);
                }

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QqTopMVAdapter.ViewHolder holder, int position) {
        holder.binding.setMv(list.get(position));
        holder.binding.setIsLight(StaticBaseInfo.isLight(context));
        holder.binding.setIsPlay(false);
        if (position > lastPosition&&lastPosition!=-1) {//这里就是动画的应用
            ItemAnimationTool.loadAnimationUp(holder.binding.getRoot(),context);
        }else if(position < firstPosition&&lastPosition!=-1){
            ItemAnimationTool.loadAnimationDown(holder.binding.getRoot(),context);
        }
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemTopMvBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    protected abstract void onItemClick(MVInfo song, int p);

}
