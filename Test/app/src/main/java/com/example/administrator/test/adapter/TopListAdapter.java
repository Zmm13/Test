package com.example.administrator.test.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.test.R;
import com.example.administrator.test.databinding.ItemGeDanBinding;
import com.example.administrator.test.databinding.ItemTopListBinding;
import com.example.administrator.test.entity.GeDanInfo;
import com.example.administrator.test.entity.QQTopListInfo;
import com.example.administrator.test.utils.Res;
import com.example.administrator.test.utils.ScreenUtils;
import com.example.administrator.test.utils.StaticBaseInfo;

import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/24
 * PackageName com.example.administrator.test.adapter
 */
public abstract class TopListAdapter extends RecyclerView.Adapter<TopListAdapter.ViewHoler> {
    private Context context;
    private List<QQTopListInfo> list;
    private int x;
    private int divider;
    private RelativeLayout.LayoutParams layoutParams;
    private LinearLayout.LayoutParams layoutParams2;

    public TopListAdapter(Context context, List<QQTopListInfo> list, RecyclerView recyclerView) {
        this.context = context;
        this.list = list;
        divider = (int) Res.getDimen(R.dimen.x10, context);
        x = ((ScreenUtils.getScreenWidth(context) - (int)Res.getDimen(R.dimen.x10, context) * 5) / 4);
        layoutParams = new RelativeLayout.LayoutParams(x, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2 = new LinearLayout.LayoutParams(x, x);
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
//        layoutParams.height = (int) (x + Res.getDimen(R.dimen.x16,context));
//        recyclerView.setLayoutParams(layoutParams);
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_top_list, null, false);
        ViewHoler holer = new ViewHoler(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(holer.getAdapterPosition());
            }
        });
        return holer;
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        holder.binding.setIsLight(StaticBaseInfo.isLight(context));
        holder.binding.setInfo(list.get(position));
        if (position != 0) {
            layoutParams.setMargins(divider, 0, 0, 0);
        } else {
            layoutParams.setMargins(0, 0, 0, 0);
        }
        holder.binding.getRoot().setLayoutParams(layoutParams);
        holder.binding.ivPic.setLayoutParams(layoutParams2);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHoler extends RecyclerView.ViewHolder {
        ItemTopListBinding binding;

        public ViewHoler(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    protected abstract void onItemClick(int position);
}
