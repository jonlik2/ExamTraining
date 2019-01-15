package sample;

import sample.model.Task;

import java.util.List;

public class RepositorySingleton {

    private static RepositorySingleton ourInstance = new RepositorySingleton();

    private List<Task> tasks;

    public static RepositorySingleton getInstance() {
        return ourInstance;
    }

    private RepositorySingleton() {
    }


}
