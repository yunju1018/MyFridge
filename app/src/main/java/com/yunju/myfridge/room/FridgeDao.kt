package com.yunju.myfridge.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FridgeDao {

    /* 냉장고 추가 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: FridgeEntity)

    /* 조건에 맞는 총 행의 수를 계산 */
    @Query("SELECT COUNT(*) FROM fridgeTable WHERE id = :fridgeId")
    fun countById(fridgeId: String): Int

    /* 냉장고 ID 반환 */
    @Query("SELECT id FROM fridgeTable")
    fun getFridgeId() : LiveData<List<String>>

    /* 냉장고 목록 반환 */
    @Query("SELECT * FROM fridgeTable WHERE id = :fridgeId") // FridgeEntity (tableName : fridge) 에서 fridgeId 가져옴
    suspend fun getFridgeData(fridgeId: String) : FridgeEntity

    /* 냉장고 목록 전체 반환, 오름차순 */
    @Query("SELECT * FROM fridgeTable ORDER BY id ASC")
    fun getAll() : List<FridgeEntity>

    /* 냉장고 목록 수정 */
    @Update
    suspend fun updateFridgeData(fridgeEntity: FridgeEntity)

    /* 냉장고 삭제 */
    @Query("DELETE FROM fridgeTable WHERE id = :fridgeId")
    suspend fun deleteByFridgeId(fridgeId: String)

    /* 냉장고 전체 삭제 */
    @Query("DELETE FROM fridgeTable")
    suspend fun deleteAllFridge()
}