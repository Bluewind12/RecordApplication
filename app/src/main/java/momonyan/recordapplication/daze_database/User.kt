package momonyan.recordapplication.daze_database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class User {
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "content")
    var content: String? = null

    @ColumnInfo(name = "days")
    var day: String? = null

    @ColumnInfo(name = "color")
    var color: Int = 0

    @ColumnInfo(name = "textColor")
    var colorDL: Int = 0

}