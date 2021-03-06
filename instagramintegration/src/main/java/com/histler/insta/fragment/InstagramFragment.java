package com.histler.insta.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.histler.insta.R;
import com.histler.insta.adapter.InstaFeedAdapter;
import com.histler.insta.adapter.viewholder.InstaFeedViewHolder;
import com.histler.insta.api.v2.InstaMedia;
import com.histler.insta.api.v2.node.InstaNode;
import com.histler.insta.api.v2.node.InstaUser;
import com.histler.insta.task.InstaRequest;
import com.histler.insta.task.InstaUserRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.UncachedSpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Set;

import ru.badr.base.BaseSpiceManager;
import ru.badr.base.fragment.RecyclerFragment;
import ru.badr.base.view.EndlessRecycleScrollListener;

/**
 * Created by ABadretdinov
 * 25.09.2015
 * 16:30
 */
public class InstagramFragment extends RecyclerFragment<InstaNode, InstaFeedViewHolder> implements RequestListener<InstaMedia> {
    public static final String INSTAGRAM_PACKAGE = "com.instagram.android";
    private SpiceManager mSpiceManager = new BaseSpiceManager(UncachedSpiceService.class);
    private String mNextMaxFeedId;
    private UserRequestListener mUserRequestsListener = new UserRequestListener();

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getRecyclerView().addOnScrollListener(new EndlessRecycleScrollListener() {
            @Override
            public void onLoadMore() {
                loadFeed();
            }
        });
    }

    private void loadFeed() {
        setRefreshing(true);

        mSpiceManager.execute(new InstaRequest("опенкон", mNextMaxFeedId), this);
    }

    @Override
    public void onRecyclerViewItemClick(View v, int position) {
        InstaNode instaFeed = getAdapter().getItem(position);
        Uri uri = Uri.parse("http://instagram.com/p/" + instaFeed.getCode());
        Intent instaIntent = new Intent(Intent.ACTION_VIEW, uri);

        instaIntent.setPackage(INSTAGRAM_PACKAGE);

        try {
            startActivity(instaIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    @Override
    public void onRefresh() {
        mNextMaxFeedId = null;
        loadFeed();
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        setRefreshing(false);
        showMessage(spiceException.getCause() != null ? spiceException.getCause().getMessage() : spiceException.getMessage(), getString(R.string.repeat), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFeed();
            }
        });
    }

    @Override
    public void onRequestSuccess(InstaMedia instaMedia) {
        setRefreshing(false);
        if (instaMedia != null) {
            if (mNextMaxFeedId == null || getAdapter() == null) {
                setAdapter(new InstaFeedAdapter(instaMedia.getNodes()));
                loadFirstUser();
            } else {
                boolean noMoreUsers = ((InstaFeedAdapter) getAdapter()).getWaitIds().isEmpty();
                ((InstaFeedAdapter) getAdapter()).addData(instaMedia.getNodes());
                if (noMoreUsers) {
                    loadFirstUser();
                }
            }
            mNextMaxFeedId = instaMedia.getPageInfo().getNextPageTag();
        }
    }

    private void loadFirstUser() {
        InstaFeedAdapter adapter = (InstaFeedAdapter) getAdapter();
        if (adapter != null) {
            Set<String> unsetUsersId = adapter.getWaitIds();
            if (!unsetUsersId.isEmpty()) {
                mSpiceManager.execute(new InstaUserRequest(unsetUsersId.iterator().next()), mUserRequestsListener);
            }
        }
    }

    private class UserRequestListener implements RequestListener<InstaUser> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            showMessage(spiceException.getCause() != null ? spiceException.getCause().getMessage() : spiceException.getMessage(), getString(R.string.repeat), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFirstUser();
                }
            });
        }

        @Override
        public void onRequestSuccess(InstaUser instaUser) {
            InstaFeedAdapter adapter = (InstaFeedAdapter) getAdapter();
            if (adapter != null) {
                adapter.updateUser(instaUser);
                loadFirstUser();
            }
        }
    }
}
