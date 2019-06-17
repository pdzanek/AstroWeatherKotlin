package com.example.astroweather

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ForecastItem
import android.annotation.SuppressLint
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.adapter_view_layout.view.*

class ForecastAdapter(val context: Context,val forecastList: List<ForecastItem>): RecyclerView.Adapter<ForecastAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_view_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.setData(forecast,position)
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun setData(forecast: ForecastItem, pos:Int){
            itemView.tvDt_txt.text=forecast.dt_txt
            itemView.tvTemperature.text=forecast.main.temp.toString()+" C"
            itemView.tvHumidity.text=forecast.main.humidity.toString()+" %"
            itemView.tvPressure.text=forecast.main.pressure.toString()+" hPa"
        }
    }
}