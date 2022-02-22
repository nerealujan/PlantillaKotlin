package db

interface Task {
    fun onTaskStart(database: AppDatabase)
    fun onTaskEnd(result: Boolean)
}