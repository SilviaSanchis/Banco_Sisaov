package com.example.banco_sisaov

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banco_sisaov.bd.MiBancoOperacional
import com.example.banco_sisaov.databinding.ActivityTransferenciaBinding
import com.example.banco_sisaov.pojo.Cliente
import com.example.banco_sisaov.pojo.Cuenta
import com.google.android.material.snackbar.Snackbar

class TransferenciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransferenciaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Se crea la instancia de la bd
        val mbo: MiBancoOperacional? = MiBancoOperacional.getInstance(this)

        binding = ActivityTransferenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.transferencia)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.transferencia.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            currentFocus?.clearFocus()
        }

        val cliente = intent.getSerializableExtra("Cliente") as Cliente

        //Sppiner de la moneda
        val moneda = arrayOf("Є", "$", "£")
        val adapterSpinnerMoneda = ArrayAdapter(this, R.layout.item_spinner, moneda)
        adapterSpinnerMoneda.setDropDownViewResource(R.layout.item_spinner)
        binding.smoneda.adapter = adapterSpinnerMoneda

        //Sppiner de la cuenta origen
        val cuentas = mbo?.getCuentas(cliente) as ArrayList<*>
        val cuentasNumero = mutableListOf<String>()

        for (cuenta in cuentas) {
            val cuentaSel = cuenta as Cuenta
            cuentasNumero.add(cuentaSel.getNumeroCuenta()!!.toString())
        }

        //Sppiner cuenta origen
        val adapterSpinnerCOrigen = ArrayAdapter(this, R.layout.item_spinner, cuentasNumero)
        adapterSpinnerCOrigen.setDropDownViewResource(R.layout.item_spinner)
        binding.sCuentasOrigen.adapter = adapterSpinnerCOrigen

        var cuentaSeleccionada: Cuenta = cuentas[0] as Cuenta

        val cuentasNumeroDestino = mutableListOf<String>()

        for (cuenta in cuentas) {
            val cuentaSel = cuenta as Cuenta
            if (!cuentaSeleccionada.getNumeroCuenta().equals(cuentaSel.getNumeroCuenta())) {
                cuentasNumeroDestino.add(cuentaSel.getNumeroCuenta()!!.toString())
            }
        }

        var adapterSpinnerCDestino = ArrayAdapter(this, R.layout.item_spinner, cuentasNumeroDestino)
        adapterSpinnerCDestino.setDropDownViewResource(R.layout.item_spinner)
        binding.sCuentasDestino.adapter = adapterSpinnerCDestino

        var cuentaDestinoSeleccionada: Cuenta = cuentas[1] as Cuenta

        //Cambio del sppiner origen
        binding.sCuentasOrigen.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    cuentaSeleccionada = cuentas[position] as Cuenta

                    //Cambiar el sppiner de la cuenta destino
                    cuentasNumeroDestino.clear()
                    for (cuenta in cuentas) {
                        val cuentaSel = cuenta as Cuenta
                        if (!cuentaSeleccionada.getNumeroCuenta()
                                .equals(cuentaSel.getNumeroCuenta())
                        ) {
                            cuentasNumeroDestino.add(cuentaSel.getNumeroCuenta()!!.toString())
                        }
                    }

                    adapterSpinnerCDestino = ArrayAdapter(
                        this@TransferenciaActivity,
                        R.layout.item_spinner,
                        cuentasNumeroDestino
                    )
                    adapterSpinnerCDestino.setDropDownViewResource(R.layout.item_spinner)
                    binding.sCuentasDestino.adapter = adapterSpinnerCDestino

                    cuentaDestinoSeleccionada = cuentas[1] as Cuenta
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }


        var tipoCuenta: String = "A la cuenta propia:"
        var tipoCuentaSelec: Boolean = true
        //Radio button
        binding.rbGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbOption1 -> {
                    binding.sCuentasDestino.visibility = View.VISIBLE
                    binding.tietNumCuenta.visibility = View.INVISIBLE
                    tipoCuenta = "A la cuenta propia:"
                    tipoCuentaSelec = true
                }

                R.id.rbOption2 -> {
                    binding.sCuentasDestino.visibility = View.INVISIBLE
                    binding.tietNumCuenta.visibility = View.VISIBLE
                    tipoCuenta = "A la cuenta ajena:"
                    tipoCuentaSelec = false
                }
            }
        }

        var destino: String
        //Sppiner de la cuenta destino
        binding.sCuentasDestino.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    cuentaDestinoSeleccionada = cuentas[position] as Cuenta
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        var justificante = "No enviar justificante"
        //CheckBox
        binding.cbJustificante.setOnClickListener {
            if (binding.cbJustificante.isChecked) {
                justificante = "Enviar justificante"
            } else {
                justificante = "No enviar justificante"
            }
        }

        //Botones
        binding.btEnviar.setOnClickListener {
            if (camposValidos(tipoCuentaSelec)) {
                destino = modificarDestino(tipoCuentaSelec, cuentaDestinoSeleccionada.getNumeroCuenta().toString(), binding.tietNumCuenta.text.toString())
                binding.transferencia.isClickable = false
                binding.transferencia.isFocusable = false
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder
                    .setTitle(getString(R.string.transferenciaOK))
                    .setMessage(buildString {
                        append("\n")
                        append("Cuenta origen:\n")
                        append(cuentaSeleccionada.getNumeroCuenta())
                        append("\n")
                        append(tipoCuenta)
                        append("\n")
                        append(destino)
                        append("\n")
                        append("Importe: ")
                        append(binding.tietCantidad.text)
                        append(binding.smoneda.selectedItem)
                        append("\n")
                        append(justificante)
                    })
                    .setPositiveButton("Aceptar") { dialog, which ->
                        finish()
                    }

                val dialog: AlertDialog = builder.create()
                dialog.show()

//                Toast.makeText(this, buildString {
//                    append(getString(R.string.transferenciaOK))
//                    append("\n")
//                    append("Cuenta origen:\n")
//                    append(cuentaSeleccionada.getNumeroCuenta())
//                    append("\n")
//                    append(tipoCuenta)
//                    append("\n")
//                    append(cuentaDestinoSeleccionada.getNumeroCuenta())
//                    append("\n")
//                    append("Importe: ")
//                    append(binding.tietCantidad.text)
//                    append("\n")
//                    append(justificante)
//                }, Toast.LENGTH_SHORT).show()

                //finish()
            } else Toast.makeText(this, getString(R.string.transferenciaNO), Toast.LENGTH_SHORT)
                .show()
        }

        binding.btCancel.setOnClickListener {
            finish()
        }
    }

    private fun camposValidos(tipoCuentaSel: Boolean): Boolean {
        if (binding.tietCantidad.text!!.isNotEmpty()) {
            if (tipoCuentaSel) return true
            else {
                if (binding.tietNumCuenta.text!!.isNotEmpty()) return true
            }
        }

        return false
    }

    private fun modificarDestino(
        tipoCuentaSel: Boolean,
        cuentaPropia: String,
        cuentaAjena: String
    ): String {
        if (tipoCuentaSel) return cuentaPropia
        else return cuentaAjena
    }
}