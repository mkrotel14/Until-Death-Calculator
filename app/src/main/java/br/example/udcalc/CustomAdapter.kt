package br.example.udcalc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomAdapter(ctx: Context, list: List<Data>) : ArrayAdapter<Data>(ctx, 0, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return myView(position,convertView,parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return myView(position,convertView,parent)
    }

    private fun myView(position: Int, convertView: View?, parent: ViewGroup): View {
        val list = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item,
            parent,
            false
        )

        list?.let {
            val itemText = view.findViewById<TextView>(R.id.tvSpinnerTextItem)
            itemText.text = list.country
        }

        return view
    }
}