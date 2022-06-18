package com.adyen.android.assignment.ui.dialog

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.adyen.android.assignment.R
import com.adyen.android.assignment.ui.CustomDialog
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class ShowCustomDialog @Inject constructor() {

    lateinit var dialog: CustomDialog

    fun show(layoutId : Int, manager : FragmentManager){

        dialog = CustomDialog(layoutId)

        val transaction = manager.beginTransaction()

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        transaction
            .add(R.id.nav_host_fragment, dialog)
            .addToBackStack(null)
            .commit()

    }

    fun dismiss() {

        if (this::dialog.isInitialized) {

            dialog.dismiss()

        }
    }

}