package com.example.banco_sisaov.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banco_sisaov.R
import com.example.banco_sisaov.Tema9.CajeroApplication
import com.example.banco_sisaov.Tema9.CajeroEntity
import com.example.banco_sisaov.adapters.CajeroAdapter
import com.example.banco_sisaov.databinding.FragmentAtmListBinding
import com.example.banco_sisaov.interfacesRecyclerViews.OnClickListenerCajero
import com.example.banco_sisaov.pojo.Cliente
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_CLIENTE = "cliente"

class AtmListFragment : Fragment(), OnClickListenerCajero {
    private lateinit var binding: FragmentAtmListBinding

    private lateinit var cajeroAdapter: CajeroAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var cliente: Cliente
    private var cajeroEntitis: List<CajeroEntity> = listOf()

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
        binding = FragmentAtmListBinding.inflate(inflater, container, false)

        ///////////////
        /*Thread{

        }.start()*/
        /////////////////
        cajeroAdapter = CajeroAdapter(cajeroEntitis, this)
        linearLayoutManager = LinearLayoutManager(context)

        binding.rvAtmList.apply {
            adapter = cajeroAdapter
            layoutManager = linearLayoutManager
        }

        cargarCajeros()

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(c: Cliente) =
            AtmListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CLIENTE, c)
                }
            }
    }

    private fun cargarCajeros() {
        CoroutineScope(Dispatchers.IO).launch {
            val cajeroEntitis = CajeroApplication.dataBase.cajeroDao().getAllCajeros()

            withContext(Dispatchers.Main) {
                cajeroAdapter.updateData(cajeroEntitis)
            }
        }
    }

    override fun onClick(cajero: CajeroEntity) {
        if (cliente.getNif() == "00000000S") {
            //enviar al fragment del manager
            val frgAtmManager: AtmManagerFragment = AtmManagerFragment.newInstance(cajero)

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragContainerATM, frgAtmManager)
            transaction.commit()
        } else {
            val dialogo = layoutInflater.inflate(R.layout.dialog_movements, null)
            var textViewDescripcion: String = buildString {
                append("ID: ")
                append(cajero.id.toString())
                append("\n")
                append("\n")
                append("Direccion: \n")
                append(cajero.direccion.toString())
                append("\n")
                append("\n")
                append("Latitud: \n")
                append(cajero.latitud.toString())
                append("\n")
                append("\n")
                append("Longitud: \n")
                append(cajero.longitud.toString())
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
}