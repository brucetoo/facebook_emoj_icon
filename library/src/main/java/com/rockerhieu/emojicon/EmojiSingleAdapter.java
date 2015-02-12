package com.rockerhieu.emojicon;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Number    N1007
 * Creator   tuyong
 * On        2015/2/11
 * time      16:41
 */
public class EmojiSingleAdapter extends PagerAdapter {

    private List<GridView> data;
    public EmojiSingleAdapter(List<GridView> mData) {
       this.data = mData;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(data.get(position));
        return data.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView(data.get(position));
    }
}
