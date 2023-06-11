package com.example.sueobmwodeudji.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sueobmwodeudji.R;
import com.example.sueobmwodeudji.databinding.ItemRatingsListBinding;
import com.example.sueobmwodeudji.dto.CommunitySubCommentModel;
import com.example.sueobmwodeudji.dto.RatingsSubListModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class RatingsSubListAdapter extends RecyclerView.Adapter<RatingsSubListAdapter.RatingsSubListViewHolder> implements EventListener<QuerySnapshot> {
    private final Context context;
    private final Query mQuery;
    private ArrayList<RatingsSubListModel> ratingsSubListModel = new ArrayList<>();

    private static OnRatingClickListener orcl;

    public RatingsSubListAdapter(Context context, Query query) {
        this.context = context;
        this.mQuery = query;
        mQuery.addSnapshotListener(this);
    }

    @NonNull
    @Override
    public RatingsSubListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_ratings_list, parent, false);
        RatingsSubListViewHolder viewHolder = new RatingsSubListViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RatingsSubListViewHolder holder, int position) {
        holder.onBind(ratingsSubListModel.get(position));
    }

    @Override
    public int getItemCount() {
        return ratingsSubListModel.size();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
        if(e != null){
            Log.w("list 에러","onEvent:error", e);
        }
        int i =0;
        ratingsSubListModel.clear();
        for(DocumentSnapshot doc : documentSnapshots.getDocuments()){
            ratingsSubListModel.add(doc.toObject(RatingsSubListModel.class));
            notifyDataSetChanged();
        }
    }

    public void setOrcl(OnRatingClickListener orcl) {
        RatingsSubListAdapter.orcl = orcl;
    }
    public interface OnRatingClickListener{
        void onClick(RatingsSubListModel data);
    }

    public static class RatingsSubListViewHolder extends RecyclerView.ViewHolder {
        Context context;
        private final TextView title;
        private final TextView sub_title, likeTv, commentTv;
        private final ConstraintLayout layout;
        private final ImageView difficultyIv, typeIv;

        RatingsSubListModel mData;

        public RatingsSubListViewHolder(Context _context, View itemView) {
            super(itemView);
            ItemRatingsListBinding binding = ItemRatingsListBinding.bind(itemView);

            context = _context;
            title = binding.titleTv;
            sub_title = binding.subTitleTv;
            likeTv = binding.likeTv;
            commentTv = binding.commentTv;
            layout = binding.layout;
            difficultyIv = binding.difficultyIv;
            typeIv = binding.typeIv;
        }
        public void onBind(RatingsSubListModel data){
            mData = data;


            int like_count = likeCounting();
            int comment_count = commentCounting();

            title.setText(data.getTitle());
            sub_title.setText(data.getContent());
            likeTv.setText(like_count + "");
            commentTv.setText(comment_count + "");
            switch (data.getDifficulty()) {
                case "상" : difficultyIv.setImageResource(R.drawable.hard); break;
                case "중" : difficultyIv.setImageResource(R.drawable.normal); break;
                case "하" : difficultyIv.setImageResource(R.drawable.easy); break;
            }

            switch (data.getType()) {
                case "유인물 위주" : typeIv.setImageResource(R.drawable.with_handouts); break;
                case "ppt 위주" :   typeIv.setImageResource(R.drawable.with_ppt); break;
                case "교과서 위주" : typeIv.setImageResource(R.drawable.with_textbook); break;
                case "기타 방식" :   typeIv.setImageResource(R.drawable.etc); break;
            }

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orcl.onClick(data);
                }
            });
        }

        private int likeCounting() {
            if (mData.getLike() == null) return 0;

            int total = 0;
            Map<String, Boolean> map = mData.getLike();

            for (String key : map.keySet()) {
                total += (map.get(key)) ? 1 : 0;
            }

            return total;
        }
        private int commentCounting() {
            int total = mData.getComments().size();
            for (CommunitySubCommentModel data : mData.getComments()) {
                total += data.getCommentModels().size();
            }
            return total;
        }
    }
}