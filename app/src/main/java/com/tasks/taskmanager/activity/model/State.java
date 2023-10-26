package com.tasks.taskmanager.activity.model;

import org.jetbrains.annotations.NonNls;

public enum State {

    NEW("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete");

    private final String taskText;

    State(String taskText) {
        this.taskText = taskText;
    }

    public String getTaskText() {
        return taskText;
    }

    public static State fromString(String possibleProductText){
        for(State state: State.values()){
            if (state.taskText.equals(possibleProductText)){
                return state;
            }
        }
        return null;
    }

    @NonNls
    @Override
    public String toString(){
        if (taskText == null){
            return "";
        }
        return taskText;
    }
}
