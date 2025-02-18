package com.example.banco_sisaov.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.banco_sisaov.R
import com.example.banco_sisaov.Tema9.CajeroEntity
import com.example.banco_sisaov.databinding.ItemCuentaBinding
import com.example.banco_sisaov.interfacesRecyclerViews.OnClickListenerCajero

class CajeroAdapter(private var cajeros: List<CajeroEntity>?, private val listener: OnClickListenerCajero): RecyclerView.Adapter<CajeroAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemCuentaBinding.bind(view)

        fun setListener(cajero: CajeroEntity) {
            binding.root.setOnClickListener {listener.onClick(cajero)}
        }
    }

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_cuenta, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = cajeros?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cajero: CajeroEntity = cajeros?.get(position) as CajeroEntity

        with(holder) {
            setListener(cajero)
            binding.txtNumCuenta.text = "ATM "+ cajero.id.toString()
            binding.txtDinero.text = cajero.direccion.toString()
        }
    }

    fun updateData(newCajeros: List<CajeroEntity>) {
        cajeros = newCajeros
        notifyDataSetChanged()
    }
}