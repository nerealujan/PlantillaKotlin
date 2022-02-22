package utils

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.plantillakotlin.myapplication.R
import com.google.android.material.snackbar.Snackbar

inline fun Context.customSnackbar(
    view: View,
    text: String,
    icon: Int? = null,
    bgColor: Int? = null,
    duration: Int? = Snackbar.LENGTH_LONG
) {

    // get snackbar and inflate custom view
    val snackbar = Snackbar.make(view, "", duration!!)
    val customView = LayoutInflater.from(this).inflate(R.layout.custom_snackbar, snackbar.view as ViewGroup)

    // background
    val viewRoot = customView.findViewById<View>(R.id.customSnackbarRoot)
    if (bgColor != null) {
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, bgColor))
        viewRoot.setBackgroundColor(ContextCompat.getColor(this, bgColor))
    } else {
        snackbar.view.setBackgroundColor(Color.DKGRAY)
        viewRoot.setBackgroundColor(Color.DKGRAY)
    }

    // icon
    val viewIcon = customView.findViewById<ImageView>(R.id.customSnackbarIcon)
    if (icon != null) {
        viewIcon.visibility = View.VISIBLE
        viewIcon.setImageResource(icon)
    } else {
        viewIcon.visibility = View.GONE
    }

    // text
    val viewLabel = customView.findViewById<TextView>(R.id.customSnackbarText)
    viewLabel.text = text

    // show snackbar
    snackbar.show()

}


fun TextView.setDrawableColor(@ColorRes color: Int) {
    compoundDrawables.filterNotNull().forEach {
        it.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
    }
}