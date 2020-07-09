package np.com.manishtuladhar.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context mContext;
    final private ItemClickListener mItemClickListner;

    public TaskAdapter(Context context,ItemClickListener itemClickListener)
    {
        this.mContext = context;
        this.mItemClickListner = itemClickListener;
    }


    // =================== ONCLICK ====================

    public interface ItemClickListener{
        void onItemClickListener(int itemId);
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView taskDescriptionView;
        TextView updatedAtView;
        TextView priorityView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskDescriptionView = itemView.findViewById(R.id.taskDescription);
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt);
            priorityView = itemView.findViewById(R.id.priorityTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
