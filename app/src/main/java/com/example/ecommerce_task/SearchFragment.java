package com.example.ecommerce_task;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private EditText etSearch;
    private ImageButton btnClear;
    private LinearLayout layoutSuggestions;
    private TextView suggestion1, suggestion2, suggestion3;
    private TextView tvResultsCount;
    private RecyclerView rvResults;
    private ProductAdapter adapter;
    private LinearLayout layoutTrending;

    private List<Product> allProducts = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch         = view.findViewById(R.id.et_search);
        btnClear         = view.findViewById(R.id.btn_clear);
        layoutSuggestions = view.findViewById(R.id.layout_suggestions);
        suggestion1      = view.findViewById(R.id.suggestion_1);
        suggestion2      = view.findViewById(R.id.suggestion_2);
        suggestion3      = view.findViewById(R.id.suggestion_3);
        tvResultsCount   = view.findViewById(R.id.tv_results_count);
        rvResults        = view.findViewById(R.id.rv_search_results);
        layoutTrending = view.findViewById(R.id.layout_trending);

        rvResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvResults.setNestedScrollingEnabled(false);

        loadProducts();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String query = s.toString().trim();

                if (query.isEmpty()) {
                    btnClear.setVisibility(View.GONE);
                    layoutSuggestions.setVisibility(View.GONE);
                    filterProducts("");
                } else {
                    btnClear.setVisibility(View.VISIBLE);
                    showSuggestions(query);
                    filterProducts(query);
                }
            }
        });

        btnClear.setOnClickListener(v -> etSearch.setText(""));

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            layoutSuggestions.setVisibility(View.GONE);
            return false;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    private void loadProducts() {
        String token = new TokenManager(requireContext()).getToken();

        RetrofitClient.getApi()
                .getProducts("Bearer " + token)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call,
                                           @NonNull Response<List<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            allProducts = response.body();
                            updateRecyclerView(allProducts);
                            tvResultsCount.setText(allProducts.size() + " RESULTS");
                            setupTrendingChips();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {}
                });
    }

    private void filterProducts(String query) {
        if (query.isEmpty()) {
            updateRecyclerView(allProducts);
            tvResultsCount.setText(allProducts.size() + " RESULTS");
            return;
        }

        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.getName().toLowerCase().contains(query.toLowerCase()) ||
                    (p.getType() != null && p.getType().toLowerCase().contains(query.toLowerCase()))) {
                filtered.add(p);
            }
        }

        updateRecyclerView(filtered);
        tvResultsCount.setText(filtered.size() + " RESULTS");
    }

    private void updateRecyclerView(List<Product> list) {
        adapter = new ProductAdapter(new ArrayList<>(list));
        adapter.setOnProductClickListener(product -> {
            String productJson = new Gson().toJson(product);
            Intent intent = new Intent(getContext(), ProductDetail.class);
            intent.putExtra(ProductDetail.EXTRA_PRODUCT, productJson);
            startActivity(intent);
        });
        rvResults.setAdapter(adapter);
    }

    private void showSuggestions(String query) {
        List<String> suggestions = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.getName().toLowerCase().startsWith(query.toLowerCase())) {
                String[] words = p.getName().trim().split("\\s+");
                String s = words.length >= 2 ? words[0] + " " + words[1] : words[0];
                if (!suggestions.contains(s)) suggestions.add(s);
                if (suggestions.size() == 3) break;
            }
        }

        if (suggestions.isEmpty()) {
            layoutSuggestions.setVisibility(View.GONE);
            return;
        }

        layoutSuggestions.setVisibility(View.VISIBLE);
        TextView[] suggestionViews = {suggestion1, suggestion2, suggestion3};
        for (int i = 0; i < suggestionViews.length; i++) {
            if (i < suggestions.size()) {
                suggestionViews[i].setVisibility(View.VISIBLE);
                suggestionViews[i].setText(suggestions.get(i));
                final String s = suggestions.get(i);
                suggestionViews[i].setOnClickListener(v -> {
                    etSearch.setText(s);
                    etSearch.setSelection(s.length());
                    layoutSuggestions.setVisibility(View.GONE);
                });
            } else {
                suggestionViews[i].setVisibility(View.GONE);
            }
        }
    }

    private void setupTrendingChips() {
        LinearLayout container = requireView().findViewById(R.id.flexbox_trending);
        container.removeAllViews();

        List<Product> top3 = new ArrayList<>(allProducts);
        top3.sort((a, b) -> b.getStock() - a.getStock());
        if (top3.size() > 3) top3 = top3.subList(0, 3);

        for (Product p : top3) {
            String[] words = p.getName().trim().split("\\s+");
            String chipText = words.length >= 2 ? words[0] + " " + words[1] : words[0];

            TextView chip = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 16, 0);
            chip.setLayoutParams(params);
            chip.setText(chipText.toUpperCase());
            chip.setTextSize(11f);
            chip.setTextColor(0xFF444444);
            chip.setPadding(32, 16, 32, 16);

            chip.setOnClickListener(v -> {
                etSearch.setText(chipText);
                etSearch.setSelection(chipText.length());
            });

            container.addView(chip);
        }
    }
}