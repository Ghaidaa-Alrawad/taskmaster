package com.tasks.taskmanager.activity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tasks.taskmanager.activity.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    public void insertTask(Task task);

    @Query("select * from Task")
    public List<Task> findAll();

    @Query("select * from Task ORDER BY title ASC")
    public List<Task> findAllSortedByTitle();

    @Query("select * from Task where id = :id")
    Task findById(long id);
}
