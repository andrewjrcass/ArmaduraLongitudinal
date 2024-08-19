package com.andrewcass.armaduralongitudinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.andrewcass.armaduralongitudinal.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


enum class TipoAco {CA25, CA50, CA60}

object armLongData { // singleton
    // dados iniciais
    var Mk: Double = 0.0
    var Md: Double= 0.0
    var bw: Double = 0.0
    var H: Double = 0.0
    var Fck : Double = 20.0
    var aco:TipoAco =  TipoAco.CA50
    var GC: Double = 1.4
    var GS: Double = 1.15
    var dl: Double =0.0
    var Ac: Double= 0.0
    var xd_duct: Double= 0.45
    //dados resultados ou calculados
    var fyk: Double= 0.0
    var fyd: Double= 0.0
    var Fcd: Double= 0.0
    var fctm: Double= 0.0
    var fctkSup: Double= 0.0
    var As: Double = 0.0
    var AsC: Double= 0.0 //arm. compressão na arm dupla
    var dmin: Double= 0.0
    var x: Double= 0.0
    var z: Double= 0.0
    var d: Double= 0.0 // altura útil (H-dl)
    var xd:Double= 0.0
    var Eyd: Double=0.0
    var Domain: Int= 0
    var ro: Double= 0.0   //Taxa de armadura mínima
    var w0:Double= 0.0
    var Mdmin:Double= 0.0
    var M2:Double=0.0 // Momento2 compressão arm. dupla
    var M1:Double= 0.0 // Momento1 tração limite para arm. dupla
    var AsMin: Double= 0.0 //Arma. mínima
    var AsMax: Double= 0.0
    var AsAc: Double= 0.0 // taxa de armadura total
}

/*
fun teste(){
    val Prefs: SharedPreferences =  getSharedPreferences("Prefs", Context.MODE_PRIVATE)
    Prefs.edit().putInt("teste", 2).apply()
    val teste = Prefs.getInt("teste", -1)
}
*/

class MainActivity : AppCompatActivity() {
    //lateinit var txtM: TextInputEditText
    lateinit var fButton: Button
    lateinit var btnSair: Button
    lateinit var btnConfig:FloatingActionButton

    //private lateinit var binding: ActivityMainBinding
     val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        fun isTextFilled():Boolean{
            val exit: Boolean/* = false */
            exit=  ((binding.txtM.text.toString() != "") && (binding.txtdck.text.toString()!="")
                    && (binding.txtGamaC.text.toString()!="") && (binding.txtGamaS.text.toString()!="") &&
                    (binding.txtH.text.toString()!="") && (binding.txtbw.text.toString()!="") &&
                    (binding.txtdl.text.toString()!=""))
            return exit
        }
        fun isComputable():Boolean {
            val exit: Boolean /* = false */
            exit = ((armLongData.GS != 0.0) && (armLongData.H != 0.0) && (armLongData.GC != 0.0) &&
                    (armLongData.Fck != 0.0) && (armLongData.bw != 0.0) && (armLongData.Mk != 0.0) &&
                    (armLongData.dl != 0.0))
            return exit
        }

        super.onCreate(savedInstanceState)


        //binding= ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        //setContentView(rbinding.root)
        binding.spinAco.setSelection(1) // defaulkt é aço CA50

        btnConfig= binding.btnConfig
        btnConfig.setOnClickListener{
            val intent= Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }

        btnSair= binding.btnSair
        btnSair.setOnClickListener { finish() }

        fButton= binding.btnCalc
        fButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.strAlert)
            builder.setMessage(R.string.strEmptyFields)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            if (isTextFilled()) {
                //if text fields filled then Get datum
                armLongData.Mk = binding.txtM.text.toString().toDouble() //KN/m^2
                armLongData.Fck = binding.txtdck.text.toString().toDouble()*1000 //KN/m^2
                armLongData.GC = binding.txtGamaC.text.toString().toDouble()
                armLongData.GS = binding.txtGamaS.text.toString().toDouble()
                armLongData.H = binding.txtH.text.toString().toDouble()/100 //(m)
                armLongData.bw = binding.txtbw.text.toString().toDouble()/100 //(m)
                armLongData.dl = binding.txtdl.text.toString().toDouble()/100 //(m)
                when (binding.spinAco.selectedItemPosition) {
                    0 -> { armLongData.aco= TipoAco.CA25
                           armLongData.xd= 0.7709
                           armLongData.Eyd= 0.104
                           armLongData.fyk= 250000.0 //KN/m^2

                    }
                    1 -> { armLongData.aco = TipoAco.CA50
                           armLongData.xd= 0.6283
                           armLongData.Eyd= 0.207
                           armLongData.fyk= 500000.0 //KN/m^2

                    }
                    2 -> { armLongData.aco = TipoAco.CA60
                           armLongData.xd= 0.5900
                           armLongData.Eyd= 0.248
                           armLongData.fyk= 600000.0 //KN/m^2

                    }
                }



                if (isComputable()) {
                    // start results activity if is computable and if is filled
                    val intent = Intent(this, ResultActivity::class.java)
                    startActivity(intent)
                }
            } else {
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                   }

        }



       // Log.i("teste", "${armLongData.aco}")

    }


}