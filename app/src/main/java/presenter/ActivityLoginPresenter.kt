package presenter

import android.content.Context
import local.RepositoryLocal
import remote.RepositoryRemote
import repository.Repository

class ActivityLoginPresenter (val context: Context, val view: ActivityLoginMVP.View) : ActivityLoginMVP.ActivityLoginView {
    private val repository: Repository =
        Repository.getInstance(RepositoryLocal.getInstance(), RepositoryRemote.getInstance())


}