package local

class RepositoryLocal : IRepositoryLocal {


    companion object {
        private var INSTANCE: RepositoryLocal? = null

        @Synchronized
        fun getInstance(): RepositoryLocal {
            if (INSTANCE == null) {
                INSTANCE =
                    RepositoryLocal()
            }
            return INSTANCE as RepositoryLocal
        }
    }

}