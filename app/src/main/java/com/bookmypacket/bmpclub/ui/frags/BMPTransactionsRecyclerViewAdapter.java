package com.bookmypacket.bmpclub.ui.frags;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.Transaction;

import java.util.List;

import static android.text.TextUtils.isEmpty;
import static com.bookmypacket.bmpclub.utils.AppConstants.RS_SYMBOL;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Transaction} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BMPTransactionsRecyclerViewAdapter
        extends RecyclerView.Adapter<BMPTransactionsRecyclerViewAdapter.ViewHolder>
{

    private final List<Transaction>                 mValues;
    private final OnListFragmentInteractionListener mListener;

    public BMPTransactionsRecyclerViewAdapter(List<Transaction> items,
                                              OnListFragmentInteractionListener listener)
    {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_transactions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);

        holder.mTransactionDate.setText(holder.mItem.getDateCreated());
        String  appender = "";
        int colorResource = R.color.color_hold;
        Context ctx      = (Context) mListener;
        String  type     = holder.mItem.getTransactionType();
        holder.mTxAmount.setText(RS_SYMBOL + holder.mItem.getTransactionAmount());
        if (type.equalsIgnoreCase("PACKET_COST_LOCKED"))
        {
            holder.mTxTypeIV.setImageResource(R.drawable.ic_debit_packet);
            appender = ctx.getString(R.string.wallet_packet_cost_locked);
            colorResource = R.color.color_hold;

        } else if (type.equalsIgnoreCase("CREDIT_HOLD_PROCESSED"))
        {
            holder.mTxTypeIV.setImageResource(R.drawable.ic_withdraw);
            appender = ctx.getString(R.string.wallet_packet_hold_release);
            colorResource = R.color.color_credit;

        } else if (type.equalsIgnoreCase("CREDIT_HOLD"))
        {
            holder.mTxTypeIV.setImageResource(R.drawable.ic_hold);

            appender = ctx.getString(R.string.wallet_pending_amount);
            colorResource = R.color.color_hold;
        } else if (type.equalsIgnoreCase("REIMBURSE_HOLD"))
        {
            holder.mTxTypeIV.setImageResource(R.drawable.ic_cash);
            appender = ctx.getString(R.string.wallet_reim_hold);
            colorResource = R.color.color_hold;
        } else {
            appender = type.replaceAll("_", " ").toLowerCase();
        }
        if (holder.mItem.getTransactionId() != null) {
            appender = appender + " " + holder.mItem.getTransactionId();
        }
        holder.mTransactionView.setText(appender);
        holder.mRootView.setBackgroundResource(colorResource);

    }

    private String getTransaction(String pre, String post)
    {
        String valStr     = RS_SYMBOL;
        double preAmount  = 0.0;
        double postAmount = 0.0;
        if (!isEmpty(pre))
        {
            preAmount = Double.parseDouble(pre);
        }
        if (!isEmpty(post))
        {
            postAmount = Double.parseDouble(post);
        }
        return valStr + (postAmount - preAmount);
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View        mView;
        public final TextView    mTransactionView;
        public final TextView    mTransactionDate;
        public final TextView    mTxAmount;
        public final ImageView   mTxTypeIV;
        public final View mRootView;
        public       Transaction mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mTransactionView = (TextView) view.findViewById(R.id.tv_transactionId);
            mTransactionDate = (TextView) view.findViewById(R.id.tv_txdate);
            mTxAmount = (TextView) view.findViewById(R.id.tv_tx_amount);
            mTxTypeIV = (ImageView) view.findViewById(R.id.iv_txtype);
            mRootView = (View) view.findViewById(R.id.card_view);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mTransactionDate.getText() + "'";
        }
    }
}
