package com.beam.parkpass

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import com.beam.parkpass.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initRecyclerView()
        attachSwipeHelperToRecyclerView()
    }

    private fun initRecyclerView() {
        val attractions = getAttractions()
        val attractionAdapter = AttractionAdapter(attractions)
        binding.attractionList.adapter = attractionAdapter
    }

    private fun attachSwipeHelperToRecyclerView() {
        val itemTouchHelper = ItemTouchHelper(SwipeHelperCallback())
        itemTouchHelper.attachToRecyclerView(binding.attractionList)
    }
}