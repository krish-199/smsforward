package com.pierreduchemin.smsforward.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [GlobalModel::class, ForwardModel::class], version = 5, exportSchema = false)
abstract class SMSForwardDatabase : RoomDatabase() {

    abstract fun globalModelDao(): GlobalModelDao

    abstract fun forwardModelDao(): ForwardModelDao

    companion object {
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE GlobalModel ADD COLUMN forwardCount INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE ForwardModel ADD COLUMN forwardCount INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}