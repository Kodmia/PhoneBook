package com.example.myapplication.presentation.contactslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.App
import com.example.myapplication.data.repository.ContactsListener
import com.example.myapplication.domain.entinties.Contact
import com.example.myapplication.domain.repository.PhoneBookService
import com.example.myapplication.utils.*

class ContactsListViewModel(
    private val phoneBookService: PhoneBookService
): BaseViewModel(), ContactClickListener {

    private val TAG = this::class.java.simpleName

    private val _contacts = MutableLiveData<Result<List<ContactListItem>>>()
    val contacts: LiveData<Result<List<ContactListItem>>> = _contacts

    private val _actionShowToast = MutableLiveData<Event<String>>()
    val actionShowToast: LiveData<Event<String>> = _actionShowToast

    private val _actionContactClicked = MutableLiveData<Event<String>>()
    val actionContactClicked: LiveData<Event<String>> = _actionContactClicked

    private val contactsIdProcessing = mutableSetOf<Long>()

    private var contactsResult: Result<List<Contact>> = EmptyResult()
        set(value){
            field = value
            notifyUpdates()
        }

    private val listener: ContactsListener = {
        contactsResult = if(it.isEmpty()){
            EmptyResult()
        }
        else {
            SuccessResult(it)
        }
    }

    init {
        phoneBookService.addListener(listener)
        loadContacts()
    }

    fun loadContacts(){
        contactsResult = PendingResult()
        phoneBookService.loadContacts()
            .onError {
                contactsResult = ErrorResult(it)
            }
            .autoCancel()
    }

    override fun contactClicked(contact: Contact) {
        Log.d(TAG, "Contact clicked: ${contact.phoneNumber}")
        _actionContactClicked.postValue(
            Event(contact.phoneNumber)
        )
    }

    override fun deleteContact(contact: Contact) {
        if (isInProcessing(contact)) return
        addToProcessing(contact)

        phoneBookService.deleteContact(contact)
            .onSuccess {
                removeFromProcessing(contact)
            }
            .onError {
                removeFromProcessing(contact)
                _actionShowToast.postValue(Event("Can't delete contact"))
            }
            .autoCancel()
    }


    private fun addToProcessing(contact: Contact){
        contactsIdProcessing.add(contact.id)
        notifyUpdates()
    }

    private fun removeFromProcessing(contact: Contact){
        contactsIdProcessing.remove(contact.id)
        notifyUpdates()
    }

    private fun isInProcessing(contact: Contact) = contactsIdProcessing.contains(contact.id)

    private fun notifyUpdates(){
        _contacts.postValue(
            contactsResult.map { contacts ->
                contacts.map { ContactListItem(it, isInProcessing(it)) }
            }
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val phoneBookService = (this[APPLICATION_KEY] as App).phoneBookService

                ContactsListViewModel(
                    phoneBookService = phoneBookService
                )
            }
        }
    }
}