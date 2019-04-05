package momonyan.recordapplication.memo_database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(
    entities = arrayOf(Memo::class),
    version = 1,
    exportSchema = false
) // Kotlin 1.2からは arrayOf(Memo::class)の代わりに[Memo::class]と書ける
abstract class AppMemoDataBase : RoomDatabase() {

    // DAOを取得する。
    abstract fun memoDao(): MemoDao

}
