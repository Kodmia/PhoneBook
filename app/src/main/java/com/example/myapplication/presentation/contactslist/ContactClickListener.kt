package com.example.myapplication.presentation.contactslist

import com.example.myapplication.domain.entinties.Contact

interface ContactClickListener {
    fun contactClicked(contact: Contact)
    fun deleteContact(contact: Contact)
}