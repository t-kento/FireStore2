package com.example.firestore2

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fire_main.*
import kotlinx.android.synthetic.main.firestore_activity.*

class FireStoreActivity :AppCompatActivity(){

    private val customAdapter by lazy { CustomAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fire_main)
        initialize()
    }

    private fun initialize(){
        initLayout()
    }

    private fun initLayout(){
        initClick()
        initRecyclerView()

    }

    private fun initClick(){
        add.setOnClickListener{
            addSinger()
        }
    }
    private fun initRecyclerView() {
        customAdapter.callback = object : CustomAdapter.CustomAdapterCallback {
            override fun onClick(data: Item) {
            }
        }
        memoRecyclerView?.apply {
            adapter = customAdapter
            layoutManager = LinearLayoutManager(this@FireStoreActivity)
        }
    }
    private fun addSinger() {
        val customLayout = layoutInflater.inflate(R.layout.firestore_activity, null)
       val  edit_singer: EditText =customLayout.findViewById(R.id.edit_singer)

        AlertDialog.Builder(this)
            .setTitle("singer")
            .setView(edit_singer)
            .setPositiveButton("ok") { _, _ ->
                val db = FirebaseFirestore.getInstance()
                // Create a new user with a first and last name

                // Create a new user with a first and last name
                val singer: MutableMap<String, Any> = HashMap()
                singer["singer"] = "$edit_singer"

                db.collection("singer")
                    .add(singer)
                    .addOnSuccessListener { documentReference ->
                        Log.d(
                            "FireStoreActivity",
                            "DocumentSnapshot added with ID: " + documentReference.id
                        )
                    }
                    .addOnFailureListener { e -> Log.w("FireStoreActivity", "Error adding document", e) }
            }
            .show()

    }

}

