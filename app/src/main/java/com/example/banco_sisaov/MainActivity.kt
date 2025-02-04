package com.example.banco_sisaov

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.banco_sisaov.databinding.ActivityMainBinding
import com.example.banco_sisaov.fragments.AccountsFragment
import com.example.banco_sisaov.fragments.AccountsMovementsFragment
import com.example.banco_sisaov.fragments.CuentaListener
import com.example.banco_sisaov.fragments.Main
import com.example.banco_sisaov.pojo.Cliente
import com.example.banco_sisaov.pojo.Cuenta
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener/*, CuentaListener*/ {

    private lateinit var binding : ActivityMainBinding
    private lateinit var cliente : Cliente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cliente = intent.getSerializableExtra("Cliente") as Cliente
        //val frgAccounts: AccountsFragment = AccountsFragment.newInstance(cliente)
        //frgAccounts.setCuentaListener(this)

        binding.txtWelcome.text = buildString {
            append(getString(R.string.text_bienvenido_a))
            append(" ")
            append(cliente.getNombre())
        }

        binding.btPosGlo.setOnClickListener {
            /*val intent = Intent(this, GlobalPositionActivity::class.java)
            intent.putExtra("Cliente", cliente)
            startActivity(intent)*/
            startPosicion(cliente)
        }

        binding.btMov.setOnClickListener {
            /*val intent = Intent(this, MovementsActivity::class.java)
            intent.putExtra("Cliente", cliente)
            startActivity(intent)*/
            startMovimientos(cliente)
        }

        binding.btTra.setOnClickListener {
            /*val intent = Intent(this, TransferenciaActivity::class.java)
            intent.putExtra("Cliente", cliente)
            startActivity(intent)*/
            startTransaction(cliente)
        }

        binding.btCambioPsw.setOnClickListener {
            /*val intent = Intent(this, ContrasenaActivity::class.java)
            intent.putExtra("Cliente", cliente)
            startActivity(intent)*/
            startPassword(cliente)
        }

        binding.btSalir.setOnClickListener {
            /*val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)*/
            startExit()
        }

        // ************************ Menu lateral ***********************************
        setSupportActionBar(binding.toolbar)

        binding.navView?.setNavigationItemSelectedListener(this)

        val toogle = ActionBarDrawerToggle(this, binding.menuLateral, binding.toolbar, R.string.open_nav, R.string.close_nav)
        binding.menuLateral?.addDrawerListener(toogle)
        toogle.syncState()



        if(savedInstanceState == null) {
            //supportFragmentManager.beginTransaction().replace(R.id.fragContainerMenu, Main()).commit()
            //start activitat main
            binding.navView?.setCheckedItem(R.id.nav_main)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.nav_pg -> startPosicion(cliente)
            R.id.nav_mov -> startMovimientos(cliente)
            R.id.nav_contrasena -> startPassword(cliente)
            R.id.nav_trans -> startTransaction(cliente)
            R.id.nav_salir -> startExit()
        }
        binding.menuLateral?.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.menuLateral!!.isDrawerOpen(GravityCompat.START)) {
            binding.menuLateral?.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /*override fun onCuentaSeleccionada(c: Cuenta) {
        if (c != null) {
            val movementFragment = AccountsMovementsFragment.newInstance(c)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragContainerMenu, movementFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }*/

    fun startPosicion(cliente: Cliente) {
        val intent = Intent(this, GlobalPositionActivity::class.java)
        intent.putExtra("Cliente", cliente)
        startActivity(intent)
    }

    fun startMovimientos(cliente: Cliente) {
        val intent = Intent(this, MovementsActivity::class.java)
        intent.putExtra("Cliente", cliente)
        startActivity(intent)
    }

    fun startTransaction(cliente: Cliente) {
        val intent = Intent(this, TransferenciaActivity::class.java)
        intent.putExtra("Cliente", cliente)
        startActivity(intent)
    }

    fun startPassword(cliente: Cliente) {
        val intent = Intent(this, ContrasenaActivity::class.java)
        intent.putExtra("Cliente", cliente)
        startActivity(intent)
    }

    fun startExit() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}