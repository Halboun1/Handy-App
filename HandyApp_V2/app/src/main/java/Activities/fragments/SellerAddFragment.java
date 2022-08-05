package Activities.fragments;

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

import Activities.SellerAddDetailsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerAddFragment extends Fragment {
    private RelativeLayout plumbing, cleaning, photography, carRepair, painting, house, web, video;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public SellerAddFragment() {
        // Required empty public constructor
    }



    public static SellerAddFragment newInstance(String param1, String param2) {
        SellerAddFragment fragment = new SellerAddFragment();
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
                Intent intent = new Intent(getActivity(), SellerAddDetailsActivity.class);
                intent.putExtra("category", "Plumbing");
                startActivity(intent);
            }
        });
        cleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellerAddDetailsActivity.class);
                intent.putExtra("category", "Cleaning");
                startActivity(intent);
            }
        });
        photography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellerAddDetailsActivity.class);
                intent.putExtra("category", "Photography");
                startActivity(intent);
            }
        });
        carRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellerAddDetailsActivity.class);
                intent.putExtra("category", "car Repair");
                startActivity(intent);
            }
        });
        painting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellerAddDetailsActivity.class);
                intent.putExtra("category", "Painting");
                startActivity(intent);
            }
        });
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellerAddDetailsActivity.class);
                intent.putExtra("category", "House Work");
                startActivity(intent);
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellerAddDetailsActivity.class);
                intent.putExtra("category", "Web Development");
                startActivity(intent);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellerAddDetailsActivity.class);
                intent.putExtra("category", "Photo & Video");
                startActivity(intent);
            }
        });

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_add, container, false);
    }
}