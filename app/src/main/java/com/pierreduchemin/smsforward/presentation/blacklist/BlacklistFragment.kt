package com.pierreduchemin.smsforward.presentation.blacklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.pierreduchemin.smsforward.R
import com.pierreduchemin.smsforward.databinding.BlacklistFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlacklistFragment : Fragment() {

    private lateinit var ui: BlacklistFragmentBinding
    private val viewModel: BlacklistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ui = BlacklistFragmentBinding.inflate(layoutInflater, container, false)
        setupToolbar()
        return ui.root
    }

    private fun setupToolbar() {
        val appCompatActivity = requireActivity() as AppCompatActivity
        appCompatActivity.setSupportActionBar(ui.toolbar.toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ui.toolbar.tbMainTitle.text = getString(R.string.blacklist_title)
        ui.toolbar.ivHelp.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.blacklistText.observe(viewLifecycleOwner) {
            ui.etBlacklist.setText(it)
        }

        viewModel.isLoaded.observe(viewLifecycleOwner) {
            ui.btnSave.isEnabled = it
        }

        viewModel.isSaved.observe(viewLifecycleOwner) {
            if (it) {
                Snackbar.make(ui.root, R.string.blacklist_info_saved, Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        ui.btnSave.setOnClickListener {
            viewModel.onSaveClicked(ui.etBlacklist.text.toString())
        }
    }
}
