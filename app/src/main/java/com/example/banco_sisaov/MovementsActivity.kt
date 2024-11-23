package com.example.banco_sisaov

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banco_sisaov.adapters.MovimientoAdapter
import com.example.banco_sisaov.bd.MiBancoOperacional
import com.example.banco_sisaov.databinding.ActivityMovementsBinding
import com.example.banco_sisaov.interfacesRecyclerViews.OnClickListenerMov
import com.example.banco_sisaov.pojo.Cliente
import com.example.banco_sisaov.pojo.Cuenta
import com.example.banco_sisaov.pojo.Movimiento
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MovementsActivity : AppCompatActivity(), OnClickListenerMov {

    private lateinit var binding: ActivityMovementsBinding

    private lateinit var movementAdapter: MovimientoAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Se crea la instancia de la bd
        val mbo: MiBancoOperacional? = MiBancoOperacional.getInstance(this)

        binding = ActivityMovementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.movements)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cliente = intent.getSerializableExtra("Cliente") as Cliente
        val cuentas = mbo?.getCuentas(cliente) as ArrayList<*>
        val cuentasNumero = mutableListOf<String>()

        for (cuenta in cuentas) {
            val cuentaSel = cuenta as Cuenta
            cuentasNumero.add(cuentaSel.getNumeroCuenta()!!.toString())
        }

        //Spinner
        val adapterSpinner = ArrayAdapter(this, R.layout.item_spinner, cuentasNumero)
        adapterSpinner.setDropDownViewResource(R.layout.item_spinner)
        binding.sCuentas.adapter = adapterSpinner

        var cuentaSeleccionada: Cuenta = cuentas[0] as Cuenta

        //RecyclerView
        var movimientos = mbo?.getMovimientos(cuentaSeleccionada) as ArrayList<*>
        movementAdapter = MovimientoAdapter(movimientos, this)
        linearLayoutManager = LinearLayoutManager(this)

        binding.rVM.apply {
            layoutManager = linearLayoutManager
            adapter = movementAdapter
        }

        //Evento de cambiar de cuenta en el sppiner
        binding.sCuentas.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cuentaSeleccionada = cuentas[position] as Cuenta
                movimientos = mbo.getMovimientos(cuentaSeleccionada) as ArrayList<*>
                movementAdapter = MovimientoAdapter(movimientos, this@MovementsActivity)

                binding.rVM.apply {
                    layoutManager = linearLayoutManager
                    adapter = movementAdapter
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

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

        MaterialAlertDialogBuilder(this)
            .setView(dialogo)
            .setPositiveButton(getString(R.string.cancelar), DialogInterface.OnClickListener{ dialogo, i ->
                dialogo.cancel()
            })
            .setCancelable(false)
            .show()
    }

}