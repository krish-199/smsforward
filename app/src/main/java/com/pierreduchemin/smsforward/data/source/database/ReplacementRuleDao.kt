package com.pierreduchemin.smsforward.data.source.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete

@Dao
interface ReplacementRuleDao {

    @Query("SELECT * FROM ReplacementRule")
    fun observeReplacementRules(): LiveData<List<ReplacementRule>>

    @Query("SELECT * FROM ReplacementRule")
    fun getReplacementRules(): List<ReplacementRule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReplacementRule(replacementRule: ReplacementRule): Long

    @Delete
    fun deleteReplacementRule(replacementRule: ReplacementRule)

    @Query("DELETE FROM ReplacementRule WHERE id = :id")
    fun deleteReplacementRuleById(id: Long)
}
