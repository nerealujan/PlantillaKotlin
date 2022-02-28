package utils

import android.content.Context

class Configuration(context: Context) {
    private var mReader: android.content.SharedPreferences
    private val mEditor: android.content.SharedPreferences.Editor


    init {
        mReader = context.getSharedPreferences(NAME_PREFERENCES, Context.MODE_PRIVATE)
        mEditor = mReader.edit()
        mEditor.apply()
    }

    fun clear(): Configuration {
        mEditor.clear()
        mEditor.commit()
        return this
    }

    fun setBoolean(key: String, value: Boolean) {
        mEditor.putBoolean(key, value)
        mEditor.commit()
    }

    fun getBoolean(key: String): Boolean {
        return mReader.getBoolean(key, false)
    }

    fun setString(key: String, value: String) {
        mEditor.putString(key, value)
        mEditor.commit()
    }

    fun getString(key: String): String {
        return mReader.getString(key, "")!!
    }

    fun setListString(key: String, items: List<String>) {
        val stringList = items.joinToString(",")
        mEditor.putString(key, stringList)
        mEditor.commit()
    }

    fun getListString(key: String): List<String> {
        val items = mReader.getString(key, "")
        return items!!.split(",")
    }

    companion object {
        private const val NAME_PREFERENCES = "myApp"
        const val TOKEN = "token"
    }

}