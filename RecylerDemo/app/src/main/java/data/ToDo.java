package data;

public class ToDo {


    private String todoText;
    private boolean done;

    public void setTodoText(String todoText) {
        this.todoText = todoText;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public ToDo(String todoText, boolean done) {
        this.todoText = todoText;
        this.done = done;
    }

    public String getTodoText() {
        return todoText;
    }

    public boolean isDone() {
        return done;
    }

}
