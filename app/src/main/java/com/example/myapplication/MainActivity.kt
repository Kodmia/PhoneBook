package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.domain.entinties.Contact
import com.example.myapplication.presentation.contactslist.ContactsListFragment

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding
    private val actions =  mutableListOf<() -> Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ContactsListFragment())
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        actions.forEach{
            it()
        }
        actions.clear()
    }

    override fun back() {
        runWhenActive {
            onBackPressed()
        }
    }

    override fun showDetails(contact: Contact) {
        TODO("Not yet implemented")
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Функция позволяет избежать ошибок, если навигация была вызвана после возвращения экрана из фона
     * Например, если функция, после которой идет навигация, не успела закончится
     */
    private fun runWhenActive(action: () -> Unit){
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)){
            action()
        }
        else {
            actions.add(action)
        }
    }
}