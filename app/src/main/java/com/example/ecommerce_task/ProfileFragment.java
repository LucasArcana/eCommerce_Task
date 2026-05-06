package com.example.ecommerce_task;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce_task.API.RetrofitClient;
import com.example.ecommerce_task.API.TokenManager;
import com.example.ecommerce_task.LoginRegister.User;
import com.example.ecommerce_task.Order.OrderAdapter;
import com.example.ecommerce_task.Order.Ordermanager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    private TextView tvFullName, tvUsername, tvEmail, tvPhone, tvAddress;
    private Button  btnLogout, btnOrderHistory;
    private TokenManager tokenManager;
    private Ordermanager orderManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokenManager = new TokenManager(requireContext());
        orderManager = new Ordermanager(requireContext());

        initViews(view);
        loadProfile();
        setupListeners();
    }

    private void initViews(View view) {
        tvFullName   = view.findViewById(R.id.tv_fullname);
        tvUsername   = view.findViewById(R.id.tv_username);
        tvEmail      = view.findViewById(R.id.tv_email);
        tvPhone      = view.findViewById(R.id.tv_phone);
        tvAddress    = view.findViewById(R.id.tv_address);
        btnOrderHistory = view.findViewById(R.id.btn_order_history);
        btnLogout      = view.findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                new TokenManager(requireContext()).clearToken();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PurchaseActivity.class);
            startActivity(intent);
        });
    }

    private void loadProfile() {
        String token = tokenManager.getToken();
        android.util.Log.d("PROFILE", "token=" + token);
        if (token == null) return;

        Call<User> call = RetrofitClient.getApi().getProfile(token);
        android.util.Log.d("PROFILE", "Calling URL: " + call.request().url());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                android.util.Log.d("PROFILE", "response code=" + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    android.util.Log.d("PROFILE", "fullName=" + user.getFullName());
                    tvFullName.setText(user.getFullName() != null ? user.getFullName() : "-");
                    tvUsername.setText("@" + (user.getUsername() != null ? user.getUsername() : "-"));
                    tvEmail.setText(user.getEmail() != null ? user.getEmail() : "-");
                    tvPhone.setText(user.getPhone() != null ? user.getPhone() : "-");
                    tvAddress.setText(user.getAddress() != null ? user.getAddress() : "-");
                } else {
                    try {
                        android.util.Log.e("PROFILE", "Error=" + response.errorBody().string());
                    } catch (Exception e) {
                        android.util.Log.e("PROFILE", "Error code=" + response.code());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                android.util.Log.e("PROFILE", "onFailure: " + t.getMessage());
                android.util.Log.e("PROFILE", "onFailure cause: " + t.getCause());
            }
        });
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> {
            tokenManager.clearToken();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}