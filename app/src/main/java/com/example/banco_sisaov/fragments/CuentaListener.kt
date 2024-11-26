package com.example.banco_sisaov.fragments

import com.example.banco_sisaov.pojo.Cuenta

interface CuentaListener {
    fun onCuentaSeleccionada(c: Cuenta)
}