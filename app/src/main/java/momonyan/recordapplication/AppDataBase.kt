package momonyan.recordapplication

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(User::class), version = 1) // Kotlin 1.2からは arrayOf(User::class)の代わりに[User::class]と書ける
abstract class AppDatabase : RoomDatabase() {

    // DAOを取得する。
    abstract fun userDao(): UserDao

    // valでも良い。
    // abstract val dao: UserDao
}
