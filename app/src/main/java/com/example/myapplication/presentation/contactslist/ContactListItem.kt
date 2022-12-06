package com.example.myapplication.presentation.contactslist

import com.example.myapplication.domain.entinties.Contact

data class ContactListItem(
    val contact: Contact,
    val inProgress: Boolean
)
