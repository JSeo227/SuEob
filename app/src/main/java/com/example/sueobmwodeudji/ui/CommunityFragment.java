package com.example.sueobmwodeudji.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sueobmwodeudji.R;
import com.example.sueobmwodeudji.adapter.BasicFrameAdapter;
import com.example.sueobmwodeudji.databinding.FragmentCommunityBinding;
import com.example.sueobmwodeudji.model.BasicFrameModel;

import java.util.LinkedList;
import java.util.List;

public class CommunityFragment extends Fragment {
    private FragmentCommunityBinding binding;
    private List<BasicFrameModel> list = new LinkedList<BasicFrameModel>();

    public static CommunityFragment getInstance() {
        return new CommunityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(binding.getRoot(), savedInstanceState);
        CommunityItemView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void CommunityItemView() {
        list.add(new BasicFrameModel("게시글", R.layout.item_community_sub_1));
        list.add(new BasicFrameModel("최근 작성된 글", R.layout.item_community_sub_2));
        binding.communityFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.communityFragment.setAdapter(new BasicFrameAdapter(getContext(), list));
    }

}