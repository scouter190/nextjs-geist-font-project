package com.example.clientesperu

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.clientesperu.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }
    private val adapter = PersonAdapter { person ->
        // Handle person click if needed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.recyclerView.adapter = adapter

        binding.searchInput.addTextChangedListener { text ->
            viewModel.searchPersons(text?.toString() ?: "")
        }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, NewPersonActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.persons.observe(this) { persons ->
            adapter.submitList(persons)
            
            // Show record count in SnackBar
            val message = getString(R.string.showing_records, persons.size, persons.size)
            Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK") { }
                .show()
        }
    }
}
