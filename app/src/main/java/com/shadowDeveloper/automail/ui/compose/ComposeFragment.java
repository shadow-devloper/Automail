package com.shadowDeveloper.automail.ui.compose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.shadowDeveloper.automail.databinding.FragmentComposeBinding;

public class ComposeFragment extends Fragment {

    //private ComposeViewModel composeViewModel;
    private FragmentComposeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //composeViewModel =
                new ViewModelProvider(this).get(ComposeViewModel.class);

        binding = FragmentComposeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}