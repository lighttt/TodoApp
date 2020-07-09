package np.com.manishtuladhar.todo.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConvertor {

    @TypeConverter
    public static Date toDate(Long timestamp)
    {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static long toTimeStamp(Date date)
    {
        return date == null ? null : date.getTime();
    }
}
