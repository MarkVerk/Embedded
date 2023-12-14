package com.example.embedded

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DevicesListAdapter(private var listener: Interface, private val names: ArrayList<String>) : RecyclerView.Adapter<DevicesListAdapter.ViewHolder>() {
    interface Interface {
        fun onItemSelected(name: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.device_name2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.device_row, parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.deviceName.text = names[position]
        holder.itemView.setOnClickListener {
            listener.onItemSelected(names[position])
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }
}