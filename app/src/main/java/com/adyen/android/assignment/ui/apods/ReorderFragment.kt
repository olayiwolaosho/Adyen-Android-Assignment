package com.adyen.android.assignment.ui.apods

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.adyen.android.assignment.data.DATE_SORT_TAG
import com.adyen.android.assignment.data.NO_SORT_TAG
import com.adyen.android.assignment.data.TITLE_SORT_TAG
import com.adyen.android.assignment.data.extensions.setWidthPercent
import com.adyen.android.assignment.databinding.FragmentReorderBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReorderFragment : DialogFragment() {

    private lateinit var binding: FragmentReorderBinding

    private val viewModel: ApodsViewModel by activityViewModels()

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

        setWidthPercent(90)

        initListeners()

        initViews()
    }

    private fun initViews() {

        when(viewModel.currentSortTag){

            DATE_SORT_TAG ->{

                binding.radioDate.isChecked = true

            }

            TITLE_SORT_TAG ->{

                binding.radioTitle.isChecked = true

            }

        }

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

            dismiss()
        }

        binding.buttonApply.setOnClickListener {

            var sortTag = NO_SORT_TAG

            if(binding.radioDate.isChecked){

                sortTag = DATE_SORT_TAG

            }

            if(binding.radioTitle.isChecked){

                sortTag = TITLE_SORT_TAG

            }

            viewModel.addFilter(sortTag)

            dismiss()

        }

    }

}