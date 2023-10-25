package com.example.planventure.utility

import android.R
import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwipeGesture(context: Context): ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    val deleteColor = ContextCompat.getColor(context,R.color.holo_red_light)
    val archiveColor = ContextCompat.getColor(context,R.color.holo_green_light)
    //val deleteIcon = R.drawable.baseline_delete_24
    //val archiveIcon = R.drawable.baseline_archive_24
    override fun onMove(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas,recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,dX: Float,dY: Float,actionState: Int,isCurrentlyActive: Boolean) {
        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(R.drawable.ic_delete)
            .addSwipeRightBackgroundColor(archiveColor)
            .addSwipeRightActionIcon(R.drawable.ic_lock_lock)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}