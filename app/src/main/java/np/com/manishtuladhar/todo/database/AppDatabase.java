package np.com.manishtuladhar.todo.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TaskEntry.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";
    private static final String DATABASE_NAME = "todolist";
    private static final Object LOCK = new Object();

    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if(sInstance == null)
        {
            synchronized (LOCK)
            {
                Log.e(TAG, "getsInstance: Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
            }
        }
        Log.e(TAG, "getsInstance: Getting the database instance");
        return sInstance;
    }

    public abstract TaskDao taskDao();
}
