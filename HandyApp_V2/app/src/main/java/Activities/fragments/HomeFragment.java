package Activities.fragments;

import Activities.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.handyapp_v2.R;


public class HomeFragment extends Fragment {

    private RelativeLayout plumbing, cleaning, photography, carRepair, painting, house, web, video;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        plumbing = view.findViewById(R.id.plumbing);
        cleaning = view.findViewById(R.id.cleaning);
        photography = view.findViewById(R.id.photo);
        carRepair = view.findViewById(R.id.car);
        painting = view.findViewById(R.id.paint);
        house = view.findViewById(R.id.house);
        web = view.findViewById(R.id.web);
        video = view.findViewById(R.id.video);


        clickListner();


    }

    private void clickListner() {
        plumbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                intent.putExtra("category","Plumbing");
                intent.putExtra("pic",R.drawable.category_img1);
                startActivity(intent);
            }
        });
        cleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                intent.putExtra("category", "Cleaning");
                intent.putExtra("pic",R.drawable.category_img2);
                startActivity(intent);
            }
        });
        photography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                intent.putExtra("category", "Photography");
                intent.putExtra("pic",R.drawable.category_img3);
                startActivity(intent);
            }
        });
        carRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                intent.putExtra("category", "Car Repair");
                intent.putExtra("pic",R.drawable.category_img4);
                startActivity(intent);
            }
        });
        painting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                intent.putExtra("category", "Painting");
                intent.putExtra("pic",R.drawable.category_img5);
                startActivity(intent);
            }
        });
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                intent.putExtra("category", "House Work");
                intent.putExtra("pic",R.drawable.category_img6);
                startActivity(intent);
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                intent.putExtra("category", "Web Development");
                intent.putExtra("pic",R.drawable.category_img7);
                startActivity(intent);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                intent.putExtra("category", "Photo & Video");
                intent.putExtra("pic",R.drawable.category_img8);
                startActivity(intent);
            }
        });

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
}