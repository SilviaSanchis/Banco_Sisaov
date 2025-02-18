package com.example.banco_sisaov.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.banco_sisaov.R
import com.example.banco_sisaov.Tema9.CajeroApplication
import com.example.banco_sisaov.Tema9.CajeroEntity
import com.example.banco_sisaov.databinding.FragmentAtmManagerBinding
import com.example.banco_sisaov.pojo.Cliente
import kotlin.properties.Delegates

private const val ARG_CAJERO = "cajero"

class AtmManagerFragment : Fragment() {
    private lateinit var binding: FragmentAtmManagerBinding
    private lateinit var cajero: CajeroEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cajero = it.getSerializable(ARG_CAJERO) as CajeroEntity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAtmManagerBinding.inflate(inflater, container, false)

        if (!cajero.direccion.equals("")) {
            binding.btnBorra.visibility = View.VISIBLE
            binding.btnGuarda.setText(R.string.actualizar)

            binding.tietAdress.setText(cajero.direccion)
            binding.tietLatitude.setText(cajero.latitud.toString())
            binding.tietLongitude.setText(cajero.longitud.toString())

            binding.btnGuarda.setOnClickListener {
                val cajeroUpdated = CajeroEntity(
                    cajero.id,
                    binding.tietAdress.text.toString(),
                    binding.tietLatitude.text.toString().toDouble(),
                    binding.tietLongitude.text.toString().toDouble(),
                    cajero.zoom)

                Thread {
                    CajeroApplication.dataBase.cajeroDao().updateCajero(cajeroUpdated)
                }.start()
                Toast.makeText(context, "Actualizado con exito!", Toast.LENGTH_SHORT).show()
                //parentFragmentManager.popBackStack()
                requireActivity().supportFragmentManager.popBackStack()
            }

            binding.btnBorra.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar eliminacion")
                    .setMessage("Estas seguro de eliminar el cajero?")
                    .setPositiveButton("SI"){_, _ ->
                        Thread{
                            CajeroApplication.dataBase.cajeroDao().deleteCajero(cajero!!)
                        }.start()
                        Toast.makeText(context, "Eliminado con exito!", Toast.LENGTH_SHORT).show()
                        //parentFragmentManager.popBackStack()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                    .setNegativeButton("NO", null)
                    .show()

            }
        } else {
            binding.btnBorra.visibility = View.INVISIBLE
            binding.btnGuarda.setText(R.string.guardar)

            binding.btnGuarda.setOnClickListener {
                if ( binding.tietAdress.text!!.isNotEmpty() && binding.tietLatitude.text!!.isNotEmpty() && binding.tietLongitude.text!!.isNotEmpty()) {
                    val cajeroNuevo = CajeroEntity(
                        direccion = binding.tietAdress.text.toString(),
                        latitud = binding.tietLatitude.text.toString().toDouble(),
                        longitud = binding.tietLongitude.text.toString().toDouble(),
                        zoom = "")

                    Thread {
                        CajeroApplication.dataBase.cajeroDao().addCajero(cajeroNuevo)
                    }.start()
                    Toast.makeText(context, "Creado con exito!", Toast.LENGTH_SHORT).show()
                    //parentFragmentManager.popBackStack()
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "Tienes que rellenar todos los campos para crear el cajero", Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.btnCancela.setOnClickListener {
            //parentFragmentManager.popBackStack()

            requireActivity().supportFragmentManager.popBackStack()

        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(cajero: CajeroEntity) =
            AtmManagerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CAJERO, cajero)
                }
            }
    }
}