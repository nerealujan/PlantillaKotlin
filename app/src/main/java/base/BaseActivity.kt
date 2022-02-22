package base

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.ButterKnife
import com.plantillakotlin.myapplication.R
import utils.Configuration
import utils.ViewUtils
import utils.customSnackbar

open class BaseActivity : AppCompatActivity(), BaseActivityView {
    lateinit var config: Configuration
    private var mToolbar: Toolbar? = null
    private var mProgressDialog: AlertDialog? = null
    private var mProgressDialogView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("APP", "Activity: " + javaClass.simpleName)
        config = Configuration(this)
    }

    override fun onResume() {
        super.onResume()
        if (!javaClass.simpleName.equals("activitylogin", ignoreCase = true)) {
            //Se añade el login
        }


    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ButterKnife.bind(this)

        // setup toolbar as action bar
        mToolbar = findViewById(R.id.toolbar)
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
            val actionBar = supportActionBar
            actionBar?.title = title
        }

        setKeyboardVisibilityListener()
    }


    override fun setTitle(title: CharSequence) {
        val actionBar = supportActionBar
        actionBar?.title = title
        if (mToolbar != null) {
            mToolbar!!.title = title
        }
        super.setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    //////////////////////////////////////////////////////////////
    //                        UTIL                              //
    //////////////////////////////////////////////////////////////

    fun setToolbarBackButton(showBackButton: Boolean) {
        val actionBar = supportActionBar ?: return
        actionBar.setDisplayHomeAsUpEnabled(showBackButton)
    }

    fun setToolbarBackButton(showBackButton: Boolean, @DrawableRes drawableRes: Int) {
        val actionBar = supportActionBar ?: return
        actionBar.setDisplayHomeAsUpEnabled(showBackButton)
        actionBar.setHomeAsUpIndicator(drawableRes)
    }


    //////////////////////////////////////////////////////////////
    //                  BASEACTIVITYVIEW                        //
    //////////////////////////////////////////////////////////////

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showSnackbar(text: String, icon: Int?, bgColor: Int?, duration: Int?) {
        val view = findViewById<View>(android.R.id.content)

        customSnackbar(view, text, icon, bgColor, duration)
    }

    override fun showDialog(
        message: String,
        positiveText: String?, positiveListener: View.OnClickListener?,
        negativeText: String?, negativeListener: View.OnClickListener?
    ) {
        if (isFinishing) return

        AlertDialog.Builder(this)
            .setPositiveButton(positiveText) { _, _ -> positiveListener?.onClick(null) }
            .setNegativeButton(negativeText) { _, _ -> negativeListener?.onClick(null) }
            .setMessage(message)
            .create()
            .show()
    }

    override fun showLoadingDialog(message: String?) {
        if (isFinishing) return

        if (mProgressDialog == null) {
            mProgressDialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
            mProgressDialog = AlertDialog.Builder(this).setView(mProgressDialogView).create()
        }

        mProgressDialogView?.findViewById<TextView>(R.id.label)?.text = message
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.show()
    }

    override fun hideLoadingDialog() {
        if (mProgressDialog == null) {
            return
        }
        mProgressDialog!!.hide()
        mProgressDialog!!.dismiss()
        mProgressDialog = null
    }

    override fun hideKeyboard() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    override fun onKeyBoardVisiblityChanged(visible: Boolean) {
    }

    private fun setKeyboardVisibilityListener() {
        val parentView: View = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)

        parentView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {

            var alreadyOpen: Boolean = false
            val defaultKeyboardHeightDP = 100f
            val EstimatedKeyboardDP =
                defaultKeyboardHeightDP + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
            val rect = Rect()

            override fun onGlobalLayout() {
                val estimatedKeyboardHeight = ViewUtils.convertDpToPixel(EstimatedKeyboardDP)
                parentView.getWindowVisibleDisplayFrame(rect)

                val heightDiff = parentView.rootView.height - (rect.bottom - rect.top)
                val isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    return
                }

                alreadyOpen = isShown

                onKeyBoardVisiblityChanged(alreadyOpen)
            }
        })
    }
}