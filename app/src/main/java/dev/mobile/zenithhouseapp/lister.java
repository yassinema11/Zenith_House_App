package dev.mobile.zenithhouseapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link lister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class lister extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<note> noteList;
    private RecyclerView.LayoutManager layoutManager;
    private noteAdapter nAdapter;

    public lister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment lister.
     */
    // TODO: Rename and change types and number of parameters
    public static lister newInstance(String param1, String param2) {
        lister fragment = new lister();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_lister, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.contact_recyclerview);

        layoutManager = new LinearLayoutManager(getActivity());
        noteBDD db = new noteBDD(getActivity());
        noteList = db.getAllnotes();

        nAdapter = new noteAdapter(getActivity(), noteList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(nAdapter);

        return view;
    }
}