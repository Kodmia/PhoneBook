package com.example.myapplication

import com.example.myapplication.domain.entinties.Contact

interface Navigator {
    fun back()

    fun showDetails(contact: Contact)

    fun showToast(message: String)
}