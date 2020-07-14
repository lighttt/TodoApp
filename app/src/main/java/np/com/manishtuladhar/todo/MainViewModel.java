package np.com.manishtuladhar.todo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

import np.com.manishtuladhar.todo.database.AppDatabase;
import np.com.manishtuladhar.todo.database.TaskEntry;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    //live data
    private LiveData<List<TaskEntry>> tasks;

    public MainViewModel(@NonNull Application application) {
        super(application);
        //database call and load the tasks then tasks set
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        Log.e(TAG, "MainViewModel: Retreiving tasks from the database" );
        tasks = database.taskDao().loadAllTasks();
    }

    public LiveData<List<TaskEntry>> getTasks() {
        return tasks;
    }
}
