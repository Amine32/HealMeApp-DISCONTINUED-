package ru.tsu.hits.healmeapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class HomeFragment extends Fragment {

    private ImageButton btnRequestMedicine;
    private ImageButton btnDeliverMedicine;
    private ImageButton btnActiveRequest;
    private ImageButton btnActiveOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnRequestMedicine = (ImageButton) view.findViewById(R.id.Home_BtnRequestMedicine);
        btnDeliverMedicine = (ImageButton) view.findViewById(R.id.Home_DeliverMedicine);
        btnActiveRequest = (ImageButton) view.findViewById(R.id.Home_ActiveRequest);
        btnActiveOrder = (ImageButton) view.findViewById(R.id.Home_ActiveOrders);

        btnRequestMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RequestMedicineMainFragment.class);
                startActivity(intent);
            }
        });

        btnDeliverMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeliverMedicine.class);
                startActivity(intent);
            }
        });

        btnActiveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActiveRequests.class);
                startActivity(intent);
            }
        });

        btnActiveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActiveOrders.class);
                startActivity(intent);
            }
        });

    }
}
