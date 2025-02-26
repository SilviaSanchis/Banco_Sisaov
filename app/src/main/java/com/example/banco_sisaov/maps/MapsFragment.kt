package com.example.banco_sisaov.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.banco_sisaov.R
import com.example.banco_sisaov.Tema9.CajeroApplication
import com.example.banco_sisaov.Tema9.CajeroEntity
import com.example.banco_sisaov.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_LAT = "lat"
private const val ARG_LONG = "long"
private const val ARG_ID = "id"
private const val ARG_ZOOM = "zoomin"

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding

    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private var id: Int = 0
    private var zoomin: Float = 0f
    private lateinit var cajeros: MutableList<CajeroEntity>
    private lateinit var gm: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        if (id == -1) {
            gm = googleMap
            cargarCajeros()
        } else {
            val ubi = LatLng(latitud, longitud)
            googleMap.addMarker(MarkerOptions().position(ubi).title("Cajero $id"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubi, zoomin))
        }
    }

    private fun cargarCajeros() {
        CoroutineScope(Dispatchers.IO).launch {
            val cajeroEntitis = CajeroApplication.dataBase.cajeroDao().getAllCajeros()
            withContext(Dispatchers.Main) {
                cajeros = cajeroEntitis

                for (cajero in cajeros) {
                    val ubi = LatLng(cajero.latitud!!, cajero.longitud!!)
                    gm.addMarker(MarkerOptions().position(ubi). title("Cajero ${cajero.id}"))
                    gm.moveCamera(CameraUpdateFactory.newLatLngZoom(ubi, cajero.zoom!!.toFloatOrNull() ?: 13f))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        arguments.let {
            latitud = it!!.getDouble(ARG_LAT)
            longitud = it.getDouble(ARG_LONG)
            id = it.getInt(ARG_ID)
            zoomin = it.getFloat(ARG_ZOOM)
        }
    }

    companion object{
        @JvmStatic
        fun newInstance(lat: Double, long: Double, id: Int, zoom: Float) =
            MapsFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_LAT, lat)
                    putDouble(ARG_LONG, long)
                    putInt(ARG_ID, id)
                    putFloat(ARG_ZOOM, zoom)
                }
            }
    }
}