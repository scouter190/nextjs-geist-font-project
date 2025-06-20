package com.example.clientesperu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.clientesperu.data.Person
import com.example.clientesperu.data.PersonDatabase
import kotlinx.coroutines.launch

sealed class SaveResult {
    object Success : SaveResult()
    object DuplicateDocument : SaveResult()
    data class Error(val message: String) : SaveResult()
}

class NewPersonViewModel(application: Application) : AndroidViewModel(application) {
    private val database = PersonDatabase.getDatabase(application)
    private val personDao = database.personDao()

    private val _saveResult = MutableLiveData<SaveResult>()
    val saveResult: LiveData<SaveResult> = _saveResult

    fun savePerson(person: Person) {
        viewModelScope.launch {
            try {
                // Check if document already exists
                val exists = personDao.isDocumentExists(person.documentId) > 0
                if (exists) {
                    _saveResult.value = SaveResult.DuplicateDocument
                    return@launch
                }

                // Save person
                personDao.insert(person)
                _saveResult.value = SaveResult.Success
            } catch (e: Exception) {
                _saveResult.value = SaveResult.Error(e.message ?: "Error desconocido")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                NewPersonViewModel(application)
            }
        }
    }
}
