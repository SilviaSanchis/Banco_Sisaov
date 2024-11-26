package com.example.banco_sisaov.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banco_sisaov.GlobalPositionDetailsActivity
import com.example.banco_sisaov.adapters.CuentaAdapter
import com.example.banco_sisaov.bd.MiBancoOperacional
import com.example.banco_sisaov.databinding.FragmentAccountsBinding
import com.example.banco_sisaov.interfacesRecyclerViews.OnClickListener
import com.example.banco_sisaov.pojo.Cliente
import com.example.banco_sisaov.pojo.Cuenta

private const val ARG_CLIENTE = "cliente"

class AccountsFragment : Fragment(), OnClickListener {

    private lateinit var cuentaAdapter: CuentaAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var binding: FragmentAccountsBinding
    private lateinit var cliente: Cliente

    private lateinit var listener: CuentaListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cliente = Cliente()
        arguments?.let {
            cliente = it.getSerializable(ARG_CLIENTE) as Cliente
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountsBinding.inflate(inflater, container, false)

        //TODO:tot lo del adapter per el recycler view
        //Se crea la instancia de la bd
        val mbo: MiBancoOperacional? = MiBancoOperacional.getInstance(context)
        val cuentas = mbo?.getCuentas(cliente as Cliente?) as ArrayList<Cuenta>?

        /*
        val clienteA = Cliente()
        clienteA.setNif("11111111A")
        clienteA.setClaveSeguridad("1234")

        val clienteYo = mbo?.login(clienteA) as Cliente
        val cuentas = mbo.getCuentas(clienteYo) as ArrayList<*>
        */

        cuentaAdapter = CuentaAdapter(cuentas, this)
        linearLayoutManager = LinearLayoutManager(context)

        binding.rVGP.apply {
            adapter = cuentaAdapter
            layoutManager = linearLayoutManager
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(c: Cliente) =
            AccountsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CLIENTE, c)
                }
            }
    }

    fun setCuentaListener(listener: CuentaListener) {
        this.listener = listener
    }

    override fun onClick(cuenta: Cuenta) {
        if (listener != null) {
            listener.onCuentaSeleccionada(cuenta)
        }

        /*
        val intent = Intent(requireContext(), GlobalPositionDetailsActivity::class.java).apply {
            putExtra("Cuenta", cuenta)
        }
        startActivity(intent)*/
    }
}