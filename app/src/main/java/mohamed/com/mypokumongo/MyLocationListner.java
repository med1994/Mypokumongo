package mohamed.com.mypokumongo;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by mohamed on 19/01/2018.
 */

public class MyLocationListner implements LocationListener {
    public static  Location location;

    public MyLocationListner() {
    location=new Location("zero");
    location.setLatitude(0);
    location.setLatitude(0);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
