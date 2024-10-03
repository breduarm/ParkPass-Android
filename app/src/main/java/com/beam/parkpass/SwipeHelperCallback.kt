package com.beam.parkpass

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeHelperCallback(private val maxSwipeDistance: Int) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.START or ItemTouchHelper.END
) {
    private var currentScrollX = 0
    private var scrollXWhenInactive = 0
    private var initialXWhenInactive = 0f
    private var isSwipeActive = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false // No drag-and-drop functionality

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // No action needed swipe threshold ensures this method won't be triggered
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
            val itemView = viewHolder.itemView

            when {
                dX == 0f -> { // Initial state when swipe starts
                    currentScrollX = itemView.scrollX
                    isSwipeActive = true
                }
                isCurrentlyActive -> { // Handle manual swipe
                    val scrollOffset = (currentScrollX - dX.toInt()).coerceIn(0, maxSwipeDistance)
                    itemView.scrollTo(scrollOffset, 0)
                }
                else -> { // Handle auto-animation after swipe
                    if (isSwipeActive) {
                        isSwipeActive = false
                        scrollXWhenInactive = itemView.scrollX
                        initialXWhenInactive = dX
                    }
                    if (itemView.scrollX < maxSwipeDistance) {
                        val interpolatedScroll =
                            (scrollXWhenInactive * dX / initialXWhenInactive).toInt()
                        itemView.scrollTo(interpolatedScroll, 0)
                    }
                }
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val itemView = viewHolder.itemView
        val constrainedScrollX = itemView.scrollX.coerceIn(0, maxSwipeDistance)
        itemView.scrollTo(constrainedScrollX, 0)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = Float.MAX_VALUE

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float = Float.MAX_VALUE
}