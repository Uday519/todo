package com.uday.todo.models;

public class Taskpojo {

    public String task;
    public Boolean status;
    public Integer id;

    public Taskpojo(String task, Boolean status, Integer id){
        this.task = task;
        this.status = status;
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
