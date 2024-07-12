package com.example.senbit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.CodeViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Code code);
    }

    private List<Code> codeList;
    private OnItemClickListener listener;

    public CodeAdapter(List<Code> codeList, OnItemClickListener listener) {
        this.codeList = codeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_code, parent, false);
        return new CodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolder holder, int position) {
        Code code = codeList.get(position);
        holder.bind(code, listener);
    }

    @Override
    public int getItemCount() {
        return codeList.size();
    }

    static class CodeViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewOwner;
        private TextView textViewDescription;

        private TextView textViewLanguage;
        private TextView textViewCodeContent;


        public CodeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewOwner = itemView.findViewById(R.id.textViewOwner);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewLanguage = itemView.findViewById(R.id.textViewLanguage);
            textViewCodeContent = itemView.findViewById(R.id.textViewCodeContent);
        }
        public void bind(final Code code, final OnItemClickListener listener) {
            textViewTitle.setText(code.getTitle());
            textViewDescription.setText(code.getDescription());
            textViewOwner.setText(code.getOwner());
            textViewLanguage.setText(code.getLanguage());
            textViewCodeContent.setText(code.getCodeContent());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(code);
                }
            });
        }
    }
}
