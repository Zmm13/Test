package com.example.administrator.test.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.test.MainActivity;
import com.example.administrator.test.R;

import java.util.List;

import com.example.administrator.test.daoJavaBean.Song;
import com.example.administrator.test.databinding.ItemMusicBinding;
import com.example.administrator.test.singleton.MediaPlayerUtils;
import com.example.administrator.test.singleton.MusicListTool;
import com.example.administrator.test.utils.StaticBaseInfo;

/**
 * Create by zmm
 * Time 2019/5/6
 * PackageName com.example.administrator.test.adapter
 */
public abstract class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private Context context;
    private List<Song> list;
//    private int playPosition = -1;
    public MusicListAdapter( Context context,List<Song> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MusicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_music,null,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p = viewHolder.getAdapterPosition();
                if(p >= 0 && p< list.size()){
                    onItemClick(list.get(p));
//                    notifyItemChanged(p);
                }
//               if(playPosition  >= 0 && playPosition< list.size()){
//                   notifyItemChanged(playPosition);
//               }
//               playPosition = p;

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MusicListAdapter.ViewHolder holder, int position) {
             holder.binding.setSong(list.get(position));
             holder.binding.setIsLight(StaticBaseInfo.isLight(context));
             holder.binding.setIsPlay(MusicListTool.getInstance().playSong == null ? false : (MusicListTool.getInstance().playSong.getPath().equals( list.get(position).getPath())));
//             holder.binding.setIsPlay(position == playPosition);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemMusicBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    protected abstract void onItemClick(Song song);
}
