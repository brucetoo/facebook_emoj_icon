/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rockerhieu.emojicon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.rockerhieu.emojicon.emoji.Emojicon;

/**
 * @author Daniele Ricci
 */
public class EmojiconRecentsGridFragment extends EmojiconGridFragment implements EmojiconRecents {
    private EmojiAdapter mAdapter;
    private boolean mUseSystemDefault = false;
//    private List<GridView> views;
//    private EmojiSingleAdapter mSingleAdapter;
    private static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";

    protected static EmojiconRecentsGridFragment newInstance() {
        return newInstance(false);
    }

    protected static EmojiconRecentsGridFragment newInstance(boolean useSystemDefault) {
        EmojiconRecentsGridFragment fragment = new EmojiconRecentsGridFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(USE_SYSTEM_DEFAULT_KEY, useSystemDefault);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUseSystemDefault = getArguments().getBoolean(USE_SYSTEM_DEFAULT_KEY);
        } else {
            mUseSystemDefault = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.emojicon_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        EmojiconRecentsManager recents = EmojiconRecentsManager
            .getInstance(view.getContext());
//        if(recents != null) {
//
//
//            int pageCount = (int) Math.ceil(recents.size() / 28.0f);
//            //  List<Emojicon> item = Arrays.asList(recents);
//            views = new ArrayList<GridView>();
//            mSingleAdapter = new EmojiSingleAdapter(views);
//            mViewPager.setAdapter(mSingleAdapter);
//            mIndicator.setViewPager(mViewPager);
//            for (int i = 0; i < pageCount; i++) {
//                GridView gv = new GridView(getActivity());
//                gv.setGravity(Gravity.CENTER);
//                gv.setClickable(true);
//                gv.setFocusable(true);
//                 gv.setNumColumns(7);
//                if (i == pageCount - 1) {
//                    mAdapter = new EmojiAdapter(getActivity(), recents.subList(i * 28, recents.size() - 1));
//                    gv.setAdapter(mAdapter);
//                } else {
//                    mAdapter = new EmojiAdapter(getActivity(), recents.subList(i * 28, (i + 1) * 28));
//                    gv.setAdapter(mAdapter);
//                }
//                gv.setOnItemClickListener(this);
//                views.add(gv);
//            }
//        }
        mAdapter = new EmojiAdapter(view.getContext(), recents, mUseSystemDefault);
        GridView gridView = (GridView) view.findViewById(R.id.Emoji_GridView);
        gridView.setAdapter(mAdapter);
        gridView.setSelector(new ColorDrawable(Color.GRAY));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter = null;
    }

    @Override
    public void addRecentEmoji(Context context, Emojicon emojicon) {
        EmojiconRecentsManager recents = EmojiconRecentsManager
            .getInstance(context);
        recents.push(emojicon);

        // notify dataset changed
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

}
