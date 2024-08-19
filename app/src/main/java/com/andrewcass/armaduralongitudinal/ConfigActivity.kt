package com.andrewcass.armaduralongitudinal

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
// binding import
import com.andrewcass.armaduralongitudinal.databinding.ActivityConfigBinding

class ConfigActivity : AppCompatActivity() {

    lateinit var btnSair: Button
    lateinit var btnsave: Button

    // create binding object
    val binding by lazy {
        ActivityConfigBinding.inflate(layoutInflater)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_config)
        supportActionBar?.setTitle("kjhgjhgjhg")
        //Set binding contents
        setContentView(binding.root)
        //btnsave=findViewById(R.id.btnConfigSave)
        btnsave= binding.btnConfigSave
        btnsave.setOnClickListener {
            //save data defaults
        }

        //btnSair= findViewById(R.id.btnConfigSair)

        btnSair= binding.btnConfigSair
        btnSair.setOnClickListener { finish() }

    }
}