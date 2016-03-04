package com.bookmypacket.bmpclub.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseExpandableListAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Manish on 23-11-2015.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter
        implements AnimationListener
{
    private Animation                 animation;
    private List<String>              listDataHeaders;
    private Context                   context;
    private Map<String, List<String>> childData;

    public ExpandableAdapter(List<String> listDataHeaders, Context context,
                             Map<String, List<String>> childData)
    {
        this.listDataHeaders = listDataHeaders;
        this.context = context;
        this.childData = childData;
    }

    @Override
    public int getGroupCount()
    {
        return listDataHeaders.size();
    }

    @Override
    public int getChildrenCount(int i)
    {
        return childData.get(listDataHeaders.get(i)).size();
    }

    @Override
    public Object getGroup(int i)
    {
        return listDataHeaders.get(i);
    }

    @Override
    public Object getChild(int i, int i1)
    {
        return childData.get(listDataHeaders.get(i1));
    }

    @Override
    public long getGroupId(int i)
    {
        return i;
    }

    @Override
    public long getChildId(int i, int i1)
    {
        return i1;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup)
    {
        return null;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup)
    {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1)
    {
        return false;
    }

    @Override
    public void onAnimationStart(Animation animation)
    {

    }

    @Override
    public void onAnimationEnd(Animation animation)
    {

    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }
}
