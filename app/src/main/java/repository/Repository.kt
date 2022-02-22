package repository

import local.RepositoryLocal
import remote.RepositoryRemote

class Repository : IRepository {
    private var mLocal: RepositoryLocal? = null
    private var mRemote: RepositoryRemote? = null

    private constructor(local: RepositoryLocal, remote: RepositoryRemote) {
        mLocal = local
        mRemote = remote
    }

    //Añade aquí tus llamadas




    ////////////////////////////////////
    companion object {
        private var INSTANCE: Repository? = null

        @Synchronized
        fun getInstance(local: RepositoryLocal, remote: RepositoryRemote): Repository {
            if (INSTANCE == null) {
                INSTANCE = Repository(local, remote)
            }
            return INSTANCE as Repository
        }
    }

}