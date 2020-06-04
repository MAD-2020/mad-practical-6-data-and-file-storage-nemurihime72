package sg.edu.np.week_6_whackamole_3_0;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page

     */
    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    UserData userData;
    String userName;
    ArrayList<Integer> levelList;
    ArrayList<Integer> scoreList;

    public CustomScoreAdaptor(UserData userdata){
        /* Hint:
        This method takes in the data and readies it for processing.
         */
        userData = userdata;
        userName = userdata.getMyUserName();
        levelList = userdata.getLevels();
        scoreList = userdata.getScores();
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        /* Hint:
        This method dictates how the viewholder layuout is to be once the viewholder is created.
         */
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_select, parent, false);
        CustomScoreViewHolder holder = new CustomScoreViewHolder(item);
        return holder;
    }

    public void onBindViewHolder(final CustomScoreViewHolder holder, final int position){

        /* Hint:
        This method passes the data to the viewholder upon bounded to the viewholder.
        It may also be used to do an onclick listener here to activate upon user level selections.

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
         */
        String level = levelList.get(position).toString();
        String score = scoreList.get(position).toString();
        holder.level.setText("Level " + level);
        holder.score.setText("Highest Score: " + score);
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int levelChosen = levelList.get(holder.getAdapterPosition());
                Intent intent = new Intent(holder.level.getContext(), Main4Activity.class);
                intent.putExtra("username", userName);
                intent.putExtra("level", levelChosen);
                holder.level.getContext().startActivity(intent);
                return false;
            }
        });
    }

    public int getItemCount(){
        /* Hint:
        This method returns the the size of the overall data.
         */
        return levelList.size();
    }
}