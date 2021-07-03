package com.shadowDeveloper.automail.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shadowDeveloper.automail.Adapters.HistoryAdapter;
import com.shadowDeveloper.automail.R;
import com.shadowDeveloper.automail.databinding.FragmentHistoryBinding;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {


    private FragmentHistoryBinding binding;
    RecyclerView recyclerView;
    ArrayList<HistoryViewModel> historyViewModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView=root.findViewById(R.id.recHistoryView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyViewModels=new ArrayList<>();

        HistoryViewModel ob1 = new HistoryViewModel(R.drawable.circlular_border,"Title","description");
        historyViewModels.add(ob1);


        recyclerView.setAdapter(new HistoryAdapter(historyViewModels));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}