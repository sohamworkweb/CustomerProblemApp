package com.example.customerproblemapp;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CustomerProblem implements Serializable {

   @SerializedName("Id")
   private Integer id;

    @SerializedName("CustomerName")
    private String customerName;

    @SerializedName("ProductName")
    private String productName;

    @SerializedName("Problem")
    private String problem;

    @SerializedName("PhoneNumber")
    private String phoneNumber;

    @SerializedName("QueryPerson")
    private String queryPerson;

    @SerializedName("EngineerName")
    private String engineerName;

    @SerializedName("DateCreated")
    private String dateCreated;

    @SerializedName("Status")
    private String status;

    // ✅ Constructor with all fields (for GET/PUT operations)
    public CustomerProblem(int id, String customerName, String productName, String problem,
                           String phoneNumber, String queryPerson, String engineerName,
                           String dateCreated, String status) {
        this.id = id;
        this.customerName = customerName;
        this.productName = productName;
        this.problem = problem;
        this.phoneNumber = phoneNumber;
        this.queryPerson = queryPerson;
        this.engineerName = engineerName;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    // ✅ Constructor without ID (for POST insert)
    public CustomerProblem(String customerName, String productName, String problem,
                           String phoneNumber, String queryPerson, String engineerName,
                           String dateCreated, String status) {
        this.customerName = customerName;
        this.productName = productName;
        this.problem = problem;
        this.phoneNumber = phoneNumber;
        this.queryPerson = queryPerson;
        this.engineerName = engineerName;
        this.dateCreated = dateCreated;
        this.status = status;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProblem() { return problem; }
    public void setProblem(String problem) { this.problem = problem; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getQueryPerson() { return queryPerson; }
    public void setQueryPerson(String queryPerson) { this.queryPerson = queryPerson; }

    public String getEngineerName() { return engineerName; }
    public void setEngineerName(String engineerName) { this.engineerName = engineerName; }

    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
