package com.adyen.android.assignment.ui.bindings

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.adyen.android.assignment.util.Globals
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view : ImageView, imageUrl : String?){

    imageUrl?.let { url ->

        Glide.with(view.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)

    }
}

@BindingAdapter("dateFormat")
fun bindWateringText(textView: TextView, date: String?) {

    date?.let { inputDate ->

        textView.text = Globals.convertDate(inputDate)

    }
}