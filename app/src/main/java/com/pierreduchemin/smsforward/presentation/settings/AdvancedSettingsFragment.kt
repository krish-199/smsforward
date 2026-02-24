package com.pierreduchemin.smsforward.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierreduchemin.smsforward.R
import com.pierreduchemin.smsforward.databinding.AdvancedSettingsFragmentBinding
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvancedSettingsFragment : Fragment() {

    private lateinit var ui: AdvancedSettingsFragmentBinding
    private val viewModel: AdvancedSettingsViewModel by viewModels()
    private lateinit var adapter: ReplacementRuleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ui = AdvancedSettingsFragmentBinding.inflate(inflater, container, false)
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        adapter = ReplacementRuleAdapter(
            onRuleChanged = { viewModel.updateReplacementRule(it) },
            onRuleDeleted = { viewModel.deleteReplacementRule(it) }
        )
        ui.rvReplacements.layoutManager = LinearLayoutManager(requireContext())
        ui.rvReplacements.adapter = adapter

        viewModel.globalModel.observe(viewLifecycleOwner) { globalModel ->
            globalModel?.let {
                if (ui.etPrefix.text.toString() != it.prefix) {
                    ui.etPrefix.setText(it.prefix)
                }
                if (ui.etSuffix.text.toString() != it.suffix) {
                    ui.etSuffix.setText(it.suffix)
                }
            }
        }

        viewModel.replacementRules.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }

        ui.etPrefix.doAfterTextChanged {
            viewModel.updatePrefix(it.toString())
        }
        ui.etSuffix.doAfterTextChanged {
            viewModel.updateSuffix(it.toString())
        }
        ui.btnAddReplacement.setOnClickListener {
            viewModel.addReplacementRule()
        }
    }

    private fun setupToolbar() {
        val appCompatActivity = requireActivity() as AppCompatActivity
        appCompatActivity.setSupportActionBar(ui.toolbar.toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appCompatActivity.supportActionBar?.title = getString(R.string.advanced_settings_title)
        ui.toolbar.tbMainTitle.text = getString(R.string.advanced_settings_title)
        ui.toolbar.ivHelp.visibility = View.GONE
        ui.toolbar.ivSettings.visibility = View.GONE
    }
}
