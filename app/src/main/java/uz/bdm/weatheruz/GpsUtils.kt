package uz.bdm.weatheruz

import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class GpsUtils(context: Context) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val TAG = "GPS"
    private val mcontext: Context = context

    private var mSettingClient: SettingsClient? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null

    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null


    init {
        mLocationManager = mcontext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mSettingClient = LocationServices.getSettingsClient(mcontext)

        mLocationRequest = LocationRequest.create()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mLocationRequest?.interval = 1000
        mLocationRequest?.fastestInterval = 500

        if (mLocationRequest != null) {
            val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest!!)
            mLocationSettingsRequest = builder.build()
        }
    }


     fun enableLocationSettings(context:Context) {
        val locationRequest = LocationRequest.create()
            .setInterval(5000)
            .setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        LocationServices
            .getSettingsClient(context)
            .checkLocationSettings(builder.build())
            .addOnSuccessListener(context as Activity) { response: LocationSettingsResponse? -> }
            .addOnFailureListener(context) { ex ->
                if (ex is ResolvableApiException) {
                    // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                        val resolvable = ex as ResolvableApiException
                        resolvable.startResolutionForResult(context as Activity, Constants.GPS_CODE)
                    } catch (sendEx: SendIntentException) {
                        // Ignore the error.
                    }
                }
            }

    }

}