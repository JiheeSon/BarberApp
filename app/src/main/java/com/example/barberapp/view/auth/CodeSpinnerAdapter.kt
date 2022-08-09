package com.example.barberapp.view.auth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.barberapp.R
import org.json.JSONArray

class CodeSpinnerAdapter(private val context: Context, private val countryList: JSONArray): BaseAdapter() {
    override fun getCount(): Int {
        return countryList.length()
    }

    override fun getItem(p0: Int): Any {
        return countryList.getJSONObject(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertView: View? = p1

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner, p2, false)
        }

        val textCountry = convertView?.findViewById<TextView>(R.id.text_country)
        val textCode = convertView?.findViewById<TextView>(R.id.text_code)

        val jsonObject = countryList.getJSONObject(p0)

        textCountry?.text = jsonObject.getString("code")
        textCode?.text = jsonObject.getString("dial_code")

        return convertView!!

    }
}