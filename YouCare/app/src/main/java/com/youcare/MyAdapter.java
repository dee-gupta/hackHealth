package com.youcare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Deepak Gupta on 2/3/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Person> personList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvPositive, tvNegative, tvBottom;
        ProgressBar pbPositive, pbNegative;
        ImageView ivColor;

        public MyViewHolder(View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tv_user_name);
            tvPositive = view.findViewById(R.id.tv_positive_value);
            tvBottom = view.findViewById(R.id.tv_bottom);
            tvNegative = view.findViewById(R.id.tv_negative_value);
            pbNegative = view.findViewById(R.id.pb_negative);
            pbPositive = view.findViewById(R.id.pb_positive);
            ivColor = view.findViewById(R.id.iv_color);
        }
    }


    public MyAdapter(List<Person> persons, Context context) {
        this.personList = persons;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.tvUserName.setText(person.getName());
        holder.tvPositive.setText(String.valueOf(person.getPositive()) + "%");
        holder.tvNegative.setText(String.valueOf(person.getNegative()) + "%");
        setImageAndProgressBar(holder, person);
    }

    private void setImageAndProgressBar(MyViewHolder holder, Person person) {
        holder.pbPositive.setProgress((int) person.getPositive());
        holder.pbNegative.setProgress((int) person.getNegative());
        if (person.getScore() < 0.5f) {
            holder.ivColor.setImageDrawable(context.getResources().getDrawable(R.drawable.green));
            holder.tvBottom.setText(" Keep in touch with your loved one. Try to take a trip with them once a while.");
        } else if (person.getScore() < 0.8f) {
            holder.ivColor.setImageDrawable(context.getResources().getDrawable(R.drawable.yellow));
            holder.tvBottom.setText("Something is bothering your loved one. Let them know they are liked, loved and appreciated.");
        } else {
            holder.ivColor.setImageDrawable(context.getResources().getDrawable(R.drawable.red));
            holder.tvBottom.setText("Your loved one needs immediate attention and care. Provide time and space to talk to them.");
        }
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }
}


