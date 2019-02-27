package momonyan.recordapplication.Database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

class User {
    @PrimaryKey
    var userId: Int = 0

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "info")
    var info: String? = null

    var age: Int = 0
}