package com.example.administrator.test.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import com.example.administrator.test.R;
import com.example.administrator.test.databinding.ItemInternetBinding;
import com.example.administrator.test.utils.StaticBaseInfo;

/**
 * Create by zmm
 * Time 2019/5/23
 * PackageName com.example.administrator.test.adapter
 */
public abstract class ItemInernetAdapter extends RecyclerView.Adapter<ItemInernetAdapter.ViewHolder> {
    private List<String> list;
    private Context context;
    private int selectedItem = 0;

    public void setSelectedItem(int selectedItem) {
        if(this.selectedItem != selectedItem){
            int i = this.selectedItem;
            this.selectedItem = selectedItem;
            notifyItemChanged(i);
            notifyItemChanged(this.selectedItem);
        }
    }

    public ItemInernetAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_internet,null,false);
                ViewHolder holder = new ViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oItemClick(holder.getAdapterPosition());
                    }
                });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setContent(list.get(position));
        holder.binding.setIsLight(StaticBaseInfo.isLight(context));
        holder.binding.setIsSelected(position == selectedItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemInternetBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
    protected abstract void oItemClick(int position);
}
