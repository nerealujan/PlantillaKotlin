package datasource

interface DataSource {

    interface LoadDataCallback<T> {
        fun onDataLoaded(response: T)

        fun onDataNotAvailable()
    }
}