package com.example.chatapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.R;
import com.example.chatapplication.databinding.UsersItemLayoutBinding;
import com.example.chatapplication.models.User;
import com.example.chatapplication.models.UserApi;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder>{
    public interface OnUserClickListener {
        void onUserClicked(User user);
    }

    ArrayList<User> usersList;
    private final OnUserClickListener onUserClickListener;


    public UsersAdapter(ArrayList<User> usersList, OnUserClickListener onUserClickListener) {
        this.usersList = usersList;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UsersItemLayoutBinding usersItemLayoutBinding =
                UsersItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new UsersViewHolder(usersItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.usersItemLayoutBinding.textViewUserItem.setText(user.getUsername());

        String imageUrl = "http://10.0.2.2:8080/api/profile/image?userId=" + user.getId();

        //Glide:
        //    downloads it asynchronously
        //    handles caching
        //    handles scaling and fallback on error
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.default_profile_photo)
                .error(R.drawable.default_profile_photo)
                .into(holder.usersItemLayoutBinding.imageViewUserItem);

        //that URL goes to our backend which should return the image
        // from user.getProfileImage() (byte[]).
        // Glide takes care of turning the response into a Bitmap
        // and loading it into the ImageView.

        holder.usersItemLayoutBinding.LinearLayoutuserItem.setOnClickListener(v -> {
            onUserClickListener.onUserClicked(user);
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        UsersItemLayoutBinding usersItemLayoutBinding;
        public UsersViewHolder(@NonNull UsersItemLayoutBinding usersItemLayoutBinding) {
            super(usersItemLayoutBinding.getRoot());

            this.usersItemLayoutBinding = usersItemLayoutBinding;
        }
    }




}
