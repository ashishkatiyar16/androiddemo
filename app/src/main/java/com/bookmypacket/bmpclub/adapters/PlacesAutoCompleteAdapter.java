package com.bookmypacket.bmpclub.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Manish on 19-01-2016.
 */
public class PlacesAutoCompleteAdapter extends ArrayAdapter<AutocompletePrediction>
        implements Filterable {

    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    private GoogleApiClient googleApiClient;
    private LatLngBounds latLngBounds;
    private AutocompleteFilter autocompleteFilter;

    private ArrayList<AutocompletePrediction> resultList;


    public PlacesAutoCompleteAdapter(Context context, GoogleApiClient googleApiClient, LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter) {
        super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1);
        this.googleApiClient = googleApiClient;
        this.latLngBounds = latLngBounds;
        this.autocompleteFilter = autocompleteFilter;
    }

    public PlacesAutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
    }

    public LatLngBounds getLatLngBounds() {
        return latLngBounds;
    }

    public void setLatLngBounds(LatLngBounds latLngBounds) {
        this.latLngBounds = latLngBounds;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public AutocompletePrediction getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        AutocompletePrediction item = getItem(position);
        TextView tv1 = (TextView) row.findViewById(android.R.id.text1);
        TextView tv2 = (TextView) row.findViewById(android.R.id.text2);
        String fullAddress = item.getFullText(null).toString();
        tv1.setText(item.getPrimaryText(STYLE_BOLD));
        tv2.setText(item.getSecondaryText(STYLE_BOLD));
        return row;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    resultList = getAutocomplete(constraint);
                    if(resultList!=null)
                    {
                        results.count = resultList.size();
                        results.values = resultList;
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results!=null && results.count>0)
                {
                    notifyDataSetChanged();
                }else {
                    notifyDataSetInvalidated();
                }

            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                if(resultValue instanceof AutocompletePrediction)
                {
                    return ((AutocompletePrediction)resultValue).getFullText(null);
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }


    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        if (googleApiClient.isConnected()) {

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(googleApiClient, constraint.toString(),
                                    latLngBounds, autocompleteFilter);

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
                        Toast.LENGTH_SHORT).show();
                autocompletePredictions.release();
                return null;
            }

            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        }
        return null;
    }

}
