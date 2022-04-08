package com.nitish.crudapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nitish.crudapp.databinding.ActivityMainBinding
import com.nitish.crudapp.databinding.DeletedataBinding
import com.nitish.crudapp.databinding.DialoglayoutBinding
import com.nitish.crudapp.databinding.RecyclerlayoutBinding

class MainActivity : AppCompatActivity(), DataClasssInterface {
    lateinit var binding: ActivityMainBinding
    lateinit var recyclerAdapter: RecyclerAdapter
    lateinit var arrayList: ArrayList<DataClass>
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var builder: AlertDialog
    lateinit var builderofdeletedata: AlertDialog
    lateinit var bind: DialoglayoutBinding
    lateinit var deletedatalayout: DeletedataBinding

    val db = Firebase.firestore

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        arrayList = ArrayList()
        binding.nodata.visibility = View.GONE
        //showing recyclerlist from database
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.equals(null)) {
                        binding.progressBar.visibility = View.GONE
                        binding.nodata.visibility = View.VISIBLE
                    } else {
                        binding.nodata.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        val dataClass = DataClass(
                            document.getString("first").toString(),
                            document.getString("last").toString(),
                            document.id
                        )
                        arrayList.add(dataClass)
                        recyclerAdapter.notifyDataSetChanged()
                    }

                    // Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                //Log.w(TAG, "Error getting documents.", exception)
            }
        //by showData function
        recyclerAdapter = RecyclerAdapter(arrayList, this)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = recyclerAdapter
        //add data progressbar
        val recyclerlayoutBinding: RecyclerlayoutBinding =
            RecyclerlayoutBinding.inflate(layoutInflater)
        bind = DialoglayoutBinding.inflate(layoutInflater)
        builder = AlertDialog.Builder(this).create()
        bind.progressBar2.visibility = View.GONE
        deletedatalayout = DeletedataBinding.inflate(layoutInflater)
        builderofdeletedata = AlertDialog.Builder(this).create()
        deletedatalayout.progressBar2.visibility = View.GONE
        bind.cancelbtn.setOnClickListener {
            builder.dismiss()
        }
        builder.setView(bind.root)
        builderofdeletedata.setView(deletedatalayout.root)
        builder.setView(bind.root)
        bind.button.setOnClickListener {
            val buttonname = bind.button.text.toString()
            if (buttonname.equals("Save Data")) {
                val nameofstudent = bind.studentname.text.toString().trim()
                val nameofclass = bind.classname.text.toString().trim()
                val user = hashMapOf(
                    "first" to nameofstudent,
                    "last" to nameofclass
                )
                if (TextUtils.isEmpty(bind.studentname.text.toString().trim())) {
                    bind.studentname.setError("Enter Name")
                } else if (TextUtils.isEmpty(bind.classname.text.toString().trim())) {
                    bind.classname.setError("Enter Class")
                } else {
                    bind.progressBar2.visibility = View.VISIBLE
                    bind.button.visibility = View.GONE
                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            bind.button.visibility = View.VISIBLE
                            bind.progressBar2.visibility = View.GONE
                            builder.dismiss()
                            Toast.makeText(this, "Data Added Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnFailureListener { e ->
                            bind.button.visibility = View.VISIBLE
                            bind.progressBar2.visibility = View.GONE
                            Toast.makeText(this, "Data Added Failed", Toast.LENGTH_SHORT).show()
                        }
                    arrayList.clear()
                    binding.progressBar.visibility = View.VISIBLE
                    db.collection("users")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                if (document.equals(null)) {
                                    binding.progressBar.visibility = View.GONE
                                    binding.nodata.visibility = View.VISIBLE
                                } else {
                                    binding.nodata.visibility = View.GONE
                                    binding.progressBar.visibility = View.GONE
                                    val dataClass = DataClass(
                                        document.getString("first").toString(),
                                        document.getString("last").toString(),
                                        document.id
                                    )
                                    arrayList.add(dataClass)
                                    recyclerAdapter.notifyDataSetChanged()
                                }
                                // Log.d(TAG, "${document.id} => ${document.data}")
                            }
                        }
                        .addOnFailureListener { exception ->
                            //Log.w(TAG, "Error getting documents.", exception)
                        }

                }
            }

        }



        recyclerlayoutBinding.deletebtn.setOnClickListener {
            Toast.makeText(this, "Delete Button Clicked", Toast.LENGTH_SHORT).show()
            db.collection("users").document(bind.textView.toString()).delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show()
                    arrayList.clear()
                    binding.progressBar.visibility = View.VISIBLE
                    db.collection("users")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                if (document.equals(" ")) {
                                    binding.progressBar.visibility = View.GONE
                                    binding.nodata.visibility = View.VISIBLE
                                } else {
                                    binding.nodata.visibility = View.GONE
                                    binding.progressBar.visibility = View.GONE
                                    val dataClass = DataClass(
                                        document.getString("first").toString(),
                                        document.getString("last").toString(),
                                        document.getString("ids").toString()
                                    )
                                    arrayList.add(dataClass)
                                }
                                // Log.d(TAG, "${document.id} => ${document.data}")
                            }
                            recyclerAdapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener { exception ->
                            //Log.w(TAG, "Error getting documents.", exception)
                        }
                }

        }
        bind.cancelbtn.setOnClickListener {
            bind.button.visibility=View.VISIBLE
            bind.progressBar2.visibility=View.GONE
            builder.dismiss()
        }
        binding.floatingActionButton.setOnClickListener {
            bind.classname.setText(null)
            bind.studentname.setText(null)
            bind.textView.setText("Add Data To Database")
            bind.button.setText("Save Data")
            builder.show()


        }
    }


    override fun delete(dataClass: DataClass) {
        builderofdeletedata.show()
        deletedatalayout.button.setOnClickListener {
            deletedatalayout.progressBar2.visibility = View.VISIBLE
            deletedatalayout.button.visibility = View.GONE
            db.collection("users").document(dataClass.ids).delete().addOnSuccessListener {
                deletedatalayout.progressBar2.visibility = View.GONE
                deletedatalayout.button.visibility = View.VISIBLE
                builderofdeletedata.dismiss()
                Toast.makeText(this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show()
                arrayList.clear()
                binding.progressBar.visibility = View.VISIBLE
                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            if (document.equals(" ")) {
                                binding.progressBar.visibility = View.GONE
                                binding.nodata.visibility = View.VISIBLE
                            } else {

                                binding.nodata.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                                val dataClass = DataClass(
                                    document.getString("first").toString(),
                                    document.getString("last").toString(),
                                    document.id
                                )
                                arrayList.add(dataClass)
                            }
                            // Log.d(TAG, "${document.id} => ${document.data}")
                        }
                        recyclerAdapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { exception ->
                        deletedatalayout.progressBar2.visibility = View.GONE
                        deletedatalayout.button.visibility = View.VISIBLE
                        Toast.makeText(this, "Somethingwent Wrong", Toast.LENGTH_SHORT).show()
                        //Log.w(TAG, "Error getting documents.", exception)
                    }
            }
        }
        deletedatalayout.cancelbtn.setOnClickListener {
            deletedatalayout.button.visibility = View.VISIBLE
            deletedatalayout.progressBar2.visibility = View.GONE
            builderofdeletedata.dismiss()
        }

    }

    override fun update(dataClass: DataClass) {
        bind.textView.setText("Update Data To Database")
        bind.button.setText("Update Data")
        bind.classname.setText(dataClass.classnameofstudent)
        bind.studentname.setText(dataClass.nameofthestudent)
        builder.show()
        val nameofstudents = bind.studentname.text.toString().trim()
        val nameofclasss = bind.classname.text.toString().trim()
        val datas = hashMapOf(
            "first" to nameofstudents,
            "last" to nameofclasss
        )
        bind.button.setOnClickListener {
            val buttonname = bind.button.text.toString()
            if (buttonname.equals("Update Data")) {
                db.collection("users").document(dataClass.ids).set(datas).addOnSuccessListener {
                    deletedatalayout.progressBar2.visibility = View.GONE
                    deletedatalayout.button.visibility = View.VISIBLE
                    builderofdeletedata.dismiss()
                    Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show()
                    arrayList.clear()
                    binding.progressBar.visibility = View.VISIBLE
                    db.collection("users")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                if (document.equals(" ")) {
                                    binding.progressBar.visibility = View.GONE
                                    binding.nodata.visibility = View.VISIBLE
                                } else {

                                    binding.nodata.visibility = View.GONE
                                    binding.progressBar.visibility = View.GONE
                                    val dataClass = DataClass(
                                        document.getString("first").toString(),
                                        document.getString("last").toString(),
                                        document.id
                                    )
                                    arrayList.add(dataClass)
                                }
                                // Log.d(TAG, "${document.id} => ${document.data}")
                            }
                            recyclerAdapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener { exception ->
                            deletedatalayout.progressBar2.visibility = View.GONE
                            deletedatalayout.button.visibility = View.VISIBLE
                            Toast.makeText(this, "Somethingwent Wrong", Toast.LENGTH_SHORT).show()
                            //Log.w(TAG, "Error getting documents.", exception)
                        }
                }
            } else {
                val nameofstudent = bind.studentname.text.toString().trim()
                val nameofclass = bind.classname.text.toString().trim()
                val user = hashMapOf(
                    "first" to nameofstudent,
                    "last" to nameofclass
                )
                if (TextUtils.isEmpty(bind.studentname.text.toString().trim())) {
                    bind.studentname.setError("Enter Name")
                } else if (TextUtils.isEmpty(bind.classname.text.toString().trim())) {
                    bind.classname.setError("Enter Class")
                } else {
                    bind.progressBar2.visibility = View.VISIBLE
                    bind.button.visibility = View.GONE
                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            bind.button.visibility = View.VISIBLE
                            bind.progressBar2.visibility = View.GONE
                            builder.dismiss()
                            Toast.makeText(this, "Data Added Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnFailureListener { e ->
                            bind.button.visibility = View.VISIBLE
                            bind.progressBar2.visibility = View.GONE
                            Toast.makeText(this, "Data Added Failed", Toast.LENGTH_SHORT).show()
                        }
                    arrayList.clear()
                    binding.progressBar.visibility = View.VISIBLE
                    db.collection("users")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                if (document.equals(null)) {
                                    binding.progressBar.visibility = View.GONE
                                    binding.nodata.visibility = View.VISIBLE
                                } else {
                                    binding.nodata.visibility = View.GONE
                                    binding.progressBar.visibility = View.GONE
                                    val dataClass = DataClass(
                                        document.getString("first").toString(),
                                        document.getString("last").toString(),
                                        document.id
                                    )
                                    arrayList.add(dataClass)
                                    recyclerAdapter.notifyDataSetChanged()
                                }
                                // Log.d(TAG, "${document.id} => ${document.data}")
                            }
                        }
                        .addOnFailureListener { exception ->
                            //Log.w(TAG, "Error getting documents.", exception)
                        }

                }

            }


        }
    }
}