package com.example.ecommerce_task;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce_task.Cart.CartAdapter;
import com.example.ecommerce_task.Cart.CartItem;
import com.example.ecommerce_task.Cart.Cartmanager;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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

    private RecyclerView rvCart;
    private TextView tvSubtotal, tvTotal;
    private LinearLayout layoutEmpty;
    private MaterialButton btnCheckout;
    private CartAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvCart      = view.findViewById(R.id.rv_cart);
        tvSubtotal  = view.findViewById(R.id.tv_subtotal);
        tvTotal     = view.findViewById(R.id.tv_total);
        layoutEmpty = view.findViewById(R.id.layout_empty);
        btnCheckout = view.findViewById(R.id.btn_checkout);

        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshCart();

        btnCheckout.setOnClickListener(v -> {
            if (Cartmanager.getInstance(requireContext()).getItems().isEmpty()) {
                Toast.makeText(getContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(getContext(), CheckoutActivity.class));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCart();
    }

    private void refreshCart() {
        List<CartItem> items = Cartmanager.getInstance(requireContext()).getItems();

        if (items.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);
        }

        adapter = new CartAdapter(items, this::updateSummary);
        rvCart.setAdapter(adapter);
        updateSummary();
    }

    private void updateSummary() {
        double subtotal = Cartmanager.getInstance(requireContext()).getSubtotal();
        tvSubtotal.setText(String.format("$%,.2f", subtotal));
        tvTotal.setText(String.format("$%,.2f", subtotal));

        if (Cartmanager.getInstance(requireContext()).getItems().isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
}