package np.com.manishtuladhar.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import np.com.manishtuladhar.todo.database.AppDatabase;
import np.com.manishtuladhar.todo.database.TaskEntry;

public class MainActivity extends AppCompatActivity implements TaskAdapter.ItemClickListener {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

    private AppDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //rv set
        mRecyclerView = findViewById(R.id.recyclerViewTasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TaskAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        //swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<TaskEntry> taskEntryList = mAdapter.getTasks();
                        mDB.taskDao().deleteTask(taskEntryList.get(position));
                    }
                });

            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(addTaskIntent);
            }
        });
        //initialize
        mDB = AppDatabase.getsInstance(getApplicationContext());

        //retrieving here instead of resume method
        setupViewModel();
    }

    private void setupViewModel() {

        //declare and initialize view model for loading tasks
        MainViewModel viewModel =  new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory
                        .getInstance(this.getApplication()))
                .get(MainViewModel.class);

        //observe : tasks observe and runonuithread adapter set tasks
        viewModel.getTasks().observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(List<TaskEntry> taskEntries) {
                Log.d(TAG, "onChanged: Recieved tasks from the database");
                mAdapter.setTasks(taskEntries);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        //while updating tasks
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID, itemId);
        startActivity(intent);
    }
}