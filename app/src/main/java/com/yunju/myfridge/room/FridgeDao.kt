package com.yunju.myfridge.room

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

    /* 냉장고 ID 반환 */
    @Query("SELECT id FROM fridgeTable")
    suspend fun getFridgeId() : List<String>

    /* 냉장고 목록 반환 */
    @Query("SELECT * FROM fridgeTable WHERE id = :fridgeId") // FridgeEntity (tableName : fridge) 에서 fridgeId 가져옴
    suspend fun getFridgeData(fridgeId: String) : FridgeEntity

    /* 냉장고 목록 수정 */
    @Update
    suspend fun updateFridgeData(fridgeEntity: FridgeEntity)

    /* 냉장고 삭제 */
    @Query("DELETE FROM fridgeTable")
    suspend fun deleteFridge()
}