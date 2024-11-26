package com.example.banco_sisaov.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.banco_sisaov.R
import com.example.banco_sisaov.databinding.ItemCuentaBinding
import com.example.banco_sisaov.interfacesRecyclerViews.OnClickListener
import com.example.banco_sisaov.pojo.Cuenta

class CuentaAdapter(private val cuentas: ArrayList<*>?, private val listener: OnClickListener): RecyclerView.Adapter<CuentaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemCuentaBinding.bind(view)

        fun setListener(cuenta: Cuenta) {
            binding.root.setOnClickListener {listener.onClick(cuenta)}
        }
    }

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_cuenta, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = cuentas?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cuenta: Cuenta =  cuentas?.get(position) as Cuenta
        with(holder) {
            setListener(cuenta)
            binding.txtNumCuenta.text = cuenta.getNumeroCuenta()
            binding.txtDinero.text = cuenta.getSaldoActual().toString()

            if (cuenta.getSaldoActual()!! < 0.0f) binding.txtDinero.setTextColor(getColor(context, R.color.roig))
            else binding.txtDinero.setTextColor(getColor(context, R.color.verd))
        }
    }
}