package com.example.administrator.test.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.administrator.test.R;
import com.example.administrator.test.databinding.ItemGeDanBinding;
import com.example.administrator.test.entity.GeDanInfo;
import com.example.administrator.test.event.GeDan;
import com.example.administrator.test.utils.Res;
import com.example.administrator.test.utils.ScreenUtils;

import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/24
 * PackageName com.example.administrator.test.adapter
 */
public class GeDanAdapter extends RecyclerView.Adapter<GeDanAdapter.ViewHoler> {
    private Context context;
    private List<GeDanInfo> list;
    private int x;
    private int divider;
    private RelativeLayout.LayoutParams layoutParams;

    public GeDanAdapter(Context context, List<GeDanInfo> list) {
        this.context = context;
        this.list = list;
        divider = (int) Res.getDimen(R.dimen.x10, context);
        x = ((ScreenUtils.getScreenWidth(context) - (int)Res.getDimen(R.dimen.x10, context) * 5) / 4);
        layoutParams = new RelativeLayout.LayoutParams(x, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ge_dan, null, false);
        ViewHoler holer = new ViewHoler(view);
        return holer;
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        holder.binding.setGeDan(list.get(position));
        if (position != 0) {
            layoutParams.setMargins(divider, 0, 0, 0);
        } else {
            layoutParams.setMargins(0, 0, 0, 0);
        }
        holder.binding.getRoot().setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHoler extends RecyclerView.ViewHolder {
        ItemGeDanBinding binding;

        public ViewHoler(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
