package com.example.moodtracker.ui.home.worries

import org.threeten.bp.LocalDateTime

interface WorryItemClickListener {

    fun onItemClick(worryDate : LocalDateTime)
    fun onItemClickComplete()

}