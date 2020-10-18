package com.example.ladm_u1_practica2

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.os.Environment
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        guardar.setOnClickListener{
            if (minterna.isChecked){
                if (guardarEnMemoriaInterna()==true){
                    AlertDialog.Builder(this).setTitle("ATENCIÓN")
                        .setMessage("SE GUARDÓ DATA")
                        .setPositiveButton("ok"){d,i->d.dismiss()}
                        .show()
                        textoe.setText("");
                        texto.setText("");
                } else {
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("NO SE GUARDÓ DATA")
                        .setPositiveButton("ok"){d,i->d.dismiss()}
                        .show()
                }
            }
            if (mexterna.isChecked){
                if (guardarEnMemoriaExterna()==true){
                    AlertDialog.Builder(this).setTitle("ATENCIÓN")
                    .setMessage("SE PUDO GUARDAR")
                    .setPositiveButton("ok") { d, i -> d.dismiss() }
                    .show()
                    textoe.setText("");
                    texto.setText("");
                }else{
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("NO SE PUDO GUARDAR")
                        .setPositiveButton("ok") { d, i -> d.dismiss() }
                        .show()
                }
            }
        }
        abrir.setOnClickListener {
            if (minterna.isChecked){
                if (abrirDesdeMemoriaInt().isEmpty()==false){
                    AlertDialog.Builder(this).setTitle("ATENCIÓN")
                        .setMessage("SE LEYÓ LA DATA")
                        .setPositiveButton("ok"){d,i->d.dismiss()}
                        .show()
                } else {
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("ERROR ARCHIVO NO ENCONTRADO")
                        .setPositiveButton("ok"){d,i->d.dismiss()}
                        .show()
                }
            }
            if (mexterna.isChecked){
                if (abrirEnMemoriaExterna().isEmpty()==false){
                    AlertDialog.Builder(this).setTitle("ATENCIÓN")
                        .setMessage("SE LEYÓ LA DATA")
                        .setPositiveButton("ok"){d,i->d.dismiss()}
                        .show()
                } else {
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("ERROR ARCHIVO NO ENCONTRADO")
                        .setPositiveButton("ok"){d,i->d.dismiss()}
                        .show()
                }
            }

        }
    }

    private fun abrirDesdeMemoriaInt(): String {
        var contenido = ""
        try {
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(texto.text.toString())))
            contenido = flujoEntrada.readLine()
            textoe.setText(contenido)
            flujoEntrada.close()
        }catch (io:IOException){
            return ""
        }
        return contenido;
    }

    private fun abrirEnMemoriaExterna() : String {
        var contenido = ""
        try {
            val rutaSD = Environment.getExternalStorageDirectory()
            val archivoEnSD = File(rutaSD.absolutePath,texto.text.toString())
            val flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(archivoEnSD)))
            contenido = flujoEntrada.readLine()
            textoe.setText(contenido)
            flujoEntrada.close()
        }catch (io:IOException){
            return ""
        }
        return contenido
    }

    private fun guardarEnMemoriaExterna(): Boolean {
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ==PackageManager.PERMISSION_GRANTED){
            try {
                if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("NO EXISTE MEMORIA SD INSERTADA")
                        .setPositiveButton("ok") { d, i -> d.dismiss() }
                        .show()
                    return false;
                }
                var rutaSD = Environment.getExternalStorageDirectory()
                var archivoEnSD = File(rutaSD.absolutePath,texto.text.toString())
                var flujoSalida = OutputStreamWriter(FileOutputStream(archivoEnSD))
                flujoSalida.write(textoe.text.toString())
                flujoSalida.flush()
                flujoSalida.close()
            } catch (io:Exception){
                return false;
            }
        }
        return true;
    }

    private fun guardarEnMemoriaInterna():Boolean {
        try {
            var flujoSalida = OutputStreamWriter(openFileOutput(texto.text.toString(),Context.MODE_PRIVATE))
            var data = textoe.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
        }catch (io:IOException){
            return false
        }
        return true;
    }
}