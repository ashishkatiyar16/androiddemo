package com.bookmypacket.bmpclub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.Transaction;
import com.bookmypacket.bmpclub.dto.WalletTransactionsRequest;
import com.bookmypacket.bmpclub.listeners.EndlessRecyclerOnScrollListener;
import com.bookmypacket.bmpclub.ui.frags.BMPTransactionsRecyclerViewAdapter;
import com.bookmypacket.bmpclub.ui.frags.OnListFragmentInteractionListener;
import com.bookmypacket.bmpclub.utils.AppConstants;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;
import com.bookmypacket.bmpclub.utils.retro.WalletTransactionsHistoryService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.bookmypacket.bmpclub.utils.AppConstants.SharedPrefrencesKeys.WALLET_AVAILABLE_AMOUNT;
import static com.bookmypacket.bmpclub.utils.AppConstants.SharedPrefrencesKeys.WALLET_HOLD_AMOUNT;

public class BMPWalletActivity extends BaseActivity implements
                                                    OnListFragmentInteractionListener<Transaction>
{
    private double   availableAmount;
    private double   holdAmount;
    private TextView tvAvailableAmount;
    private List<Transaction> transactions = new ArrayList<>();
    private BMPTransactionsRecyclerViewAdapter mAdapter;
    private LinearLayoutManager                layoutManager;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmpwallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(BMPWalletActivity.this, WithdrawMoneyActivity.class);
                startActivity(i);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new BMPTransactionsRecyclerViewAdapter(transactions, this);
        initIds();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        processWalletAmount();
        setValues();
        addActions();
        loadTransactions(0);
    }

    private void processWalletAmount()
    {
        String availAmount = SharedPrefrenceManager.getPrefrence(this, WALLET_AVAILABLE_AMOUNT);
        String holdAmt     = SharedPrefrenceManager.getPrefrence(this, WALLET_HOLD_AMOUNT);
        if (!isEmpty(availAmount))
        {
            availableAmount = Double.parseDouble(availAmount);
        }
        if (!isEmpty(holdAmt))
        {
            holdAmount = Double.parseDouble(holdAmt);
        }
    }

    private void initIds()
    {
        tvAvailableAmount = (TextView) findViewById(R.id.tv_available);

        recyclerView = (RecyclerView) findViewById(R.id.rv_txlist);

    }

    private void setValues()
    {
        tvAvailableAmount.setText(AppConstants.RS_SYMBOL + availableAmount);
    }

    private void loadTransactions(int pageno)
    {
        progressDialog.setMessage("Wait...");
        progressDialog.show();
        String                    authKey      = SharedPrefrenceManager.getAuthHeader(this);
        String                    mobileNumber = SharedPrefrenceManager.getMobileNo(this);
        WalletTransactionsRequest request      = new WalletTransactionsRequest();
        request.setMobileNo(mobileNumber);
        request.setPageNumber(String.valueOf(pageno));
        WalletTransactionsHistoryService service = ServiceGenerator.createService
                (WalletTransactionsHistoryService.class);
        Call<List<Transaction>> call = service.get(authKey, request);
        call.enqueue(new Callback<List<Transaction>>()
        {
            @Override
            public void onResponse(Response<List<Transaction>> response, Retrofit retrofit)
            {
                progressDialog.dismiss();
                if (response.body() != null && response.body().size() > 0)
                {
                    transactions.addAll(response.body());
                } else if (response.body() != null)
                {
                    makeText(BMPWalletActivity.this, R.string.wallet_end, LENGTH_LONG).show();
                } else
                {
                    makeText(BMPWalletActivity.this, R.string.json_error, LENGTH_LONG).show();
                }
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
//                        mAdapter = new BMPTransactionsRecyclerViewAdapter(transactions,
//                                                                          BMPWalletActivity.this);
                        // recyclerView.setAdapter(mAdapter);
                        recyclerView.invalidate();
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onFailure(Throwable t)
            {
                progressDialog.dismiss();
            }
        });
    }

    private void addActions()
    {
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                loadTransactions(current_page);
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Transaction item)
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bmp, menu);
        return true;
    }
}
