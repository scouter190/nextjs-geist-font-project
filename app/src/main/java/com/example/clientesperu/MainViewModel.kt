package com.example.clientesperu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.clientesperu.data.Person
import com.example.clientesperu.data.PersonDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = PersonDatabase.getDatabase(application)
    private val personDao = database.personDao()
    private val searchQuery = MutableStateFlow("")

    val persons: LiveData<List<Person>> = searchQuery
        .debounce(300)
        .switchMap { query ->
            if (query.isEmpty()) {
                personDao.getAllPersons()
            } else {
                personDao.searchPersons("%$query%")
            }
        }

    fun searchPersons(query: String) {
        viewModelScope.launch {
            searchQuery.emit(query)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                MainViewModel(application)
            }
        }
    }
}
