package com.rekoj134.moodandhabittracker.epoxy.controller

import android.content.Context
import android.widget.Toast
import com.airbnb.epoxy.EpoxyController
import com.rekoj134.moodandhabittracker.itemRoutineTaskCustom
import com.rekoj134.moodandhabittracker.model.RoutineTask

class RoutineTasksController : EpoxyController() {
    private val listTask by lazy { ArrayList<RoutineTask>() }
    private var context: Context? = null

    fun setListData(listData: ArrayList<RoutineTask>) {
        this.listTask.clear()
        this.listTask.addAll(listData)
    }

    fun getListData() : ArrayList<RoutineTask> {
        return listTask
    }

    override fun buildModels() {
        listTask.forEach {
            itemRoutineTaskCustom {
                id(it.id)
                task(it.taskName)
                onClickItem { _ ->
                    this@RoutineTasksController.context?.let {
                        Toast.makeText(it, "Click Item", Toast.LENGTH_SHORT).show()
                    }
                }
                onClickDelete { _ ->
                    this@RoutineTasksController.context?.let {
                        Toast.makeText(it, "Click Delete", Toast.LENGTH_SHORT).show()
                    }
                    this@RoutineTasksController.listTask.remove(it)
                    this@RoutineTasksController.requestModelBuild()
                }
            }
        }
    }
}