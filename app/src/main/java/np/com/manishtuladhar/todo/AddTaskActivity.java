package np.com.manishtuladhar.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.Date;

import np.com.manishtuladhar.todo.database.AppDatabase;
import np.com.manishtuladhar.todo.database.TaskEntry;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskActivity";

    //intents
    public static final String EXTRA_TASK_ID = "extraTaskId";
    private static final int DEFAULT_TASK_ID = -1;
    private int mTaskId = DEFAULT_TASK_ID;

    //save instance
    private static final String INSTANCE_TASK_ID = "instanceTaskId";

    //variable and views
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;

    //database
    private AppDatabase mDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        mEditText = findViewById(R.id.etTaskDescription);
        mRadioGroup = findViewById(R.id.radioGroup);
        mButton = findViewById(R.id.addButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });

        mDB = AppDatabase.getsInstance(getApplicationContext());

        //save instance
        if(savedInstanceState!=null && savedInstanceState.containsKey(INSTANCE_TASK_ID))
        {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID,DEFAULT_TASK_ID);
        }

        //intents
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update);
            if (mTaskId == DEFAULT_TASK_ID) {
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);

                //single task lina lai
                //live data for a single task entry
                final LiveData<TaskEntry> task = mDB.taskDao().loadTaskById(mTaskId);
                //observe the data for change and update the ui
                task.observe(this, new Observer<TaskEntry>() {
                    @Override
                    public void onChanged(TaskEntry taskEntry) {
                        //remove the observer since we dont need it after we update
                        task.removeObserver(this);
                        populateUI(taskEntry);
                    }
                });
            }
        }
    }

    /**
     * Handling saving state of the app
      */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID,mTaskId);
        super.onSaveInstanceState(outState);
    }

    /**
     * Populating our UI with data if it is from update
     */
    void populateUI(TaskEntry task) {
        if (task == null) {
            return;
        }
        //set
        mEditText.setText(task.getDescription());
        setPriorityInViews(task.getPriority());
    }


    /**
     * Adding new task to the list
     */
    public void onSaveButtonClicked() {
        String description = mEditText.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();

        //adding new object
        final TaskEntry taskEntry = new TaskEntry(description, priority, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mTaskId == DEFAULT_TASK_ID) {
                    mDB.taskDao().insertTask(taskEntry);
                } else {
                    taskEntry.setId(mTaskId);
                    mDB.taskDao().updateTask(taskEntry);
                }
                //close activity
                finish();
            }
        });
    }

    /**
     * Getting which priority is clicked
     */
    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }

    /**
     * Setting priority clicked
     */
    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }
}