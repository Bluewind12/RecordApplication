package momonyan.recordapplication.memo_database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query


@Dao
interface MemoDao {

    // シンプルなSELECTクエリ
    @Query("SELECT * FROM memo")
    fun getAll(): LiveData<List<Memo>>

    // データモデルのクラスを引数に渡すことで、データの作成ができる。
    @Insert
    fun insert(memo: Memo)

    // 可変長引数にしたり
    @Insert
    fun insertAll(vararg memos: Memo)

    // Listで渡したりもできる
    @Insert
    fun insertAll(memos: List<Memo>)


    // 条件でDelete
    @Query("DELETE FROM memo WHERE memoId = :id")
    fun deleteId(id: Int)

    @Query("DELETE FROM memo WHERE `check` = :check")
    fun deleteCheck(check: Boolean)


    // データモデルのクラスを引数に渡すことで、データの削除ができる。主キーでデータを検索して削除する場合。
    @Delete
    fun delete(memo: Memo)

    @Query("UPDATE memo SET `check` = :bool WHERE memoId = :id ")
    fun updateMemoCheck(id: Int, bool: Boolean)

    @Query("UPDATE memo SET content = :content , backColor = :color ,textColor = :textColor WHERE memoId = :id ")
    fun updateMemoData(id: Int, content: String, color: Int, textColor: Int)

}