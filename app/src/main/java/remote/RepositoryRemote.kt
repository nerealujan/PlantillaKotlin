package remote

class RepositoryRemote {

    companion object : IRepositoryRemote  {
        private var INSTANCE: RepositoryRemote? = null

        @Synchronized
        fun getInstance(): RepositoryRemote {
            if (INSTANCE == null) {
                INSTANCE = RepositoryRemote()
            }
            return INSTANCE as RepositoryRemote
        }
    }
}