package com.medical.mypolygonmap.Activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.medical.mypolygonmap.R
import java.io.IOException
import java.lang.Math.toRadians


class MainActivity : AppCompatActivity(), OnMyLocationButtonClickListener,
    GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
    OnMyLocationClickListener, OnMapReadyCallback,
    OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    View.OnClickListener, View.OnLongClickListener {

    private var permissionDenied = false
    private lateinit var map: GoogleMap
    private val PERMISSION_REQUEST_CODE: Int = 101
    var points: ArrayList<LatLng>? = null
    val MIN_ZOOM = 5
    private lateinit var bt_ResetMap: Button
    var mLocationRequest: LocationRequest? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    lateinit var btSearch: Button
    lateinit var btDetail: Button
    lateinit var btUndo: Button
    lateinit var edSearch: EditText
    lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "kotlinsharedpreference"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.polygonPlotMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        sharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        bt_ResetMap = findViewById(R.id.bt_ResetMap)
        btSearch = findViewById(R.id.btSearch)
        btDetail = findViewById(R.id.bt_Detail)
        btUndo = findViewById(R.id.bt_undo)
        edSearch = findViewById(R.id.edinput_Serarch)
        bt_ResetMap.setOnClickListener(this)
        btSearch.setOnClickListener(this)
        btDetail.setOnClickListener(this)
        btUndo.setOnClickListener(this)
        bt_ResetMap.setOnLongClickListener(this)
        points = ArrayList()
        btUndo.let {
            it.isEnabled = false
            it.setBackgroundResource(R.drawable.bg_btundo)
            it.setTextColor(Color.parseColor("#5D98F9"))
        }
        bt_ResetMap.let {
            it.isEnabled = false
            it.setBackgroundResource(R.drawable.bg_btundo)
            it.setTextColor(Color.parseColor("#5D98F9"))

        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        map.setOnMyLocationClickListener(this)
        map.uiSettings.isZoomControlsEnabled = true
        enableMyLocation()
        map.setOnMapClickListener(this)
        map.setOnMapLongClickListener(this)
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (checkPermission()) {
            map.isMyLocationEnabled = true
        } else {
            requestPermission()
            buildGoogleApiClient()
        }
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation()
                } else {
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_LONG)
                        .show()
                }
                return
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            permissionDenied = false
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onMapClick(point: LatLng?) {

    }

    override fun onMapLongClick(point: LatLng?) {
        if (point != null) {
            Log.i("the", point?.latitude.toString() + " " + point?.longitude)
            var polylineOptions = PolylineOptions()
            polylineOptions.color(Color.RED)
            polylineOptions.width(5f)
            if (point != null) {
                points!!.add(point)

            }
            polylineOptions.addAll(points)
            map.addPolyline(polylineOptions)

            val circleOptions = CircleOptions()
                .center(point)
                .radius(10.0)
            map.addCircle(circleOptions)
            val vb: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vb.vibrate(100)
        }
        bt_ResetMap.let {
            it.isEnabled = true
            it.setBackgroundResource(R.drawable.bg_reset)
            it.setTextColor(Color.parseColor("#FFFFFF"))
        }
        btDetail.isEnabled = true

    }


    override fun onResume() {
        super.onResume()

    }

    override fun onClick(view: View?) {
        val id = view?.id
        when (id) {
            R.id.bt_ResetMap -> {
                val dialog = android.app.AlertDialog.Builder(this)
                dialog.setMessage("Please Select any option")
                dialog.setTitle(R.string.clear_label)
                dialog.setPositiveButton(
                    R.string.reset_label,
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        val polylines: List<Polyline> = ArrayList()
                        for (line in polylines) {
                            line.remove()
                        }
                        points?.clear()
                        map.clear()

                        bt_ResetMap.let {
                            it.isEnabled = false
                            it.setBackgroundResource(R.drawable.bg_btundo)
                            it.setTextColor(Color.parseColor("#5D98F9"))
                        }
                        btUndo.let {
                            it.isEnabled = true
                            it.setBackgroundResource(R.drawable.bg_reset)
                            it.setTextColor(Color.parseColor("#FFFFFF"))
                        }

                    })

                dialog.setNegativeButton(
                    R.string.cancel_label,
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.dismiss()
                        }
                    })
                val alertDialog = dialog.create()
                alertDialog.show()
                btUndo.isEnabled = true
                btUndo.visibility = View.VISIBLE

            }

            R.id.btSearch -> {
                var location = edSearch.text.toString()
                var addressList: List<Address>? = null
                if (location != null || !location.equals("")) {
                    val geocoder = Geocoder(this)
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    map.addMarker(MarkerOptions().position(latLng).title(location))
                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    Toast.makeText(
                        applicationContext,
                        address.latitude.toString() + " " + address.longitude,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            R.id.bt_Detail -> {

                try {
                    if (points!!.size > 0 && points != null) {
                val intent = Intent(applicationContext, DetailInfoActivity::class.java)
                        intent.putExtra("list", points)
                        startActivity(intent)
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putFloat("coordinateOnelat", points!!.get(0).latitude.toFloat())
                        editor.putFloat("coordinateOnelng", points!!.get(0).longitude.toFloat())
                        editor.putFloat("coordinateTwolat", points!!.get(1).latitude.toFloat())
                        editor.putFloat("coordinateTwolng", points!!.get(1).longitude.toFloat())
                        editor.putFloat("coordinateThreelat", points!!.get(2).latitude.toFloat())
                        editor.putFloat("coordinateThreelng", points!!.get(2).longitude.toFloat())
                        editor.putFloat("coordinateFourlat", points!!.get(3).latitude.toFloat())
                        editor.putFloat("coordinateFourlng", points!!.get(3).longitude.toFloat())
                        editor.putFloat("coordinateFivelat", points!!.get(4).latitude.toFloat())
                        editor.putFloat("coordinateFivelng", points!!.get(4).longitude.toFloat())
                        editor.apply()
                        editor.commit()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    Toast.makeText(
                        applicationContext,
                        "Please first plot the coordinates",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            R.id.bt_undo -> {
                val sharedNameValueOnelat: Float =
                    sharedPreferences.getFloat("coordinateOnelat", 0.0f)
                val sharedNameValueOnelng: Float = sharedPreferences.getFloat(
                    "coordinateOnelng",
                    0.0f
                )
                val sharedNameValueTwolat: Float = sharedPreferences.getFloat(
                    "coordinateTwolat",
                    0.0f
                )
                val sharedNameValueTwolng: Float = sharedPreferences.getFloat(
                    "coordinateTwolng",
                    0.0f
                )
                val sharedNameValueThreelat: Float = sharedPreferences.getFloat(
                    "coordinateThreelat",
                    0.0f
                )
                val sharedNameValueThreelng: Float = sharedPreferences.getFloat(
                    "coordinateThreelng",
                    0.0f
                )
                val sharedNameValueFourlat: Float = sharedPreferences.getFloat(
                    "coordinateFourlat",
                    0.0f
                )
                val sharedNameValueFourlng: Float = sharedPreferences.getFloat(
                    "coordinateFourlng",
                    0.0f
                )
                val sharedNameValueFivelat: Float = sharedPreferences.getFloat(
                    "coordinateFivelat",
                    0.0f
                )
                val sharedNameValueFivelng: Float = sharedPreferences.getFloat(
                    "coordinateFivelng",
                    0.0f
                )
                val polyline1 = map.addPolyline(
                    PolylineOptions()
                        .clickable(true)
                        .color(Color.BLUE)
                        .width(5f)
                        .add(
                            LatLng(
                                sharedNameValueOnelat.toDouble(),
                                sharedNameValueOnelng.toDouble()
                            ),
                            LatLng(
                                sharedNameValueTwolat.toDouble(),
                                sharedNameValueTwolng.toDouble()
                            ),
                            LatLng(
                                sharedNameValueThreelat.toDouble(),
                                sharedNameValueThreelng.toDouble()
                            ),
                            LatLng(
                                sharedNameValueFourlat.toDouble(),
                                sharedNameValueFourlng.toDouble()
                            ),
                            LatLng(
                                sharedNameValueFivelat.toDouble(),
                                sharedNameValueFivelng.toDouble()
                            )
                        )
                )

                btUndo.let {
                    it.isEnabled = false
                    it.setBackgroundResource(R.drawable.bg_btundo)
                    it.setTextColor(Color.parseColor("#5D98F9"))
                }
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLongClick(view: View?): Boolean {
        val id = view?.id
        when (id) {
            R.id.bt_undo -> {
                map.clear()
            }
        }
        return true
    }

    fun CalculationByDistance(initialLat: Double, initialLong: Double, finalLat: Double, finalLong: Double
    ): Double {
        val R = 6371 // km
        val dLat: Double = toRadians(finalLat - initialLat)
        val dLon: Double = toRadians(finalLong - initialLong)
       val lat1 = toRadians(points!!.get(0).latitude)
        val lat2 = toRadians(points!!.get(0).longitude)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
        Log.i("the","area"+ R*c)
    }

    fun deg2Radian( coordinateLayout:Double):Double{
        return coordinateLayout*Math.PI/180.0
    }




}