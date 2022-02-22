package base

import android.view.View
import com.google.android.material.snackbar.Snackbar

interface BaseActivityView {
    fun showToast(message: String)

    fun showSnackbar(
        text: String, icon: Int? = null,
        bgColor: Int? = null,
        duration: Int? = Snackbar.LENGTH_LONG
    )

    fun showDialog(
        message: String,
        positiveText: String? = "Ok", positiveListener: View.OnClickListener? = null,
        negativeText: String? = null, negativeListener: View.OnClickListener? = null
    )

    fun showLoadingDialog(message: String? = "Por favor espera")

    fun hideLoadingDialog()

    fun hideKeyboard()

    fun onKeyBoardVisiblityChanged(visible: Boolean)
}
