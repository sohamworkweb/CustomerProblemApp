package com.example.customerproblemapp;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;

public interface ApiInterface {

    // Get list of customer problems
    @GET("customer-problems")  // Replace with your API endpoint
    Call<List<com.example.customerproblemapp.CustomerProblem>> getCustomerProblems();

    // Insert a new customer problem
    @POST("customer-problems")  // Replace with your API endpoint
    Call<com.example.customerproblemapp.CustomerProblem> createCustomerProblem(@Body com.example.customerproblemapp.CustomerProblem customerProblem);

    // Update an existing customer problem
   /* @PUT("customer-problems")  // Replace with your API endpoint
    Call<com.example.customerproblemapp.CustomerProblem> updateCustomerProblem(@Path("id") int id, @Body com.example.customerproblemapp.CustomerProblem customerProblem);*/
    @PUT("customer-problems/{id}")
    Call<ResponseBody> updateCustomerProblem(@Path("id") int id, @Body CustomerProblem customerProblem);

    /*@DELETE("customer-problems/{id}")
    Call<ResponseBody> deleteCustomerProblem(@Path("id") int id);*/

    @DELETE("customer-problems/{id}")
    Call<ResponseBody> deleteCustomerProblem(@Path("id") int id);
}
