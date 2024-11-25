package com.example.banco_sisaov.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banco_sisaov.R
import com.example.banco_sisaov.adapters.MovimientoAdapter
import com.example.banco_sisaov.bd.MiBancoOperacional
import com.example.banco_sisaov.databinding.FragmentAccountsMovementsBinding
import com.example.banco_sisaov.interfacesRecyclerViews.OnClickListenerMov
import com.example.banco_sisaov.pojo.Cliente
import com.example.banco_sisaov.pojo.Cuenta
import com.example.banco_sisaov.pojo.Movimiento
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val ARG_ACCOUNT = "cuenta"

class AccountsMovementsFragment : Fragment(), OnClickListenerMov {

    private lateinit var movimientoAdapter: MovimientoAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var binding: FragmentAccountsMovementsBinding
    private lateinit var cuenta: Cuenta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cuenta = Cuenta()
        arguments?.let {
            cuenta = it.getSerializable(ARG_ACCOUNT) as Cuenta
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountsMovementsBinding.inflate(inflater, container, false)

        //TODO:tot lo del adapter per el recycler view
        //Se crea la instancia de la bd
        val mbo: MiBancoOperacional? = MiBancoOperacional.getInstance(context)
        val movimientos = mbo?.getMovimientos(cuenta as Cuenta?) as ArrayList<Movimiento>?

        movimientoAdapter = MovimientoAdapter(movimientos, this)
        linearLayoutManager = LinearLayoutManager(context)

        binding.rVFAM.apply {
            adapter =  movimientoAdapter
            layoutManager = linearLayoutManager
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(c: Cuenta) =
            AccountsMovementsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ACCOUNT, c)
                }
            }
    }

    override fun OnClick(movimiento: Movimiento) {
        val dialogo = layoutInflater.inflate(R.layout.dialog_movements, null)
        var textViewDescripcion: String = buildString {
            append("ID: ")
            append(movimiento.getId())
            append("\n")
            append("Importe: ")
            append(movimiento.getImporte())
            append("\n")
            append("Fecha de la operacion: \n")
            append(movimiento.getFechaOperacionNormal())
            append("\n")
            append("Descripcion: \n")
            append(movimiento.getDescripcion())
        }

        dialogo.findViewById<TextView>(R.id.tv_descripcion).text = textViewDescripcion

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setView(dialogo)
                .setPositiveButton(getString(R.string.cancelar), DialogInterface.OnClickListener{ dialogo, i ->
                    dialogo.cancel()
                })
                .setCancelable(false)
                .show()
        }
    }
}