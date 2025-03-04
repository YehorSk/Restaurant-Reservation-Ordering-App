package com.yehorsk.platea.core.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1,2){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `restaurant_info` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `address` TEXT NOT NULL,
                `phone` TEXT NOT NULL,
                `email` TEXT NOT NULL,
                `website` TEXT,
                `openingHours` TEXT NOT NULL,
                `created_at` TEXT NOT NULL DEFAULT 'undefined',
                `updated_at` TEXT NOT NULL DEFAULT 'undefined'
            )
        """)
    }
}