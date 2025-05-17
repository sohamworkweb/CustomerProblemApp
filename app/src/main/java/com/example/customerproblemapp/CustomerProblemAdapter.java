package com.example.customerproblemapp;

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

        holder.customerName.setText(customerProblem.getCustomerName());
        holder.productName.setText(customerProblem.getProductName());
        holder.problem.setText(customerProblem.getProblem());
        holder.phoneNumber.setText(customerProblem.getPhoneNumber());
        holder.queryPerson.setText(customerProblem.getQueryPerson());
        holder.engineerName.setText(customerProblem.getEngineerName());
        holder.status.setText(customerProblem.getStatus());

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCustomerProblemClick(customerProblem);
            }
        });
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
                    if (item.getCustomerName().toLowerCase().contains(filterPattern) ||
                            item.getProductName().toLowerCase().contains(filterPattern) ||
                            item.getProblem().toLowerCase().contains(filterPattern)) {
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
            customerProblems.addAll((List) results.values);
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
