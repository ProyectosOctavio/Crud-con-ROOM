package com.example.databasekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databasekotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mAdapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setAdapter(list:List<User>){
        mAdapter?.setData(list)

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val userList = AppDatabase(this@MainActivity).getUserDao().getAllUser()

            mAdapter =UserAdapter()
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = mAdapter
                setAdapter(userList)


                    mAdapter?.setOnActionEditListener {

                        val intent = Intent(this@MainActivity, AddActivity::class.java)
                        intent.putExtra("Data", it)
                        startActivity(intent)

                    }
                mAdapter?.setOnActionDeleteListener { userToDelete ->
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setMessage("Estas seguro que quieres eliminarlo?")
                    builder.setPositiveButton(
                        "SI"
                    ) { p0, p1 ->
                        lifecycleScope.launch {
                            AppDatabase(this@MainActivity).getUserDao().deleteUser(userToDelete)
                            val updatedList = AppDatabase(this@MainActivity).getUserDao().getAllUser()
                            setAdapter(updatedList)
                        }
                        p0.dismiss()
                    }
                    builder.setNegativeButton("NO") { p0, p1 ->
                        p0.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                }

                    }
                }

            }
        }

