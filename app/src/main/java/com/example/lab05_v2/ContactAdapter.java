package com.example.lab05_v2;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Person[] contacts;

    public void setContacts(Person[] contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        if (contacts != null && position < contacts.length) {
            Person contact = contacts[position];
            holder.bind(contact);
        }
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.length : 0;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView idTextView;
        private TextView nameTextView;
        private TextView dobTextView;
        private TextView emailTextView;

        private ImageView imageView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dobTextView = itemView.findViewById(R.id.dobTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            imageView = itemView.findViewById(R.id.imageViewAvatar);
        }

        public void bind(Person contact) {
            idTextView.setText("ID: " + String.valueOf(contact.getId()));
            nameTextView.setText("Name: " + contact.getName());
            dobTextView.setText("Date of Birth: " + contact.getDob());
            emailTextView.setText("Email: " + contact.getEmail());
            imageView.setImageURI(Uri.parse("file://" + contact.getAvatar()));
        }
    }
}