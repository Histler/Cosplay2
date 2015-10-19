package ru.badr.cosplay2.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.badr.base.adapter.OnItemClickListener;
import ru.badr.base.adapter.viewholder.BaseViewHolder;
import ru.badr.cosplay2.R;

/**
 * Created by ABadretdinov
 * 19.10.2015
 * 16:20
 */
public class FestPhotoCardViewHolder extends BaseViewHolder {
    public ImageView photo;
    public TextView title;

    public FestPhotoCardViewHolder(View itemView, OnItemClickListener clickListener) {
        super(itemView, clickListener);
    }

    @Override
    protected void initView(View itemView) {
        photo = (ImageView) itemView.findViewById(R.id.image);
        title = (TextView) itemView.findViewById(R.id.title);
    }
}
