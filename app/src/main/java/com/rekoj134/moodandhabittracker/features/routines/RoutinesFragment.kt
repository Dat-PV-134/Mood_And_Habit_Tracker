package com.rekoj134.moodandhabittracker.features.routines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rekoj134.moodandhabittracker.base.BaseFragment
import com.rekoj134.moodandhabittracker.databinding.FragmentRoutinesBinding
import com.rekoj134.moodandhabittracker.itemRoutine
import com.rekoj134.moodandhabittracker.model.Routine

class RoutinesFragment : BaseFragment() {
    private var _binding: FragmentRoutinesBinding? = null
    private val binding get() = _binding!!

    private val listRoutines = ArrayList<Routine>()
    private val listOfExpand = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setupView()
        handleEvent()
    }

    private fun handleEvent() {

    }

    private fun setupView() {
        binding.rvRoutines.withModels {
            listRoutines.forEach {
                itemRoutine {
                    id(it.id)
                    routineName(it.routineName)
                    routineExpand(it.routineTasks)
                    onClickExpand { _ ->
                        if (listOfExpand.contains(it.id)) listOfExpand.remove(it.id)
                        else listOfExpand.add(it.id)
                        binding.rvRoutines.requestModelBuild()
                    }
                    isExpand(listOfExpand.contains(it.id))
                    data(arrayListOf("Exercises", "Eat", "Drink"))
                }
            }
        }
    }

    private fun initData() {
        listRoutines.add(Routine(0, "Morning Routine", "Have a good health", "2345678", 0, "Exercises&*Eat&*DrinkCoffee","05/05/2024", "05/05/2024&*06/05/2024"))
        listRoutines.add(Routine(1, "Eating Routine", "Have a good health", "2345678", 0, "Exercises&*Eat&*DrinkCoffeeawffafwfafawfawfafafwafwfawfwa","05/05/2024", "05/05/2024&*06/05/2024"))
        listRoutines.add(Routine(2, "Night Routine", "Have a good health", "2345678", 0, "Exercises&*Eat&*DrinkCoffeeawdafwaf","05/05/2024", "05/05/2024&*06/05/2024"))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}