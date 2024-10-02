package com.beam.parkpass

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.absoluteValue

class SwipeHelperCallback : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.START or ItemTouchHelper.END
) {
    private val hasBeenSwipedMap = HashMap<Int, Boolean>()
    private var previousStatusMap = HashMap<Int, Boolean>()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // No action needed for drag and drop
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.layoutPosition
        hasBeenSwipedMap[position] = true
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (previousStatusMap[viewHolder.layoutPosition] == null) {
                previousStatusMap[viewHolder.layoutPosition] = isCurrentlyActive
            }

            val itemView = viewHolder.itemView
            val attractionViewHolder = viewHolder as AttractionAdapter.AttractionViewHolder
            val maxSwipeDistance = -attractionViewHolder.getRemoveButtonWidth().toFloat()

            var constrainedDx = dX.coerceAtLeast(maxSwipeDistance)

            when {
                hasBeenSwipedMap[viewHolder.layoutPosition] == true -> {
                    val dXDifference = (1440 - dX.absoluteValue)
                    constrainedDx += dXDifference
                    if (constrainedDx > 0) {
                        itemView.translationX = 0f
                        if (previousStatusMap[viewHolder.layoutPosition] == false && isCurrentlyActive) {
                            hasBeenSwipedMap.remove(viewHolder.layoutPosition)
                        }
                        previousStatusMap[viewHolder.layoutPosition] = isCurrentlyActive
                    } else {
                        itemView.translationX = constrainedDx
                    }
                }
                else -> {
                    itemView.translationX = constrainedDx
                }
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 2f
}