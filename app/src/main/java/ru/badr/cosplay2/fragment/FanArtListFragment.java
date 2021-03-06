package ru.badr.cosplay2.fragment;

import android.os.Bundle;
import android.view.View;

import com.badr.cosplay2.model.cards.Card;
import com.badr.cosplay2.model.cards.list.ListCard;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.UncachedSpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import ru.badr.base.BaseSpiceManager;
import ru.badr.base.fragment.RecyclerFragment;
import ru.badr.base.util.Navigate;
import ru.badr.cosplay2.adapter.FestPhotoCardAdapter;
import ru.badr.cosplay2.adapter.viewholder.FestPhotoCardViewHolder;
import ru.badr.cosplay2.task.TaggedCardsLoadRequest;
import ru.badr.opencon.R;

/**
 * Created by ABadretdinov
 * 19.10.2015
 * 18:28
 */
public class FanArtListFragment extends RecyclerFragment<ListCard, FestPhotoCardViewHolder> implements RequestListener<ListCard.List> {
    private SpiceManager mSpiceManager = new BaseSpiceManager(UncachedSpiceService.class);

    private String mTag;
    private String mTitle;

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title.toString();
    }


    @Override
    public void onStart() {
        if (!mSpiceManager.isStarted()) {
            mSpiceManager.start(getActivity().getApplicationContext());
            onRefresh();
        }
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if (mSpiceManager.isStarted()) {
            mSpiceManager.shouldStop();
        }
        super.onDestroy();
    }

    @Override
    public boolean isNeedDivider() {
        return false;
    }

    @Override
    public void onRefresh() {
        setRefreshing(true);
        mSpiceManager.execute(new TaggedCardsLoadRequest(getActivity(), mTag), this);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        setRefreshing(false);
        setAdapter(new FestPhotoCardAdapter(null));
        showMessage(spiceException.getCause() != null ? spiceException.getCause().getMessage() : spiceException.getMessage(), getString(R.string.repeat), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
    }

    @Override
    public void onRequestSuccess(ListCard.List cards) {
        setRefreshing(false);
        setAdapter(new FestPhotoCardAdapter(cards));
    }

    @Override
    public void onRecyclerViewItemClick(View v, int position) {
        Card card = getAdapter().getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString(FestCardInfoFragment.TITLE, mTitle);
        bundle.putSerializable(FestCardInfoFragment.CARD, card);
        Navigate.to(getActivity(), FestCardInfoFragment.class, bundle, false);
    }
}
