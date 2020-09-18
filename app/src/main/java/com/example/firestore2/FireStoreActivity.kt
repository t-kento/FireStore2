package com.example.firestore2

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.okhttp.internal.DiskLruCache
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
        initData()
    }

    private fun initLayout(){
        initClick()
        initRecyclerView()

    }

    private fun initClick(){
        add.setOnClickListener{
            showInputSingerDialog()
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
    private fun showInputSingerDialog() {
        val customLayout = layoutInflater.inflate(R.layout.firestore_activity, null)
       //val  edit_singer: EditText =customLayout.findViewById(R.id.edit_singer)

        val edit_singer=EditText(this)

        AlertDialog.Builder(this)
            .setTitle("singer")
            .setView(edit_singer)
            .setPositiveButton("ok") { _, _ ->
                addSinger(edit_singer.text.toString())
            }
            .show()

    }
    private fun addSinger(singerName:String){
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name

        // Create a new user with a first and last name
        val item = Item().apply {
            this.editSinger=singerName
        }

        db.collection("singer")
            .add(item)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "FireStoreActivity",
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w("FireStoreActivity", "Error adding document", e) }
    }

    private fun initData(){
        FirebaseFirestore.getInstance()
            .collection("singer")
            .get()
            .addOnCompleteListener() {
                if (!it.isSuccessful){
                    customAdapter.refresh(listOf())
                    return@addOnCompleteListener
                }
                it.result?.toObjects(Item::class.java)?.also {
                    customAdapter.refresh(it)
                }?:run{
                    customAdapter.refresh(listOf())
                }
                intiObserver()
            }
            .addOnFailureListener{
                    e -> Log.w("FireStoreActivity", "Error adding document", e)
            }
    }

    private fun intiObserver(){
        FirebaseFirestore.getInstance()
            .collection("singer")
            .orderBy(Item::createdAt.name,Query.Direction.DESCENDING)
            .limit(1L)
            .addSnapshotListener{snapshot,firebaseFirestoreException->
                if(firebaseFirestoreException !=null){
                    Log.w("FireStoreActivity","error:firebaseFirestoreException")
                    return@addSnapshotListener
                }
                snapshot?.toObjects(Item::class.java)?.also{
                    if(it.isEmpty())
                        return@addSnapshotListener
                    it.forEach{item ->
                        Log.d("initObserver","$item")
                    }
                    customAdapter.add(it.first())

                }


            }
    }

}

