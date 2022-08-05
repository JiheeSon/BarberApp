package com.example.barberapp.view.appointment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.barberapp.R
import com.example.barberapp.model.Constants.BASE_IMAGE_URL
import com.example.barberapp.model.remote.response.barber.Service

class ServiceItemAdapter(private val context: Context, private val serviceList: ArrayList<Service>): BaseAdapter() {
    override fun getCount(): Int {
        return serviceList.size
    }

    override fun getItem(p0: Int): Any {
        return serviceList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertView: View? = p1

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_barber_services, p2, false)
        }

        val service = serviceList[p0]
        convertView?.findViewById<TextView>(R.id.text_service_name)?.text = service.serviceName
        convertView?.findViewById<TextView>(R.id.text_duration)?.text = service.duration.toInt().toString() + " Minutes"
        convertView?.findViewById<TextView>(R.id.text_cost)?.text = service.cost.toString()

        val img = convertView?.findViewById<ImageView>(R.id.img_service)
        Glide.with(context)
            .load(BASE_IMAGE_URL+service.servicePic)
            .into(img!!)

        return convertView!!
    }
}