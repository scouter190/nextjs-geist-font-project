package com.example.clientesperu

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.clientesperu.data.Person
import com.example.clientesperu.databinding.ActivityNewPersonBinding

class NewPersonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPersonBinding
    private val viewModel: NewPersonViewModel by viewModels { NewPersonViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        // Setup district spinner
        val districts = arrayOf("San Isidro", "Miraflores", "Surco", "La Molina", "Barranco", "San Borja")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.districtSpinner.adapter = spinnerAdapter

        // Handle gender checkboxes mutual exclusivity
        binding.cbMale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.cbFemale.isChecked = false
        }
        binding.cbFemale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.cbMale.isChecked = false
        }

        binding.btnSave.setOnClickListener {
            if (validateFields()) {
                val person = createPersonFromInput()
                viewModel.savePerson(person)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.saveResult.observe(this) { result ->
            when (result) {
                is SaveResult.Success -> {
                    showSuccessDialog()
                }
                is SaveResult.DuplicateDocument -> {
                    showDuplicateDocumentError()
                }
                is SaveResult.Error -> {
                    showErrorDialog(result.message)
                }
            }
        }
    }

    private fun validateFields(): Boolean {
        val missingFields = mutableListOf<String>()

        if (binding.fullNameInput.text.isNullOrBlank()) {
            missingFields.add("Nombre Completo")
        }
        if (binding.documentInput.text.isNullOrBlank()) {
            missingFields.add("Documento de Identidad")
        }
        if (binding.addressInput.text.isNullOrBlank()) {
            missingFields.add("Dirección")
        }
        if (binding.phoneInput.text.isNullOrBlank()) {
            missingFields.add("Teléfono")
        }

        return if (missingFields.isEmpty()) {
            true
        } else {
            val message = "Campos obligatorios faltantes:\n${missingFields.joinToString("\n")}"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun createPersonFromInput(): Person {
        val maritalStatus = when {
            binding.rbSingle.isChecked -> "Soltero(a)"
            binding.rbMarried.isChecked -> "Casado(a)"
            binding.rbDivorced.isChecked -> "Divorciado(a)"
            binding.rbWidowed.isChecked -> "Viudo(a)"
            else -> ""
        }

        val gender = when {
            binding.cbMale.isChecked -> "Masculino"
            binding.cbFemale.isChecked -> "Femenino"
            else -> ""
        }

        return Person(
            documentId = binding.documentInput.text.toString(),
            fullName = binding.fullNameInput.text.toString(),
            address = binding.addressInput.text.toString(),
            phone = binding.phoneInput.text.toString(),
            district = binding.districtSpinner.selectedItem.toString(),
            maritalStatus = maritalStatus,
            gender = gender
        )
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Éxito")
            .setMessage("Persona registrada correctamente")
            .setPositiveButton("OK") { _, _ ->
                finish()
            }
            .show()
    }

    private fun showDuplicateDocumentError() {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("El documento de identidad ya existe")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
