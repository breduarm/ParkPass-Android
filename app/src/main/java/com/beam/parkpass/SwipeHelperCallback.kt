package com.beam.parkpass

import android.content.Context
import android.graphics.Canvas
import android.util.TypedValue
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeHelperCallback(private val maxSwipeDistance: Int) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.START or ItemTouchHelper.END
) {
    private var currentScrollX = 0
    private var currentScrollXWhenInActive = 0
    private var initXWhenInactive = 0f
    private var firstInActive = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // No action needed for drag and drop
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // No action needed
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

            if (dX == 0f) {
                currentScrollX = viewHolder.itemView.scrollX
                firstInActive = true
            }

            if (isCurrentlyActive) {
                // Swipe with finger

                var scrollOffset = currentScrollX + (-dX).toInt()
                if (scrollOffset > maxSwipeDistance) {
                    scrollOffset = maxSwipeDistance
                } else if (scrollOffset < 0){
                    scrollOffset = 0
                }
                viewHolder.itemView.scrollTo(scrollOffset, 0)
            } else {
                // Swipe auto animation

                if (firstInActive) {
                    firstInActive = false
                    currentScrollXWhenInActive = viewHolder.itemView.scrollX
                    initXWhenInactive = dX
                }

                if (viewHolder.itemView.scrollX < maxSwipeDistance) {
                    viewHolder.itemView.scrollTo((currentScrollXWhenInActive * dX / initXWhenInactive).toInt(), 0)
                }
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder.itemView.scrollX > maxSwipeDistance) {
            viewHolder.itemView.scrollTo(maxSwipeDistance, 0)
        } else if (viewHolder.itemView.scrollX < 0) {
            viewHolder.itemView.scrollTo(0, 0)
        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = Float.MAX_VALUE

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float = Float.MAX_VALUE
}