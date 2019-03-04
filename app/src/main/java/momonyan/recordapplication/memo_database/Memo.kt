package momonyan.recordapplication.memo_database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Memo {
    @PrimaryKey(autoGenerate = true)
    var memoId: Int = 0

    @ColumnInfo(name = "check")
    var check: Boolean? = null

    @ColumnInfo(name = "content")
    var content: String? = null

    @ColumnInfo(name = "backColor¬")
    var color: Int = 0

    @ColumnInfo(name = "textColor")
    var textColor: Int = 0
}