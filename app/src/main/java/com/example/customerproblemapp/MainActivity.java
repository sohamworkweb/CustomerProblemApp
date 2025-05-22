package com.example.customerproblemapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.SearchView; // ✅ Correct version
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomerProblemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView); // <-- initialize this!
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchView = findViewById(R.id.search_view);

        FloatingActionButton fab = findViewById(R.id.fabAddProblem);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InsertCustomerProblemActivity.class);
            startActivity(intent);
        });

        setupSwipeToDelete();
        fetchCustomerProblems();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CustomerProblem problem = adapter.getCustomerProblemAt(position);

                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            deleteProblemFromServer(problem.getId(), position);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            adapter.notifyItemChanged(position); // Restore the item
                            dialog.dismiss();
                        })
                        .setCancelable(false)
                        .show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void fetchCustomerProblems() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<CustomerProblem>> call = apiInterface.getCustomerProblems();

        call.enqueue(new Callback<List<CustomerProblem>>() {
            @Override
            public void onResponse(Call<List<CustomerProblem>> call, Response<List<CustomerProblem>> response) {
                if (response.isSuccessful()) {
                    List<CustomerProblem> customerProblems = response.body();
                    Log.d("MainActivity", "Fetched items: " + customerProblems.size());

                    adapter = new CustomerProblemAdapter(customerProblems, customerProblem -> {
                        Intent intent = new Intent(MainActivity.this, InsertCustomerProblemActivity.class);
                        intent.putExtra("customerProblem", customerProblem);
                        startActivity(intent);
                    });

                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CustomerProblem>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteProblemFromServer(int id, int position) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.deleteCustomerProblem(id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    adapter.removeItem(position);
                    Toast.makeText(MainActivity.this, "✅ Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "❌ Delete failed", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemChanged(position); // Restore item
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "❌ Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                adapter.notifyItemChanged(position); // Restore item
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCustomerProblems(); // Refresh on return
    }
}
