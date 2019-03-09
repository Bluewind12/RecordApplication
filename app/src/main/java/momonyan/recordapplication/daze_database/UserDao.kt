package momonyan.recordapplication.daze_database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query


@Dao
interface UserDao {

    // シンプルなSELECTクエリ
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    // メソッドの引数をSQLのパラメーターにマッピングするには :引数名 と書く
    @Query("SELECT * FROM user WHERE userId IN (:userIds)")
    fun loadAllaByIds(vararg userIds: Int): List<User>

    // 複数の引数も渡せる
    @Query("SELECT * FROM user WHERE title LIKE :first AND content LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    // データモデルのクラスを引数に渡すことで、データの作成ができる。
    @Insert
    fun insert(user: User)

    // 可変長引数にしたり
    @Insert
    fun insertAll(vararg users: User)

    // Listで渡したりもできる
    @Insert
    fun insertAll(users: List<User>)

    // データモデルのクラスを引数に渡すことで、データの削除ができる。主キーでデータを検索して削除する場合。
    @Delete
    fun delete(user: User)

    // 条件でDelete
    @Query("DELETE FROM user WHERE userId = :id")
    fun deleteId(id: Int)

    // 書き込み
    @Query("UPDATE user SET title = :title , content = :content ,color = :color, textColor = :textColor WHERE userId = :id ")
    fun editDaze(id: Int, title: String, content: String, color: Int, textColor: Int)

    // 書き込み
    @Query("UPDATE user SET memo = :changeMemo WHERE userId = :id ")
    fun editMemo(id: Int, changeMemo: String)



}