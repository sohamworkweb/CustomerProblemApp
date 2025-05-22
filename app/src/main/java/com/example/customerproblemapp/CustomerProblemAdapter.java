package com.example.customerproblemapp;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomerProblemAdapter extends RecyclerView.Adapter<CustomerProblemAdapter.CustomerProblemViewHolder>
        implements Filterable {

    private List<CustomerProblem> customerProblems;
    private List<CustomerProblem> customerProblemsFull; // Backup for filtering
    private final OnCustomerProblemClickListener listener;
    private String lastSearchQuery = "";

    // Define the click listener interface
    public interface OnCustomerProblemClickListener {
        void onCustomerProblemClick(CustomerProblem customerProblem);
    }

    // Adapter constructor
    public CustomerProblemAdapter(List<CustomerProblem> customerProblems, OnCustomerProblemClickListener listener) {
        this.customerProblems = customerProblems;
        this.customerProblemsFull = new ArrayList<>(customerProblems); // deep copy for search
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomerProblemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer_problem, parent, false);
        return new CustomerProblemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerProblemViewHolder holder, int position) {
        CustomerProblem customerProblem = customerProblems.get(position);

        highlightText(holder.customerName, customerProblem.getCustomerName(), lastSearchQuery);
        highlightText(holder.productName, customerProblem.getProductName(), lastSearchQuery);
        highlightText(holder.problem, customerProblem.getProblem(), lastSearchQuery);
        highlightText(holder.phoneNumber, customerProblem.getPhoneNumber(), lastSearchQuery);
        highlightText(holder.queryPerson, customerProblem.getQueryPerson(), lastSearchQuery);
        highlightText(holder.engineerName, customerProblem.getEngineerName(), lastSearchQuery);
        highlightText(holder.status, customerProblem.getStatus(), lastSearchQuery);

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCustomerProblemClick(customerProblem);
            }
        });
    }
    private void highlightText(TextView textView, String originalText, String query) {
        if (query == null || query.isEmpty() || originalText == null) {
            textView.setText(originalText);
            return;
        }

        String lowerOriginal = originalText.toLowerCase();
        String lowerQuery = query.toLowerCase();

        int start = lowerOriginal.indexOf(lowerQuery);
        if (start < 0) {
            textView.setText(originalText);
            return;
        }

        int end = start + query.length();

        SpannableString spannable = new SpannableString(originalText);
        spannable.setSpan(
                new ForegroundColorSpan(Color.RED),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textView.setText(spannable);
    }


    @Override
    public int getItemCount() {
        return customerProblems.size();
    }
    public CustomerProblem getCustomerProblemAt(int position) {
        return customerProblems.get(position);
    }

    public void removeItem(int position) {
        customerProblems.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public Filter getFilter() {
        return customerProblemFilter;
    }

    private final Filter customerProblemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CustomerProblem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(customerProblemsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CustomerProblem item : customerProblemsFull) {
                    if ((item.getCustomerName() != null && item.getCustomerName().toLowerCase().contains(filterPattern)) ||
                            (item.getProductName() != null && item.getProductName().toLowerCase().contains(filterPattern)) ||
                            (item.getProblem() != null && item.getProblem().toLowerCase().contains(filterPattern)) ||
                            (item.getPhoneNumber() != null && item.getPhoneNumber().toLowerCase().contains(filterPattern)) ||
                            (item.getQueryPerson() != null && item.getQueryPerson().toLowerCase().contains(filterPattern)) ||
                            (item.getEngineerName() != null && item.getEngineerName().toLowerCase().contains(filterPattern)) ||
                            (item.getDateCreated() != null && item.getDateCreated().toLowerCase().contains(filterPattern)) ||
                            (item.getStatus() != null && item.getStatus().toLowerCase().contains(filterPattern))) {

                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            customerProblems.clear();
            customerProblems.addAll((List<CustomerProblem>) results.values);
            lastSearchQuery = constraint != null ? constraint.toString() : "";
            notifyDataSetChanged();
        }
    };


    public static class CustomerProblemViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, productName, problem, phoneNumber, queryPerson, engineerName, status;

        public CustomerProblemViewHolder(@NonNull View view) {
            super(view);
            customerName = view.findViewById(R.id.customerName);
            productName = view.findViewById(R.id.productName);
            problem = view.findViewById(R.id.problem);
            phoneNumber = view.findViewById(R.id.phoneNumber);
            queryPerson = view.findViewById(R.id.queryPerson);
            engineerName = view.findViewById(R.id.engineerName);
            status = view.findViewById(R.id.status);
        }
    }
}
