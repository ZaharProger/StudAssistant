package com.example.studassistant.constants;

import java.util.Locale;

public class DataBaseValues {
    public static final String DATABASE_NAME = "liked.db";
    public static final int SCHEMA = 2;
    public static final String TABLE_NAME = "tutors";
    public static final String ID_COLUMN = "id";
    public static final String TUTOR_PERSONAL_COLUMN = "personal";
    public static final String CREATE_TABLE_TEMPLATE = String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "%s VARCHAR(30) NOT NULL);", TABLE_NAME, ID_COLUMN, TUTOR_PERSONAL_COLUMN);

    public static final String DELETE_TABLE_TEMPLATE = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
}
