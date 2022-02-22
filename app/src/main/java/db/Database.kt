package db

import android.os.AsyncTask

object Database {
    private lateinit var db: AppDatabase

    fun init(database: AppDatabase) {
        db = database
    }

    fun execute(listener: Task?) {
        if (listener == null) {
            throw RuntimeException("DatabaseTask.Task cannot be null")
        }

        object : AsyncTask<Void, Void, Boolean>() {
            override fun doInBackground(vararg voids: Void): Boolean? {
                try {
                    listener.onTaskStart(db)
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                    return false
                }

            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                listener.onTaskEnd(result!!)
            }

        }.execute()
    }
}