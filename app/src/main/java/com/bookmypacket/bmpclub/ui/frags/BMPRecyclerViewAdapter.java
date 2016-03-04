package com.bookmypacket.bmpclub.ui.frags;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.BMPPacket;

import java.util.List;

/**
 * Created by Manish on 24-01-2016.
 */
public abstract class BMPRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    protected final OnListFragmentInteractionListener<BMPPacket> mListener;
    protected final List<BMPPacket>                              mValues;

    public BMPRecyclerViewAdapter(
            OnListFragmentInteractionListener mListener, List<BMPPacket> mValues)
    {
        this.mListener = mListener;
        this.mValues = mValues;
    }

    @Override
    public int getItemCount()
    {
        return mValues.size() > 0 ? mValues.size() : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder view = null;
        if (mValues.size() == 0)
        {
            view = new EmptyViewHolder(LayoutInflater.from(parent.getContext())
                                               .inflate(R.layout.empty_list_layout, parent, false));
        } else
        {
            view = customViewHolder(parent, viewType);
        }
        return view;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position,
                                 List<Object> payloads)
    {
        if (holder instanceof EmptyViewHolder)
        {

        } else
        {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)
    {

        if (mValues.size() > 0)
        {
            customBindViewHolder(holder, position);
        } else if (holder instanceof EmptyViewHolder)
        {
            EmptyViewHolder h = (EmptyViewHolder) holder;
            h.mIdView.setText(emptyString());
        }
    }

    abstract public void customBindViewHolder(final RecyclerView.ViewHolder holder, int
            position);

    abstract public RecyclerView.ViewHolder customViewHolder(ViewGroup parent, int viewType);

    abstract protected int emptyString();

    public class EmptyViewHolder extends RecyclerView.ViewHolder
    {
        public final View     mView;
        public final TextView mIdView;

        public EmptyViewHolder(View view)
        {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.empty_text);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}
