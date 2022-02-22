package base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import db.AppDatabase
import db.Database

class ApplicationBase : Application(), Application.ActivityLifecycleCallbacks {

    companion object {
        lateinit var currentActivity: FragmentActivity
        lateinit var instance: ApplicationBase
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(this)

        initDB()
    }
    fun initDB() {
        //añade el nombre de la bbdd a una clase que sea CONSTANS
        Database.init(
            Room.databaseBuilder(this, AppDatabase::class.java, "Nombre de tu bbdd")
                .fallbackToDestructiveMigration()
                .build()
        )
    }

    private fun setActivity(fragmentActivity: FragmentActivity?) {
        currentActivity = fragmentActivity!!
    }

    fun getActivity(): FragmentActivity? = currentActivity
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = setActivity(activity as FragmentActivity?)
    override fun onActivityDestroyed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityStopped(activity: Activity) = Unit

}