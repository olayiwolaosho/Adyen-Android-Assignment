package com.adyen.android.assignment.ui

import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.adyen.android.assignment.R
import com.adyen.android.assignment.ui.callbacks.RefreshListener


class CustomDialog(
    private val layoutId : Int = R.layout.loading_screen,
    private val viewsFragment : Fragment? = null
) : DialogFragment() {

    // Using this instance of the refreshListener to deliver refresh events
    private lateinit var listener: RefreshListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(layoutId, container, false)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_DeviceDefault_Dialog_MinWidth
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {

            viewsFragment?.let { frag ->

                listener = frag as RefreshListener

            }

        } catch (e: ClassCastException) {

            //if the activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement RefreshListener"))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(layoutId){

            R.layout.error_screen ->{

                val refreshButton = view.findViewById<Button>(R.id.button_refresh)

                refreshButton.setOnClickListener {

                    listener.refresh()

                }

            }

            R.layout.network_error_screen ->{

                val networkButton = view.findViewById<Button>(R.id.network_setting)

                networkButton.setOnClickListener {

                    val intent  = Intent(android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS )

                    //val intent = Intent(Intent.ACTION_MAIN)

                    //val cName = ComponentName("com.android.phone", "com.android.phone.NetworkSetting")

                    //intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting")

                    startActivity(intent)

                }
            }
        }
    }

}