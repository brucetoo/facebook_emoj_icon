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

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.rockerhieu.emojicon.emoji.People;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public class EmojiconGridFragment extends Fragment implements AdapterView.OnItemClickListener {
    private OnEmojiconClickedListener mOnEmojiconClickedListener;
    private EmojiconRecents mRecents;
    private Emojicon[] mData;
    private boolean mUseSystemDefault = false;
    protected ViewPager mViewPager;
    protected CirclePageIndicator mIndicator;
    private static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";
    private List<GridView> views;

    protected static EmojiconGridFragment newInstance(Emojicon[] emojicons, EmojiconRecents recents) {
        return newInstance(emojicons, recents, false);
    }

    protected static EmojiconGridFragment newInstance(Emojicon[] emojicons, EmojiconRecents recents, boolean useSystemDefault) {
        EmojiconGridFragment emojiGridFragment = new EmojiconGridFragment();
        Bundle args = new Bundle();
        args.putSerializable("emojicons", emojicons);
        args.putBoolean(USE_SYSTEM_DEFAULT_KEY, useSystemDefault);
        emojiGridFragment.setArguments(args);
        emojiGridFragment.setRecents(recents);
        return emojiGridFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emoji_item_fragment, null);
        mViewPager = (ViewPager) view.findViewById(R.id.item_viewpager);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.page_indicator);
//        return inflater.inflate(R.layout.emojicon_grid, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            mData = People.DATA;
            mUseSystemDefault = false;
        } else {
            Object[] o = (Object[]) getArguments().getSerializable("emojicons");
            mData = Arrays.asList(o).toArray(new Emojicon[o.length]);
            mUseSystemDefault = bundle.getBoolean(USE_SYSTEM_DEFAULT_KEY);
        }
        initPage();
        mViewPager.setAdapter(new EmojiSingleAdapter(views));
        mIndicator.setViewPager(mViewPager);
    /*    GridView gridView = (GridView) view.findViewById(R.id.Emoji_GridView);
        Bundle bundle = getArguments();
        if (bundle == null) {
            mData = People.DATA;
            mUseSystemDefault = false;
        } else {
            Object[] o = (Object[]) getArguments().getSerializable("emojicons");
            mData = Arrays.asList(o).toArray(new Emojicon[o.length]);
            mUseSystemDefault = bundle.getBoolean(USE_SYSTEM_DEFAULT_KEY);
        }
        gridView.setAdapter(new EmojiAdapter(view.getContext(), mData, mUseSystemDefault));
        gridView.setOnItemClickListener(this);*/
    }

    private void initPage() {
        int pageCount = (int) Math.ceil(mData.length / 28.0f);
        List<Emojicon> item = Arrays.asList(mData);
        views = new ArrayList<GridView>();
        for (int i = 0; i < pageCount; i++) {
            GridView gv = new GridView(getActivity());
            gv.setGravity(Gravity.CENTER);
            gv.setClickable(true);
            gv.setFocusable(true);
            gv.setNumColumns(7);
            if (i == pageCount - 1) {
                gv.setAdapter(new EmojiAdapter(getActivity(), item.subList(i*28, item.size()-1),mUseSystemDefault));
            } else
                gv.setAdapter(new EmojiAdapter(getActivity(), item.subList(i*28, (i+1) * 28),mUseSystemDefault));
            gv.setOnItemClickListener(this);
            gv.setSelector(new ColorDrawable(Color.GRAY));
            views.add(gv);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("emojicons", mData);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnEmojiconClickedListener) {
            mOnEmojiconClickedListener = (OnEmojiconClickedListener) activity;
        } else if (getParentFragment() instanceof OnEmojiconClickedListener) {
            mOnEmojiconClickedListener = (OnEmojiconClickedListener) getParentFragment();
        } else {
            throw new IllegalArgumentException(activity + " must implement interface " + OnEmojiconClickedListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        mOnEmojiconClickedListener = null;
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnEmojiconClickedListener != null) {
            mOnEmojiconClickedListener.onEmojiconClicked((Emojicon) parent.getItemAtPosition(position));
        }
        if (mRecents != null) {
            mRecents.addRecentEmoji(view.getContext(), ((Emojicon) parent
                    .getItemAtPosition(position)));
        }
    }

    private void setRecents(EmojiconRecents recents) {
        mRecents = recents;
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
    }
}
