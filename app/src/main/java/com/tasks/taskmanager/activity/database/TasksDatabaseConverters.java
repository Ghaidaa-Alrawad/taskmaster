package com.tasks.taskmanager.activity.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class TasksDatabaseConverters {

    @TypeConverter
    public static Date fromTimStamp(Long value){
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimStamp(Date date){
        return date == null ? null : date.getTime();
    }
}
