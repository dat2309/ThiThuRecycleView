package com.example.thithurecycleview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<Customer> customers;
    private Context context;
    private String url = "https://60b08c541f26610017ffe5f1.mockapi.io/Cus";

    public MyAdapter(Context context) {
        this.context = context;
        update();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Customer customer = customers.get(position);

        holder.firstName.setText("First Name: " + customer.getFirstName());
        holder.lastName.setText("Last Name: " + customer.getLastName());
        holder.email.setText("Email: " + customer.getEmail());

        holder.btnDelete.setOnClickListener(v -> {
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url + "/" + customer.getId(),
                    response -> {
                        Toast.makeText(context, "Xóa thành công student", Toast.LENGTH_SHORT).show();
                        update();
                    },
                    error -> {
                        error.printStackTrace();
                    });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1));
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(stringRequest);
        });

        holder.btnEdit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("customer", customer);
            Intent intent = new Intent(context, FormCustomer.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void update() {
        customers = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            try {
                for (int i = response.length() - 1; i >= 0; i--) {
                    JSONObject object = (JSONObject) response.get(i);

                    int id = object.getInt("id");
                    String fName = object.getString("firstName");
                    String lName = object.getString("lastName");
                    String email = object.getString("email");

                    customers.add(new Customer(id, fName, lName, email));
                    Collections.sort(customers, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            Customer p1 = (Customer) o1;
                            Customer p2 = (Customer) o2;
                            return new Integer(p1.getId()).compareTo(new Integer(p2.getId()));
                        }
                    });
                }
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, 1, 1));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArrayRequest);
    }

}
