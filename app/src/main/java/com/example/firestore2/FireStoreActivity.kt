package com.example.firestore2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
//            showInputSingerDialog()
            showInputView()
        }
        registerButton.setOnClickListener {
            register()
        }
        cancelButton.setOnClickListener {
            hideInputView()
            // キーボードを隠す処理
            // ２つのEditTextを空にする処理
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
//       val  edit_singer: EditText =customLayout.findViewById(R.id.edit_singer)


        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("singer")
            .setView(editText)
            .setPositiveButton("ok") { _, _ ->
                addSinger(Item().apply {
                    singer = editText.text.toString()
                })
            }
            .show()

    }

    private fun register() {
        val singer = singerEditText.text.toString()
        val singeText = singerTextEditText.text.toString()

        if (singer.isEmpty()) {
            Toast.makeText(this, "歌手名を入れてください", Toast.LENGTH_SHORT).show()
            return
        }

        val item = Item().apply {
            this.singer = singer
            this.text2 = singeText
        }
        addSinger(item)
        hideInputView()
    }

    private fun addSinger(item: Item) {
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name

        // Create a new user with a first and last name
//                val singer: MutableMap<String, Any> = HashMap()
//                singer["singer"] = editText.text.toString()

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

    private fun initData() {
        FirebaseFirestore.getInstance()
            .collection("singer")
            .get()
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    customAdapter.refresh(listOf())
                    return@addOnCompleteListener
                }
                it.result?.toObjects(Item::class.java)?.also { items ->
                    customAdapter.refresh(items)
                } ?: run {
                    customAdapter.refresh(listOf())
                }
                initObserver()
            }
            .addOnFailureListener {
                    e -> Log.w("FireStoreActivity", "Error get document", e)
            }
    }

    private fun initObserver() {
        Log.d("FireStoreActivity", "initObserver")
        FirebaseFirestore.getInstance()
            .collection("singer")
            .orderBy(Item::createdAt.name, Query.Direction.DESCENDING)
            .limit(1L)
            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w("FireStoreActivity", "error:$firebaseFirestoreException")
                    return@addSnapshotListener
                }
                snapshot?.toObjects(Item::class.java)?.also {
                    if (it.isEmpty())
                        return@addSnapshotListener
                    it.forEach { item ->
                        Log.d("initObserver", "$item")
                    }
                    customAdapter.add(it.first())
                }
            }
    }

    private fun showInputView() {
        changeInputViewVisible(true)
    }

    private fun hideInputView() {
        changeInputViewVisible(false)
    }

    private fun changeInputViewVisible(isVisible: Boolean) {
        inputView.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }
}

