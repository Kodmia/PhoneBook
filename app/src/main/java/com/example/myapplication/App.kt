package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.repository.PhoneBookDemoService
import com.example.myapplication.domain.repository.PhoneBookService
import kotlin.properties.Delegates

class App: Application() {
    var phoneBookService: PhoneBookService by Delegates.notNull<PhoneBookService>()

    override fun onCreate() {
        super.onCreate()
        initPhoneBookService()
    }

    private fun initPhoneBookService(){
        phoneBookService = PhoneBookDemoService()
    }
}