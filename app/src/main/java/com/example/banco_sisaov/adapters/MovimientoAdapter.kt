package com.example.banco_sisaov.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.banco_sisaov.R
import com.example.banco_sisaov.databinding.ItemMovimientoBinding
import com.example.banco_sisaov.interfacesRecyclerViews.OnClickListenerMov
import com.example.banco_sisaov.pojo.Movimiento

class MovimientoAdapter(private val movimientos: ArrayList<*>, private val listener: OnClickListenerMov) : RecyclerView.Adapter<MovimientoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemMovimientoBinding.bind(view)

        fun setListener(movimiento: Movimiento){
            binding.root.setOnClickListener {listener.OnClick(movimiento)}
        }
    }

    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_movimiento, parent, false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int = movimientos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movimiento: Movimiento = movimientos.get(position) as Movimiento
        with(holder) {
            setListener(movimiento)
            binding.txtNombreMov.text = movimiento.getDescripcion()
            binding.txtDinero.text = buildString {
                append(movimiento.getFechaOperacionNormal())
                append("   "+ context.getString(R.string.importe_ActMov) +" ")
                append(movimiento.getImporte().toString())
            }
        }
    }
}