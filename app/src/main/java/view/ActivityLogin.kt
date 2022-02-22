package view

import android.os.Bundle
import base.BaseActivity
import com.plantillakotlin.myapplication.R
import presenter.ActivityLoginMVP
import presenter.ActivityLoginPresenter

class ActivityLogin : BaseActivity(), ActivityLoginMVP.View {
    private val presenter: ActivityLoginMVP.ActivityLoginView = ActivityLoginPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


    }


}