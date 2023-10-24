package com.tasks.taskmanager.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tasks.taskmanager.R;
import com.tasks.taskmanager.activity.TaskDetails;
import com.tasks.taskmanager.activity.model.Task;

import java.util.List;

public class TasksListRecyclerViewAdapter extends RecyclerView.Adapter<TasksListRecyclerViewAdapter.TasksListViewHolder> {

    List<Task> tasks;

    Context callingActivity;

    public TasksListRecyclerViewAdapter(List<Task> tasks, Context callingActivity) {
        this.tasks = tasks;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public TasksListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tasksFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tasks_list, parent, false);

        return new TasksListViewHolder(tasksFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksListViewHolder holder, int position) {

        TextView taskFragmentTextViewTitle = (TextView) holder.itemView.findViewById(R.id.listFragmentTextViewTitle);
        TextView taskFragmentTextViewState = (TextView) holder.itemView.findViewById(R.id.listFragmentTextViewState);

        String taskTitle = tasks.get(position).getTitle();
        String taskBody = tasks.get(position).getBody();
        String taskState = tasks.get(position).getState().toString();

        taskFragmentTextViewTitle.setText(taskTitle);
        taskFragmentTextViewState.setText(taskState);

        View tasksViewHolder = holder.itemView;
        tasksViewHolder.setOnClickListener(v -> {
            Intent goToTaskDetailsIntent = new Intent(callingActivity, TaskDetails.class);
            goToTaskDetailsIntent.putExtra("taskTitle", taskTitle);
            goToTaskDetailsIntent.putExtra("taskBody", taskBody);
            goToTaskDetailsIntent.putExtra("taskState", taskState);

            callingActivity.startActivity(goToTaskDetailsIntent);
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TasksListViewHolder extends RecyclerView.ViewHolder{

        public TasksListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
