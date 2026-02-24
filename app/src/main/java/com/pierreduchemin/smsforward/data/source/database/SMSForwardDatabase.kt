package com.pierreduchemin.smsforward.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [GlobalModel::class, ForwardModel::class, ReplacementRule::class], version = 5, exportSchema = false)
abstract class SMSForwardDatabase : RoomDatabase() {

    companion object {
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE GlobalModel ADD COLUMN prefix TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE GlobalModel ADD COLUMN suffix TEXT NOT NULL DEFAULT ''")
                db.execSQL("CREATE TABLE IF NOT EXISTS ReplacementRule (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, pattern TEXT NOT NULL, replacement TEXT NOT NULL)")
            }
        }
    }

    abstract fun globalModelDao(): GlobalModelDao

    abstract fun forwardModelDao(): ForwardModelDao

    abstract fun replacementRuleDao(): ReplacementRuleDao
}