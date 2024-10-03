package com.beam.parkpass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.beam.parkpass.databinding.ItemListBinding

class AttractionAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return AttractionViewHolder(view)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    class AttractionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemListBinding.bind(itemView)

        fun bind(attractionName: String) = with(binding) {
            itemTitle.text = attractionName
            removeContainer.setOnClickListener {
                Toast.makeText(this.root.context, "Remove button clicked", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}