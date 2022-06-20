package com.adyen.android.assignment.ui.apods

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.adyen.android.assignment.data.Constants.DATE_SORT_TAG
import com.adyen.android.assignment.data.Constants.TITLE_SORT_TAG
import com.adyen.android.assignment.databinding.FragmentReorderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReorderFragment : DialogFragment() {

    private lateinit var binding: FragmentReorderBinding

    private val viewModel: ApodsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_DeviceDefault_Dialog_MinWidth
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentReorderBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        initListeners()

        initViews()
    }

    private fun initViews() {



    }

    private fun initListeners() {

        binding.radioTitle.setOnClickListener {

            binding.radioDate.isChecked = false

        }

        binding.radioDate.setOnClickListener {

            binding.radioTitle.isChecked = false

        }

        binding.buttonReset.setOnClickListener {

            binding.radioDate.isChecked = false
            binding.radioTitle.isChecked = false

            viewModel.removeFilter()
        }

        binding.buttonApply.setOnClickListener {

            var sortTag = 0

            if(binding.radioDate.isChecked){

                sortTag = DATE_SORT_TAG

            }

            if(binding.radioTitle.isChecked){

                sortTag = TITLE_SORT_TAG

            }

            viewModel.addFilter(sortTag)

            //dismiss()

        }

    }

    private fun initObservers() {


    }

}