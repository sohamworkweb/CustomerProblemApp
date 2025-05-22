package com.example.customerproblemapp;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertCustomerProblemActivity extends AppCompatActivity {

    private EditText edtCustomerName, edtProductName, edtProblem, edtPhoneNumber, edtQueryPerson, edtEngineerName, edtStatus;

    private Integer existingId = null;
    private String existingDateCreated = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_customer_problem);

        edtCustomerName = findViewById(R.id.edtCustomerName);

        edtProductName = findViewById(R.id.edtProductName);
        edtProblem = findViewById(R.id.edtProblem);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtQueryPerson = findViewById(R.id.edtQueryPerson);
        edtEngineerName = findViewById(R.id.edtEngineerName);
        edtStatus = findViewById(R.id.edtStatus);
        Button btnSave = findViewById(R.id.btnSave);

        CustomerProblem existingProblem = (CustomerProblem) getIntent().getSerializableExtra("customerProblem");
        if (existingProblem != null) {
            existingId = existingProblem.getId();
            existingDateCreated = existingProblem.getDateCreated();
            edtCustomerName.setText(existingProblem.getCustomerName());
            edtProductName.setText(existingProblem.getProductName());
            edtProblem.setText(existingProblem.getProblem());
            edtPhoneNumber.setText(existingProblem.getPhoneNumber());
            edtQueryPerson.setText(existingProblem.getQueryPerson());
            edtEngineerName.setText(existingProblem.getEngineerName());
            edtStatus.setText(existingProblem.getStatus());
        }

        btnSave.setOnClickListener(v -> saveCustomerProblem());
    }

    private void saveCustomerProblem() {
        String customerName = edtCustomerName.getText().toString().trim();
        String productName = edtProductName.getText().toString().trim();
        String problem = edtProblem.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String queryPerson = edtQueryPerson.getText().toString().trim();
        String engineerName = edtEngineerName.getText().toString().trim();
        String status = edtStatus.getText().toString().trim();

        if (TextUtils.isEmpty(customerName) || TextUtils.isEmpty(problem) || TextUtils.isEmpty(status)) {
            Toast.makeText(this, "Please fill required fields: Customer Name, Problem, and Status", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phoneNumber.matches("\\d{10}")) {
            Toast.makeText(this, "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        String dateCreated = existingDateCreated != null
                ? existingDateCreated
                : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());

        CustomerProblem customerProblem = new CustomerProblem(
                existingId,
                customerName,
                productName,
                problem,
                phoneNumber,
                queryPerson,
                engineerName,
                dateCreated,
                status
        );

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (existingId == null) {
            Call<CustomerProblem> call = apiInterface.createCustomerProblem(customerProblem);
            call.enqueue(new Callback<CustomerProblem>() {
                @Override
                public void onResponse(Call<CustomerProblem> call, Response<CustomerProblem> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(InsertCustomerProblemActivity.this, "✅ Customer problem saved", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("API_ERROR", "Insert Error: " + errorBody);
                        } catch (Exception e) {
                            Log.e("API_ERROR", "Insert Error: Unable to parse error body", e);
                        }
                        Toast.makeText(InsertCustomerProblemActivity.this, "❌ Failed to insert. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CustomerProblem> call, Throwable t) {
                    Log.e("API_ERROR", "Insert failed", t);
                    Toast.makeText(InsertCustomerProblemActivity.this, "❌ Failed to insert: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Call<ResponseBody> call = apiInterface.updateCustomerProblem(existingId, customerProblem);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(InsertCustomerProblemActivity.this, "✅ Customer problem updated", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("API_ERROR", "Update Error: " + errorBody);
                        } catch (Exception e) {
                            Log.e("API_ERROR", "Update Error: Unable to parse error body", e);
                        }
                        Toast.makeText(InsertCustomerProblemActivity.this, "❌ Failed to update. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("API_ERROR", "Update failed", t);
                    Toast.makeText(InsertCustomerProblemActivity.this, "❌ Failed to update: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
