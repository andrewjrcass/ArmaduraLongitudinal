package com.andrewcass.armaduralongitudinal

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlin.math.sqrt


var  lineStr= ""
var finishStr= ""
var isArmDupla= false
lateinit var myContext: Context
/* var strDomain= "" */
var strInitialData= ""


fun GetDomain(): Int {
    val x23: Double//= 0.0
    val x34: Double//= 0.0
    val x: Double//= 0.0
    val domain: Int//= 0

    x23= 0.259*armLongData.d
    x34= armLongData.xd*armLongData.d
    x= armLongData.x

    if ( x == 0.0) {domain= 0}
    else if (x < x23) { domain= 2 }
    else if (x < x34) { domain= 3 }
    else { domain= 4}

    armLongData.Domain= domain
    return domain
    }

fun GetDmin():Double{
 return  sqrt(armLongData.Md/(armLongData.bw*armLongData.Fcd*(
                                 0.68*armLongData.xd_duct-0.272*Math.pow(armLongData.xd_duct,2.0)
                                                                )
                                )
             )
}

fun GetArMin(): Double{
    val Fck: Double//= 0.0
    val ro: Double//= 0.0
    val Asmin: Double//=0.0
    Fck= armLongData.Fck

    when (Fck) {
        in 0.0..35000.0 -> {ro= 0.150}
        in 35000.0..40000.0 -> {ro= 0.164}
        in 40000.0..45000.0 -> {ro= 0.179}
        in 45000.0..50000.0 -> {ro= 0.194}
        in 50000.0..55000.0 -> {ro= 0.208}
        in 55000.0..60000.0 -> {ro= 0.211}
        in 60000.0..65000.0 -> {ro= 0.219}
        in 65000.0..70000.0 -> {ro= 0.226}
        in 70000.0..75000.0 -> {ro= 0.233}
        in 75000.0..80000.0 -> {ro= 0.239}
        in 80000.0..85000.0 -> {ro= 0.245}
        in 85000.0..90000.0 -> {ro= 0.251}
        else -> {ro= 0.256}
    }

    Asmin=armLongData.Ac*ro/100

    armLongData.ro= ro // pega taxa de arm. mínima
    armLongData.AsMin= Asmin
 return Asmin
}

fun GetRootX():Double{
    val x1: Double
    val x2: Double
    var x = 0.0
    x1= (0.68*armLongData.d + sqrt(Math.pow(0.68*armLongData.d, 2.0)-4*0.272*(armLongData.Md/(armLongData.bw*armLongData.Fcd))))/0.544
    x2= (0.68*armLongData.d - sqrt(Math.pow(0.68*armLongData.d, 2.0)-4*0.272*(armLongData.Md/(armLongData.bw*armLongData.Fcd))))/0.544

    if (x1 in 0.0..armLongData.d) {
        x= x1
    } else if (x2 in 0.0..armLongData.d){
        x= x2
    }
    return x
    //x1:= (0.68*d+sqrt(sqr(0.68*d)-4*0.272*(Md/(bw*Fcd))))/0.544;
    //x2:= (0.68*d-sqrt(sqr(0.68*d)-4*0.272*(Md/(bw*Fcd))))/0.544;
}

fun PrintResults(rMemo: TextView){
    lineStr= myContext.getString(/* resId = */ R.string.strLine)
    finishStr= myContext.getString(/* resId = */ R.string.strFinish)
    rMemo.text= strInitialData
    rMemo.append(" Mk: ${armLongData.Mk} (KN.m); Md: ${"%.2f".format(armLongData.Md)} (KN.m);\n")
    rMemo.append(" Gama S: ${armLongData.GS}; Gama C: ${armLongData.GC}\n" +
            " bw: ${armLongData.bw} (m), H: ${armLongData.H} (m);\n")
    rMemo.append(" d': ${"%.3f".format(armLongData.dl)} (m); d: ${"%.3f".format(armLongData.d)} (m)\n" +
            " Ac: ${"%.2f".format(armLongData.Ac)} (m^2)\n")
    rMemo.append(" fck/fcd: ${armLongData.Fck/1000}/${"%.2f".format(armLongData.Fcd/1000)} (MPa)" + lineStr+"\n")
    rMemo.append(" Tipo de aço: ${armLongData.aco}\n" +
            " fyk/fyd: ${armLongData.fyk/1000}/${"%.2f".format(armLongData.fyd/1000)} (MPa)\n")
    rMemo.append(" x/d(lim.3/4): ${armLongData.xd}, Eyd: ${armLongData.Eyd}")
    rMemo.append(lineStr +
            "\n x: ${"%.3f".format(armLongData.x)} (m), z: ${"%.3f".format(armLongData.z)} (m)\n")
    rMemo.append(" coef. duct: ${armLongData.xd_duct}, x/d: ${"%.3f".format(armLongData.x/armLongData.d)}\n")
    rMemo.append(" tx arm. min.: ${"%.3f".format(armLongData.ro)}%\n" +
            " As.min: ${"%.2f".format(armLongData.AsMin*10000)} (cm^2)\n" +
            " As.max(4% Ac): ${"%.2f".format(armLongData.AsMax)} (cm^2)\n" +
            " As/Ac: ${"%.3f".format(armLongData.AsAc*100.0)} %\n" +
            " Md.min: ${"%.2f".format(armLongData.Mdmin*1000)} (KN.m)\n" +
            " d.min: ${"%.3f".format(armLongData.dmin)} (m) ")
    rMemo.append("As: ${"%.2f".format(armLongData.As*10000)} (cm^2), Domínio: ${GetDomain()}\n")

    if (isArmDupla) {
            // isArmDupla= false default
        rMemo.append(" d.min > d ->\n" + myContext.getString(R.string.strArmDupAlert))
    } else {

        rMemo.append(" d.min < d ->\n " + myContext.getString(R.string.strArmSimples))
    }
    rMemo.append(finishStr)
}

fun CalcAS1(){
    armLongData.Md= armLongData.GC * armLongData.Mk
    armLongData.d= armLongData.H - armLongData.dl
    armLongData.Fcd= armLongData.Fck/armLongData.GC
    armLongData.fyd= armLongData.fyk/armLongData.GS
    armLongData.Ac= armLongData.H * armLongData.bw
    if (armLongData.Fck >= 50000.0) {armLongData.xd_duct= 0.35} // Coef. ductibilidade
    else {armLongData.xd_duct= 0.45}
    armLongData.fctm= 0.30*Math.pow(armLongData.Fck/1000, 2.0/3.0)
    armLongData.fctkSup= 1.30*armLongData.fctm
    armLongData.w0= armLongData.bw*Math.pow(armLongData.H, 2.0)/6.0
    armLongData.Mdmin= 0.8*armLongData.fctkSup*armLongData.w0
    armLongData.x= GetRootX()
    armLongData.z= armLongData.d-0.40*armLongData.x
    armLongData.AsMax= armLongData.Ac*0.04*10000
    armLongData.AsMin= GetArMin()
    //GetRootX()
    armLongData.dmin= GetDmin()
    armLongData.As= armLongData.Md/(armLongData.z*armLongData.fyd)
    armLongData.AsAc= armLongData.As/armLongData.Ac
    isArmDupla= armLongData.dmin >= armLongData.d
}

fun CalcAS2(rMemo: TextView, txtAs2:TextView, txtAs1: TextView){
    rMemo.text=""  // clear result memo
    //with(armLongData){}
    armLongData.M1= 0.251*armLongData.bw*armLongData.Fcd*Math.pow(armLongData.d,2.0)
    armLongData.M2= armLongData.Md-armLongData.M1
    armLongData.As= armLongData.M1/((1-0.4*armLongData.xd_duct)*armLongData.d*armLongData.fyd) +
                    (armLongData.Md-armLongData.M1)/((armLongData.d-armLongData.dl)*armLongData.fyd)
    armLongData.AsC= armLongData.M2/((armLongData.d-armLongData.dl)*armLongData.fyd)
    armLongData.AsAc= (armLongData.As + armLongData.AsC)/armLongData.Ac
    PrintResults(rMemo) // deve ser aqui depois dos cálculos!
    rMemo.append("M1: ${"%.2f".format(armLongData.M1)} (KN.m) (tração)\n")
    rMemo.append("M2: ${"%.2f".format(armLongData.M2)} (KN.m) (compressão)\n")

    txtAs1.isVisible= true
    txtAs1.text= "As: ${"%.2f".format(armLongData.As*10000)} (cm^2) | (<->)"
    txtAs2.isVisible= true
    txtAs2.text= "As': ${"%.2f".format(armLongData.AsC*10000)} (cm^2) | (>-<)"
}

class ResultActivity : AppCompatActivity() {
    lateinit var rButton: Button
    lateinit var btnArmDupla: Button
    lateinit var rMemo: TextView
    lateinit var txtAS1: TextView
    lateinit var txtAS2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        myContext= this
        rMemo= findViewById(R.id.memoRes)
        txtAS1= findViewById(R.id.txtAS1)
        txtAS2= findViewById(R.id.txtAS2)
        btnArmDupla= findViewById(R.id.btnCalcArmDupla)
        btnArmDupla.isVisible= false
        rButton= findViewById(R.id.btnBack)

        rButton.setOnClickListener {
            super.finish()
        }
        btnArmDupla.setOnClickListener {
           CalcAS2(rMemo, txtAS2, txtAS1)
        }

        txtAS1.text=" "
        txtAS2.text=" "
        txtAS2.isVisible= false
        txtAS1.isVisible= false
        strInitialData =  getString(R.string.strInitialData)

        CalcAS1()
        PrintResults(rMemo)
        btnArmDupla.isVisible= isArmDupla


        txtAS1.text= "As: ${"%.2f".format(armLongData.As*10000)} (cm^2)"
        txtAS1.isVisible= true
        if (isArmDupla) {
            txtAS1.text= "Requer armadura de compressão"
            txtAS2.text= "     (Armadura dupla)!"
            txtAS2.isVisible= true
        }

    }

}