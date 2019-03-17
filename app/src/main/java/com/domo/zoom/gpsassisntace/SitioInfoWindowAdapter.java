package com.domo.zoom.gpsassisntace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class SitioInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public SitioInfoWindowAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.sitio_infowindows, null);

        TextView tvGir = (TextView) v.findViewById(R.id.tvgir);
        TextView tvDetails = (TextView) v.findViewById(R.id.tvd);
        ImageView ivLogo = v.findViewById(R.id.imLogo);

        String[] partsTitle = marker.getSnippet().split("@");
        if (Integer.valueOf(partsTitle[0])>0){
            ivLogo.setVisibility(View.GONE);
        }
        // TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
        tvGir.setText(marker.getTitle());
        tvDetails.setText("Hacer click para más detalles...");
        //tvDetails.setText(marker.getSnippet());
        //tvLng.setText("Longitude:"+ latLng.longitude);
        return v;
    }


}
