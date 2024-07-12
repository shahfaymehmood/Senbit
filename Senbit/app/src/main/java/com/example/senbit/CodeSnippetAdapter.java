package com.example.senbit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CodeSnippetAdapter extends RecyclerView.Adapter<CodeSnippetAdapter.CodeSnippetViewHolder> {

    private final List<CodeSnippet> codeSnippetList;
    private final Context context;

    public CodeSnippetAdapter(List<CodeSnippet> codeSnippetList, Context context) {
        this.codeSnippetList = codeSnippetList;
        this.context = context;
    }

    @NonNull
    @Override
    public CodeSnippetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_code_snippet, parent, false);
        return new CodeSnippetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeSnippetViewHolder holder, int position) {
        CodeSnippet codeSnippet = codeSnippetList.get(position);
        holder.codeTitle.setText(codeSnippet.getTitle());
        holder.codeOwner.setText(codeSnippet.getcodeOwner());
        holder.codeDescription.setText(codeSnippet.getcodeDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BitActivity.class);
                intent.putExtra("id", codeSnippet.getId());
                intent.putExtra("title", codeSnippet.getTitle());
                intent.putExtra("owner", codeSnippet.getcodeOwner());
                intent.putExtra("description", codeSnippet.getcodeDescription());
                intent.putExtra("programmingLanguage", codeSnippet.getProgrammingLanguage());
                intent.putExtra("content", codeSnippet.getContent());
                intent.putExtra("likedBy", codeSnippet.getLikedBy());
                intent.putExtra("dislikedBy", codeSnippet.getDislikedBy());
//                intent.putExtra("likedBy", codeSnippet.getLikedBy());
//                intent.putExtra("disLikedBy", codeSnippet.getDislikedBy());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return codeSnippetList.size();
    }

    public static class CodeSnippetViewHolder extends RecyclerView.ViewHolder {
        TextView codeTitle;
        TextView codeOwner;
        TextView codeDescription;

        public CodeSnippetViewHolder(@NonNull View itemView) {
            super(itemView);
            codeTitle = itemView.findViewById(R.id.codeTitle);
            codeOwner = itemView.findViewById(R.id.codeOwner);
            codeDescription = itemView.findViewById(R.id.codeDescription);
        }
    }
}
