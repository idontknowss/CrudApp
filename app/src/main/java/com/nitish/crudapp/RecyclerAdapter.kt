package com.nitish.crudapp
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(var array:ArrayList<DataClass>,var dataclassInterface: DataClasssInterface):
    RecyclerView.Adapter<RecyclerAdapter.AdapterHolder>()
{
    class AdapterHolder(view: View): RecyclerView.ViewHolder(view) {
        var name: TextView =view.findViewById(R.id.nameofthestudent)
        var number: TextView =view.findViewById(R.id.classnameofthestudent)
        var ids:TextView=view.findViewById(R.id.idofdata)
        var deletebtn:Button=view.findViewById(R.id.deletebtn)
        var updatebtn:Button=view.findViewById(R.id.updatebtn)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder {
        val itemview= LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerlayout,parent,false)
        return AdapterHolder(itemview)
    }

    override fun onBindViewHolder(holder: AdapterHolder, position: Int) {
        holder.name.setText("${array.get(position).nameofthestudent}")
        holder.number.setText("${array.get(position).classnameofstudent}")
        holder.ids.setText("${array.get(position).ids}")
        holder.deletebtn.setOnClickListener {
            dataclassInterface.delete(array.get(position))
            Log.e("Tad", array.get(position).toString())
        }
        holder.updatebtn.setOnClickListener {
            dataclassInterface.update(array.get(position))
            Log.e("Tad", array.get(position).toString())
        }

    }

    override fun getItemCount(): Int {
        return array.size
    }
}