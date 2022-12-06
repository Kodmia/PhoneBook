package com.example.myapplication.domain.repository

import com.example.myapplication.data.repository.ContactsListener
import com.example.myapplication.domain.entinties.Contact
import com.example.myapplication.utils.Task

interface PhoneBookService {
    fun loadContacts(): Task<Unit>
    fun getContactById(id: Long): Task<Contact>
    fun deleteContact(contact: Contact): Task<Unit>

    fun addListener(listener: ContactsListener): Unit
    fun removeListener(listener: ContactsListener): Unit
}