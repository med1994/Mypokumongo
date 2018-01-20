package mohamed.com.mypokumongo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static mohamed.com.mypokumongo.MyLocationListner.location;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LoadPokemons();
        checkPermison();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



    }
    // control sur le permmision
    final int  REQUEST_CODE_ASK_PERMISSIONS=10;
    protected void checkPermison(){
        if (Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
            return;
            }
        }
GetLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
            if (grantResults [0]== PackageManager.PERMISSION_GRANTED) {
                GetLocation();

            }else {
                Toast.makeText(this,"didnot accept Location cannot find pokumon",Toast.LENGTH_LONG).show();
            }
            break;

        default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    }

    public  void GetLocation(){
        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        MyLocationListner myLocationListner=new MyLocationListner();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3,myLocationListner);
         Mythread mythread=new Mythread();
        mythread.start();
    }
    Location OldLocation;
    class Mythread extends Thread{
        public Mythread() {
            OldLocation=new Location("zero");
            OldLocation.setLatitude(0);
            OldLocation.setLatitude(0);
        }

        @Override
        public void run(){
            while (true){
                if (OldLocation.distanceTo(MyLocationListner.location)==0){
                continue;
                }
                OldLocation=MyLocationListner.location;
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.clear();
                            // Add a marker in my location
                            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker my Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                            .title("Me"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14));
                            //show pokumen
                            for(int i=0;i<listPockumons.size();i++){
                                Pockemon pockemon=listPockumons.get(i);
                                if (pockemon.isCatch==false){
                                    LatLng pokumonLocation= new LatLng(pockemon.location.getLatitude(), pockemon.location.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(pokumonLocation).title(pockemon.name).snippet(pockemon.des+"  power : "+pockemon.power)
                                    .icon(BitmapDescriptorFactory.fromResource(pockemon.Image))
                                    );
                                    if (pockemon.location.distanceTo(MyLocationListner.location )<2){
                                        Mypower=Mypower+pockemon.power;
                                        pockemon.isCatch=true;
                                        listPockumons.set(i,pockemon);
                                        Toast.makeText(MapsActivity.this,"You catch new pokumon ",Toast.LENGTH_LONG).show();

                                    }
                                }
                            }

                        }
                    });
                    Thread.sleep(10000);

                }catch (Exception e){

                }
            }
        }
    }
    double Mypower=0;

    ArrayList<Pockemon> listPockumons=new ArrayList<>();
    void LoadPokemons(){
        listPockumons.add(new Pockemon(  R.drawable.charmander, "Charmander", "Charmander living in japan", 55, 33.1795224, 10.4384186));
        listPockumons.add(new Pockemon(  R.drawable.bulbasaur ,"Bulbasaur",  "Bulbasaur living in usa", 90.5, 37.7949568502667,  -122.410494089127));
        listPockumons.add(new Pockemon(  R.drawable.squirtle, "Squirtle",  "Squirtle living in iraq", 33.5, 37.7816621152613,  -122.41225361824  ));

    }
}
