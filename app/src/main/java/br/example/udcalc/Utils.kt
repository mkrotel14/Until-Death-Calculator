package br.example.udcalc

import android.content.Context
import android.util.Log
import java.io.IOException
import java.io.InputStream

fun getAssetJsonData(context: Context): String? {
    var json: String? = null

    json = try {
        val inputS: InputStream = context.assets.open("data.json")
        val size: Int = inputS.available()
        val buffer = ByteArray(size)
        inputS.read(buffer)
        inputS.close()
        String(buffer)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    return json
}