package com.example.ecommerce_task;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce_task.API.RetrofitClient;
import com.example.ecommerce_task.API.TokenManager;
import com.example.ecommerce_task.Product.Product;
import com.example.ecommerce_task.Product.ProductAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_explore = view.findViewById(R.id.btn_explore);
        RecyclerView recyclerView = view.findViewById(R.id.rv_products);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setNestedScrollingEnabled(false);

        String token = new TokenManager(getContext()).getToken();
        RetrofitClient.getApi()
                .getProducts("Bearer " + token)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call,
                                           @NonNull Response<List<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Product> all = response.body();
                            List<Product> first6 = all.subList(0, Math.min(6, all.size()));
                            ProductAdapter adapter = new ProductAdapter(new ArrayList<>(first6));

                            adapter.setOnProductClickListener(product -> {
                                String productJson = new Gson().toJson(product);
                                Intent intent = new Intent(getContext(), ProductDetail.class);
                                intent.putExtra(ProductDetail.EXTRA_PRODUCT, productJson);
                                startActivity(intent);
                            });

                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        EditText edSearch = view.findViewById(R.id.et_search);
        edSearch.setFocusable(false);
        edSearch.setOnClickListener(v -> {
            ((NavigationActivity) requireActivity()).switchToSearch();
        });

        btn_explore.setOnClickListener(view1 -> {
            ((NavigationActivity) requireActivity()).switchToSearch();
        });
    }
}