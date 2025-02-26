package com.example.banco_sisaov.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banco_sisaov.R
import com.example.banco_sisaov.Tema9.CajeroEntity
import com.example.banco_sisaov.databinding.FragmentAtmButtonsBinding
import com.example.banco_sisaov.maps.MapsFragment
import com.example.banco_sisaov.pojo.Cliente

private const val ARG_CLIENTE = "cliente"
/**
 * A simple [Fragment] subclass.
 * Use the [AtmButtonsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AtmButtonsFragment : Fragment() {

    private lateinit var binding: FragmentAtmButtonsBinding
    private lateinit var cliente: Cliente

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
        binding = FragmentAtmButtonsBinding.inflate(inflater, container, false)

        if (cliente.getNif() == "00000000S") binding.btnADD.visibility = View.VISIBLE

        binding.btnList.setOnClickListener {
            val frgAtmList: AtmListFragment = AtmListFragment.newInstance(cliente)

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragContainerATM, frgAtmList)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.btnADD.setOnClickListener {
            val cajero = CajeroEntity(direccion = "", latitud = 0.0, longitud = 0.0, zoom = "13f")
            val frgAtmManager: AtmManagerFragment = AtmManagerFragment.newInstance(cajero)

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragContainerATM, frgAtmManager)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.btnMaps.setOnClickListener {
            val frgMaps: MapsFragment = MapsFragment.newInstance(0.0, 0.0, -1, 13f)

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragContainerATM, frgMaps)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(c: Cliente) =
            AtmButtonsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CLIENTE, c)
                }
            }
    }
}