package com.example.week2_developer_app;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.week2_developer_app.databinding.FragmentProjectBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProject extends Fragment implements Listener {

    private ArrayList<Project> projectlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchView searchView;
    private AdapterProject adapter;
    private String name;
    private String email;

    private FragmentProjectBinding binding;


    private void getProjectlist(int viewtype){

        projectlist.clear();
        ProjectApi service = RetrofitClient.getClient().create(ProjectApi.class);
        Call<List<Project>> call = service.getProject();
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                Log.d("response", response.body().toString());
                projectlist.clear();
                projectlist.addAll(response.body());

                adapter.notifyDataSetChanged();
                adapter = new AdapterProject(projectlist);
                adapter.setViewtype(viewtype);
                binding.projectlistview.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getmyProjectlist(int viewtype){

        projectlist.clear();
        ProjectApi service = RetrofitClient.getClient().create(ProjectApi.class);
        Project_myData data = new Project_myData(email);
        Call<List<Project>> call = service.getmyProject(data);
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                Log.d("response", response.body().toString());
                projectlist.addAll(response.body());

                adapter = new AdapterProject(projectlist);
                adapter.setViewtype(viewtype);
                binding.projectlistview.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = binding.getRoot();

        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                adapter.setViewtype(0);
                binding.projectlistview.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.projectlistview.setAdapter(adapter);
                return false;
            }
        });

        binding.refreshProject.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(requireContext(), "Refeshed", Toast.LENGTH_SHORT).show();
                getProjectlist(0);
                binding.refreshProject.setRefreshing(false);
                Log.d("refresh", projectlist.toString());
            }
        });

        binding.projectlistview.setHasFixedSize(true);
        //binding.projectlistview.addItemDecoration(new DividerItemDecoration(rootView.getContext(), 1));
        adapter = new AdapterProject(projectlist);
        binding.projectlistview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.projectlistview.setItemAnimator(new DefaultItemAnimator());
        binding.projectlistview.setAdapter(adapter);

        adapter.setOnItemClicklistener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterProject.ViewHolder holder, View v, int pos) {

                Project obj = projectlist.get(pos);

                Intent intent = new Intent(getContext(), ProjectdetailActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);

                intent.putExtra("proj_id", obj.getproj_id());
                intent.putExtra("writer", obj.getwriter());
                intent.putExtra("writer_email", obj.getwriter_email());
                intent.putExtra("title", obj.gettitle());
                intent.putExtra("content", obj.getcontent());
                intent.putExtra("field", obj.getfield());
                intent.putExtra("level", obj.getlevel());
                intent.putExtra("headcount", obj.getheadcount());
                intent.putExtra("language", obj.getlanguage());
                intent.putExtra("time", obj.gettime());
                intent.putExtra("regdata", obj.getregdata());
                startActivity(intent);
            }
        });

        binding.addbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProjectaddActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        binding.deletebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getmyProjectlist(1);
                binding.deletebtn.setVisibility(View.INVISIBLE);
                binding.backbtn.setVisibility(View.VISIBLE);
                binding.topAppBar.setTitle("My Projects");
            }
        });

        binding.backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getProjectlist(0);
                binding.deletebtn.setVisibility(View.VISIBLE);
                binding.backbtn.setVisibility(View.INVISIBLE);
                adapter.setOnItemClicklistener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterProject.ViewHolder holder, View v, int pos) {

                        Project obj = projectlist.get(pos);

                        Intent intent = new Intent(getContext(), ProjectdetailActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);

                        intent.putExtra("proj_id", obj.getproj_id());
                        intent.putExtra("writer", obj.getwriter());
                        intent.putExtra("writer_email", obj.getwriter_email());
                        intent.putExtra("title", obj.gettitle());
                        intent.putExtra("content", obj.getcontent());
                        intent.putExtra("field", obj.getfield());
                        intent.putExtra("level", obj.getlevel());
                        intent.putExtra("headcount", obj.getheadcount());
                        intent.putExtra("language", obj.getlanguage());
                        intent.putExtra("time", obj.gettime());
                        intent.putExtra("regdata", obj.getregdata());
                        startActivity(intent);
                    }
                });
                binding.topAppBar.setTitle("Project List");
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.deletebtn.setVisibility(View.VISIBLE);
        binding.backbtn.setVisibility(View.INVISIBLE);
        binding.topAppBar.setTitle("Project List");
        getProjectlist(0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getProjectlist(0);
        name = this.getArguments().getString("name");
        email = this.getArguments().getString("email");
        binding = FragmentProjectBinding.inflate(getLayoutInflater());
        binding.topAppBar.setTitle("Project List");
    }

    @Override
    public void returnyes(String str) {}
}

interface Listener{
    void returnyes(String str);
}