package com.bookmypacket.bmpclub.ui.frags;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.AcceptPacketRequest;
import com.bookmypacket.bmpclub.dto.AcceptPacketResponse;
import com.bookmypacket.bmpclub.dto.BMPPacket;
import com.bookmypacket.bmpclub.ui.BaseActivity;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.bookmypacket.bmpclub.utils.retro.PacketDeliveryAccept;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.bookmypacket.bmpclub.utils.AppConstants.RS_SYMBOL;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BMPPacket} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BMPPacketRecyclerViewAdapter extends BMPRecyclerViewAdapter
{


    public BMPPacketRecyclerViewAdapter(
            OnListFragmentInteractionListener mListener,
            List<BMPPacket> mValues)
    {
        super(mListener, mValues);
    }

    @Override
    public RecyclerView.ViewHolder customViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bmppacket, parent, false);
        return new PacketViewHolder(view);
    }

    @Override
    protected int emptyString()
    {
        return R.string.empty_bmp_packets_u;
    }

    @Override
    public void customBindViewHolder(final RecyclerView.ViewHolder h, int position)
    {
        if (mValues.size() > 0)
        {
            final PacketViewHolder holder = (PacketViewHolder) h;
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
            holder.mAccept.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    acceptPacket(packet);
                }
            });

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

    private void acceptPacket(BMPPacket packet)
    {
        final BaseActivity ctx = (BaseActivity) mListener;
        ctx.getProgressDialog().show();
        String              autHeader = SharedPrefrenceManager.getAuthHeader(ctx);
        String              mobile    = SharedPrefrenceManager.getMobileNo(ctx);
        AcceptPacketRequest request   = new AcceptPacketRequest();
        request.setAssignedTo(mobile);
        request.setPacketId(packet.getPacketId());
        request.setAcceptanceStatus("PACKET_DELIVERY_ACCEPTED");
        PacketDeliveryAccept service =
                ServiceGenerator.createService(PacketDeliveryAccept.class);
        Call<AcceptPacketResponse> call = service.acceptPacket(autHeader, request);
        call.enqueue(new Callback<AcceptPacketResponse>()
        {
            @Override
            public void onResponse(Response<AcceptPacketResponse> response, Retrofit retrofit)
            {
                ctx.getProgressDialog().dismiss();
                if (response.body() != null && response.body().getSuccess())
                {
                    Toast.makeText(ctx, R.string.packet_accepted, Toast.LENGTH_LONG)
                            .show();
                } else
                {
                    Toast.makeText(ctx, response.body().getErrorMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                ctx.getProgressDialog().dismiss();
            }
        });
    }


    public class PacketViewHolder extends RecyclerView.ViewHolder
    {
        public final View      mView;
        public final TextView  mIdView;
        public final TextView mSizeView;
        public final TextView mDelivery;
        public final TextView mAccept;
        public final TextView mWeightView;
        public final View     mRootView;
        public       BMPPacket mItem;

        public PacketViewHolder(View view)
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
