package com.rekoj134.moodandhabittracker.util

import com.rekoj134.moodandhabittracker.model.Routine
import com.rekoj134.moodandhabittracker.model.RoutineTask
import com.rekoj134.moodandhabittracker.model.Timeline
import java.time.LocalDate
import java.util.Calendar

object TestDataUtil {
    fun getTestDataForRoutine(): List<Routine> {
        val listRoutines = ArrayList<Routine>()
        listRoutines.add(
            Routine(
                0,
                "Morning routine",
                "Have a good health",
                "2345678",
                Calendar.getInstance().timeInMillis,
                arrayListOf(
                    RoutineTask(0, "Make your bed"),
                    RoutineTask(1, "Reading novel 20 minutes"),
                    RoutineTask(2, "Flirting 25 minutes"),
                    RoutineTask(3, "Listen to music"),
                ),
                Timeline(0, LocalDateUtil.fromDateToString(LocalDate.of(2024, 5, 5)), 15, 22, 32),
                arrayListOf(
                    Timeline(
                        0,
                        LocalDateUtil.fromDateToString(LocalDate.of(2024, 5, 5)),
                        15,
                        22,
                        32
                    ),
                    Timeline(
                        1,
                        LocalDateUtil.fromDateToString(LocalDate.of(2024, 5, 7)),
                        15,
                        22,
                        32
                    ),
                    Timeline(
                        2,
                        LocalDateUtil.fromDateToString(LocalDate.of(2024, 5, 8)),
                        15,
                        22,
                        32
                    ),
                    Timeline(
                        3,
                        LocalDateUtil.fromDateToString(LocalDate.of(2024, 5, 9)),
                        15,
                        22,
                        32
                    )
                )
            )
        )

        listRoutines.add(
            Routine(
                1,
                "Night routine",
                "Have a good sleep",
                "2345678",
                Calendar.getInstance().timeInMillis,
                arrayListOf(
                    RoutineTask(0, "Play game 30 minutes"),
                    RoutineTask(1, "Reading novel 20 minutes"),
                    RoutineTask(2, "Play guqin 30 minutes")
                ),
                Timeline(0, LocalDateUtil.fromDateToString(LocalDate.of(2024, 5, 8)), 15, 22, 32),
                arrayListOf(
                    Timeline(
                        0,
                        LocalDateUtil.fromDateToString(LocalDate.of(2024, 5, 9)),
                        15,
                        22,
                        32
                    ),
                    Timeline(
                        1,
                        LocalDateUtil.fromDateToString(LocalDate.of(2024, 5, 11)),
                        15,
                        22,
                        32
                    ),
                )
            )
        )


        listRoutines.add(
            Routine(
                1,
                "Workout routine",
                "Have a good sleep",
                "2345678",
                Calendar.getInstance().timeInMillis,
                arrayListOf(
                    RoutineTask(0, "Squat"),
                    RoutineTask(1, "Boxing"),
                    RoutineTask(2, "Cadio by run")
                ),
                Timeline(0, LocalDateUtil.fromDateToString(LocalDate.of(2024, 5, 8)), 15, 22, 32),
                arrayListOf(
                    Timeline(
                        0,
                        LocalDateUtil.fromDateToString(LocalDate.of(2024, 5, 9)),
                        15,
                        22,
                        32
                    ),
                    Timeline(
                        1,
                        LocalDateUtil.fromDateToString(LocalDate.of(2024, 5, 11)),
                        15,
                        22,
                        32
                    ),
                )
            )
        )

        return listRoutines
    }
}