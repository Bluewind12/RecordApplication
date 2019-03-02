package momonyan.recordapplication.daze_database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(User::class), version = 1) // Kotlin 1.2からは arrayOf(Memo::class)の代わりに[Memo::class]と書ける
abstract class AppDataBase : RoomDatabase() {

    // DAOを取得する。
    abstract fun userDao(): UserDao

    // valでも良い。
    // abstract val dao: MemoDao
}
