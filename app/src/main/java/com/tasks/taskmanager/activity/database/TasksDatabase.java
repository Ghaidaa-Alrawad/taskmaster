package com.tasks.taskmanager.activity.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.tasks.taskmanager.activity.dao.TaskDao;
import com.tasks.taskmanager.activity.model.Task;

@Database(entities = {Task.class}, version = 1)
@TypeConverters({TasksDatabaseConverters.class})
public abstract class TasksDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();
}
