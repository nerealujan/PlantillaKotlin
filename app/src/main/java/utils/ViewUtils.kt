package utils

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Build
import android.text.Html
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.plantillakotlin.myapplication.R
import utils.Constants.Companion.DATE_FORMAT_TOKEN
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.Long
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class ViewUtils {
    companion object {
        fun log(message: String) {
            Log.d("APP DE PRUEBAS", message)
        }

        fun logout(context: Context, forceLogout: Boolean) {
            if (forceLogout) {
                //CERRAMOS SESION
//                logout(context)
            }
            val config = Configuration(context)
            config.clear()
            System.exit(0)
        }

        fun replaceAccent(text: String?): String {
            if (text == null) return ""
            return text.toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
        }

        fun convertMicrosoftDateFormatToDate(msFormat: String): Date {
            val timestamp = msFormat.substring(6, 19)
            val timezone = msFormat.substring(19, 24)
            val date = Date(Long.parseLong(timestamp))

            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.timeZone = TimeZone.getTimeZone("GMT$timezone")

            return calendar.time
        }

        fun setBackgroundSelectableItem(view: View?) {
            if (view == null || view.context == null) return
            val context = view.context

            // Create an array of the attributes we want to resolve using values from a theme
            // android.R.attr.selectableItemBackground requires API LEVEL 11
            val attrs = intArrayOf(android.R.attr.selectableItemBackground /* index 0 */)
            //R.attr.selectableItemBackground;

            // Obtain the styled attributes. 'themedContext' is a context with a theme
            val ta = context.obtainStyledAttributes(attrs)

            // Now get the value of the 'listItemBackground' attribute that was set in the theme used in 'themedContext'.
            // The parameter is the index of the attribute in the 'attrs' array. The returned Drawable is what you are after
            val drawableSelectableItemBackground = ta.getDrawable(0 /* index */)

            // Finally free resources used by TypedArray
            ta.recycle()

            // check if mDrawableSelectableItemBackground is null
            if (drawableSelectableItemBackground == null) return

            // setBackground(Drawable) requires API LEVEL 16,
            // otherwise you have to use deprecated setBackgroundDrawable(Drawable) method.
            view.background = drawableSelectableItemBackground
            view.isClickable = true
        }

        enum class Orientation {
            HORIZONTAL, VERTICAL
        }

        fun getScreenWidth(windowManager: WindowManager?): Int {
            if (windowManager == null) return 0
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.x
        }

        fun getScreenHeight(windowManager: WindowManager?): Int {
            if (windowManager == null) return 0
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.y
        }

        fun getStatusBarHeight(context: Context): Int {
            var result = 0
            val resourceId =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

        fun getDivider(context: Context?, orientation: Orientation, resColor: Int): View? {
            if (context == null) return null

            val viewLine = View(context)
            viewLine.setBackgroundColor(ContextCompat.getColor(context, resColor))

            if (orientation == Orientation.VERTICAL) {
                viewLine.layoutParams =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        convertDpToPixel(1f).toInt()
                    )
            } else if (orientation == Orientation.HORIZONTAL) {
                viewLine.layoutParams =
                    LinearLayout.LayoutParams(
                        convertDpToPixel(1f).toInt(),
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
            }

            return viewLine
        }

        /**
         * This method converts dp unit to equivalent pixels, depending on device density.
         *
         * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
         * @return A float value to represent px equivalent to dp depending on device density
         */
        fun convertDpToPixel(dp: Float): Float {
            val metrics = Resources.getSystem().displayMetrics
            return dp * (metrics.densityDpi / 160f)
        }

        /**
         * This method converts device specific pixels to density independent pixels.
         *
         * @param px A value in px (pixels) unit. Which we need to convert into db
         * @return A float value to represent dp equivalent to px value
         */
        fun convertPixelsToDp(px: Float): Float {
            val metrics = Resources.getSystem().displayMetrics
            return px / (metrics.densityDpi / 160f)
        }

        /**
         * Blend `color1` and `color2` using the given ratio.
         *
         * @param ratio of which to blend. 1.0 will return `color1`, 0.5 will give an even blend,
         * 0.0 will return `color2`.
         */
        fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
            val inverseRation = 1f - ratio
            val r = Color.red(color1) * ratio + Color.red(color2) * inverseRation
            val g = Color.green(color1) * ratio + Color.green(color2) * inverseRation
            val b = Color.blue(color1) * ratio + Color.blue(color2) * inverseRation
            return Color.rgb(r.toInt(), g.toInt(), b.toInt())
        }


        fun getActionBarSize(context: Context?): Int {
            var actionBarSize = 0
            if (context == null) return actionBarSize

            val tv = TypedValue()
            if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarSize = TypedValue.complexToDimensionPixelSize(
                    tv.data,
                    context.resources.displayMetrics
                )
            }

            return actionBarSize
        }

        fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
            var drawable = ContextCompat.getDrawable(context, drawableId)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = DrawableCompat.wrap(drawable!!).mutate()
            }

            val bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        }

        fun hoursBetween(d1: Date, d2: Date): Int {
            return ((getTime(d2) - getTime(d1)) / (1000 * 60 * 60)).toInt()
        }

        private fun getTime(date: Date): kotlin.Long {
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar.timeInMillis
        }

        fun getBase64FromPath(path: String): String {
            var base64 = ""
            try {/*from   w w w .  ja  va  2s  .  c om*/
                val file = File(path)
                val buffer = ByteArray(file.length().toInt() + 100)
                val length = FileInputStream(file).read(buffer)
                base64 = Base64.encodeToString(
                    buffer, 0, length,
                    Base64.DEFAULT
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return base64
        }

        fun getColorByName(name: String): Int {
            var colorId = 0
            try {
                val res = R.color::class.java
                val field = res.getField(name)
                colorId = field.getInt(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return colorId
        }

        /*fun countUnreadMessages(items: List<AllCommunications>): Int {
            var unread = 0
            for (item in items) {
                if (item.lastMessageStatus == AllCommunications.Companion.LastMessageStatusEnum.UNREADED.value) {
                    unread++
                }
            }
            return unread
        }*/

        fun genericAlert(context: Context, message: String) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(
                    context.resources.getString(R.string.ok)
                ) { dialog, id -> }
            val alert = builder.create()
            alert.show()
        }

        fun genericConfirmDialog(
            context: Context,
            title: String = "",
            description: String = "",
            functionOk: () -> Unit,
            functionCancel: () -> Unit
        ) {
            var message = ""
            if (title != "") {
                message += "<font color='#000000'>$title</font><br /><br />"
            }
            message += "<font color='#666666'>$description</font>"
            val builder = AlertDialog.Builder(context)
            builder.setMessage(Html.fromHtml(message))
                .setCancelable(false)
                .setPositiveButton(
                    context.getString(R.string.accept)
                ) { dialog, id ->
                    functionOk()
                }.setNegativeButton(
                    context.getString(R.string.cancel)
                ) { dialog, id -> functionCancel() }
            val alert = builder.create()
            alert.show()

            val colorButton = ContextCompat.getColor(context, R.color.blue_dark)
            val positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(colorButton)
            val negativeButton = alert.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(colorButton)
        }

        fun setToolbarBackButtonColor(toolbar: Toolbar, color: Int) {
            toolbar.navigationIcon!!.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }

        fun extractOtNumber(full: String): String {
            return full.substring(full.lastIndexOf("-") + 1).trim()
        }

      /*  fun extractDocuments(documents: List<DocumentCategory>): String {
            val sb = java.lang.StringBuilder()
            var foundOne = false

            for (document in documents) {
                if (foundOne) {
                    sb.append(" / ")
                }
                foundOne = true
                sb.append(document.category)
            }

            return sb.toString()
        }*/

        fun cutIfHasMultipleOts(otString: String): String {
            val characterSeparator = ";"
            return if (otString.trim().contains(characterSeparator)) {
                otString.substring(0, otString.indexOf(characterSeparator))
            } else {
                otString
            }
        }

        fun getMinutesDiff(date1: Date?, date2: Date?): kotlin.Long {
            val d1 = Calendar.getInstance()
            d1.time = date1
            val d2 = Calendar.getInstance()
            d2.time = date2
            return Math.abs(
                (d2.time.time - d1.time.time) / (60 * 1000)
            )
        }

        fun isValidToken(token: String): Boolean {
            try {
                val data = Base64.decode(token, Base64.DEFAULT)
                val decodeString = String(data, Charset.forName("UTF-8"))
                val position = decodeString.indexOf("NotOnOrAfter")
                val dateStr = decodeString.substring(position + 14, position + 33)
                val formatter = SimpleDateFormat(DATE_FORMAT_TOKEN)
                val dateToken = formatter.parse(dateStr)
                val now = Date()
                return getMinutesDiff(now, dateToken) > 10
            } catch (e: java.lang.Exception) {
            }
            return false
        }

    }
}