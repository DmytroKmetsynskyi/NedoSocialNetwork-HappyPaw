package com.fernfog.happypaw;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AnalyzeFragment extends Fragment {
    private FirebaseFirestore db;
    private LineChart lineChart;

    public AnalyzeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = new View(getContext());

        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            mAuth.getCurrentUser().reload();

            FirebaseUser currentUser = mAuth.getCurrentUser();

            view = inflater.inflate(R.layout.fragment_analyze, container, false);

            if (currentUser == null) {

            } else {
                db = FirebaseFirestore.getInstance();
                lineChart = view.findViewById(R.id.lineChart);
                fetchDataAndUpdateChart();
            }
        } catch (Exception e) {

        }

        return view;
    }

    private void fetchDataAndUpdateChart() {
        db.collection("articles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Integer> dateCountMap = new HashMap<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String dateString = document.getString("date");

                        if (dateCountMap.containsKey(dateString)) {
                            int count = dateCountMap.get(dateString);
                            dateCountMap.put(dateString, count + 1);
                        } else {
                            dateCountMap.put(dateString, 1);
                        }
                    }

                    List<Entry> entries = new ArrayList<>();
                    List<String> labels = new ArrayList<>();

                    for (Map.Entry<String, Integer> entry : dateCountMap.entrySet()) {
                        labels.add(entry.getKey());
                    }

                    Collections.sort(labels, new Comparator<String>() {
                        @Override
                        public int compare(String date1, String date2) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

                            try {
                                Date dateObj1 = sdf.parse(date1);
                                Date dateObj2 = sdf.parse(date2);

                                return dateObj1.compareTo(dateObj2);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });

                    for (String label : labels) {
                        entries.add(new Entry(labels.indexOf(label), dateCountMap.get(label)));
                    }

                    LineDataSet dataSet = new LineDataSet(entries, getResources().getString(R.string.analytics_text));
                    LineData lineData = new LineData(dataSet);

                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setValueFormatter(new DateAxisValueFormatter(labels));
                    xAxis.setGranularity(1f);

                    lineChart.setScaleEnabled(false);
                    lineChart.setData(lineData);
                    lineChart.invalidate();
                } else {
                    Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
