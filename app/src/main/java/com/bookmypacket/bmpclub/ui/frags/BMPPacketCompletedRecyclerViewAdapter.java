package com.bookmypacket.bmpclub.ui.frags;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.BMPPacket;

import java.util.List;

import static android.view.View.GONE;
import static com.bookmypacket.bmpclub.utils.AppConstants.RS_SYMBOL;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BMPPacket} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BMPPacketCompletedRecyclerViewAdapter extends BMPRecyclerViewAdapter
{


    public BMPPacketCompletedRecyclerViewAdapter(List<BMPPacket> items,
                                                 OnListFragmentInteractionListener listener)
    {
        super(listener, items);
    }


    @Override
    public RecyclerView.ViewHolder customViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bmppacketcompleted, parent, false);
        return new CompletedViewHolder(view);
    }

    @Override
    protected int emptyString()
    {
        return R.string.empty_bmp_packets;
    }

    @Override
    public void customBindViewHolder(final RecyclerView.ViewHolder h, int position)
    {
        if (mValues.size() > 0)
        {
            final CompletedViewHolder holder = (CompletedViewHolder) h;
            holder.mItem = mValues.get(position);
            final BMPPacket packet = mValues.get(position);
            holder.mIdView.setText(RS_SYMBOL + packet.getEarnings());
            holder.mSizeView.setText(packet.getLength() + " X " + packet.getWidth() + " X " + packet
                    .getHeight());
            holder.mDelivery.setText(packet.getDeliveryAddress() + " " + packet.getToCityPin());
            holder.mWeightView.setText(holder.mItem.getWeight());
            if (packet.isCod())
            {
                holder.mRootView.setBackgroundResource(R.color.colorCOD);
            }
            holder.mAccept.setVisibility(GONE);

            holder.mRootView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            });
        }
    }

    public class CompletedViewHolder extends RecyclerView.ViewHolder
    {
        public final View      mView;
        public final TextView  mIdView;
        public final TextView mSizeView;
        public final TextView mDelivery;
        public final TextView mAccept;
        public final TextView mWeightView;
        public final View     mRootView;
        public       BMPPacket mItem;

        public CompletedViewHolder(View view)
        {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mSizeView = (TextView) view.findViewById(R.id.packet_size);
            mWeightView = (TextView) view.findViewById(R.id.packet_weight);
            mDelivery = (TextView) view.findViewById(R.id.tv_delivery);
            mAccept = (TextView) view.findViewById(R.id.btn_accept);
            mRootView = view.findViewById(R.id.card_view);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mSizeView.getText() + "'";
        }
    }
}
