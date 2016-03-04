package com.bookmypacket.bmpclub.ui.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookmypacket.bmpclub.R;
import com.bookmypacket.bmpclub.dto.BMPPacket;
import com.bookmypacket.bmpclub.dto.BMPPacketsList;
import com.bookmypacket.bmpclub.dto.PacketsListRequest;
import com.bookmypacket.bmpclub.utils.SharedPrefrenceManager;
import com.bookmypacket.bmpclub.utils.retro.GetAvailablePacketsService;
import com.bookmypacket.bmpclub.utils.retro.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BMPPacketProgressFragment extends Fragment
{


    private final List<BMPPacket> bmpPackets = new ArrayList<>();
    private RecyclerView                      recyclerView;
    private OnListFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OnPacketListResult listResultListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BMPPacketProgressFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View viewRoot = inflater.inflate(R.layout.fragment_bmppacket_progress_list, container,
                                         false);
        View view = viewRoot.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) viewRoot.findViewById(R.id.swipe_progress);
        // Set the adapter
        if (view instanceof RecyclerView)
        {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(
                    new BMPPacketProgressRecyclerViewAdapter(mListener, bmpPackets));
        }
        addActions();
        getPacketsList();
        return viewRoot;
    }

    private void addActions()
    {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getPacketsList();
            }
        });
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener)
        {
            mListener = (OnListFragmentInteractionListener) context;
        }
        if (context instanceof OnPacketListResult)
        {
            listResultListener = (OnPacketListResult) context;
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    private void getPacketsList()
    {
        PacketsListRequest request    = new PacketsListRequest();
        String             authHeader = SharedPrefrenceManager.getAuthHeader(getContext());
        String             mobileNo   = SharedPrefrenceManager.getMobileNo(getContext());
        request.setMobileNo(mobileNo);
        GetAvailablePacketsService service = ServiceGenerator.createService
                (GetAvailablePacketsService.class);
        Call<BMPPacketsList> call =
                service.register(authHeader, request);
        call.enqueue(new Callback<BMPPacketsList>()
        {
            @Override
            public void onResponse(final Response<BMPPacketsList> response, Retrofit retrofit)
            {
                try
                {
                    swipeRefreshLayout.setRefreshing(false);
                    if (response.body() != null && response.body().getPacketResponses() != null &&
                            response.body().getPacketResponses().size() != 0)
                    {
                        bmpPackets.removeAll(bmpPackets);
                        bmpPackets.addAll(response.body().getPacketResponses());
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                recyclerView.setAdapter(
                                        new BMPPacketProgressRecyclerViewAdapter(
                                                mListener, response.body().getPacketResponses()
                                        ));
                                recyclerView.invalidate();
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        });
                        listResultListener.packetsRecieved(bmpPackets, 1);
                    }
                }
                catch (Throwable t)
                {

                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
