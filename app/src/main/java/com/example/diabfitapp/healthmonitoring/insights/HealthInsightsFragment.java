package com.example.diabfitapp.healthmonitoring.insights;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.diabfitapp.R;
import com.example.diabfitapp.healthmonitoring.sugerlog.SugarLog;
import com.example.diabfitapp.healthmonitoring.sugerlog.SugarLogDatabaseHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HealthInsightsFragment extends Fragment {

    private SugarLogDatabaseHelper sugarLogDatabaseHelper;
    private SimpleDateFormat inputDateFormat;
    private SimpleDateFormat outputDateFormat;
    private LineChart lineChart;
    private List<SugarLog> sugarLogs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_health_insights, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Health Insights");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        lineChart = view.findViewById(R.id.lineChart);
        Button btnGeneratePdf = view.findViewById(R.id.btn_generate_pdf);

        sugarLogDatabaseHelper = new SugarLogDatabaseHelper(requireContext());
        inputDateFormat = new SimpleDateFormat("dd MMM yyyy h:mm:ss a", Locale.getDefault());
        outputDateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());

        sugarLogs = sugarLogDatabaseHelper.getAllSugarLogs();
        List<Entry> fastingEntries = new ArrayList<>();
        List<Entry> postprandialEntries = new ArrayList<>();

        for (int i = 0; i < sugarLogs.size(); i++) {
            SugarLog log = sugarLogs.get(i);
            float x = i;
            float y = log.getSugarLevel();
            if (log.getType().equals("Fasting Blood Sugar (FBS)")) {
                fastingEntries.add(new Entry(x, y));
            } else if (log.getType().equals("Postprandial Blood Sugar (PPBS)")) {
                postprandialEntries.add(new Entry(x, y));
            }
        }

        // Create datasets for fasting and postprandial readings
        LineDataSet fastingDataSet = new LineDataSet(fastingEntries, "Fasting Blood Sugar");
        fastingDataSet.setColor(getResources().getColor(R.color.colorFastingDefault));
        fastingDataSet.setValueTextColor(getResources().getColor(R.color.colorFastingDefault));
        fastingDataSet.setDrawCircles(true);

        LineDataSet postprandialDataSet = new LineDataSet(postprandialEntries, "Postprandial Blood Sugar");
        postprandialDataSet.setColor(getResources().getColor(R.color.colorPostprandialDefault));
        postprandialDataSet.setValueTextColor(getResources().getColor(R.color.colorPostprandialDefault));
        postprandialDataSet.setDrawCircles(true);

        // Update circles for in-range values
        updateCircles(fastingDataSet, 70, 100, getResources().getColor(R.color.colorFastingInRange), getResources().getColor(R.color.colorFastingDefault));
        updateCircles(postprandialDataSet, 70, 140, getResources().getColor(R.color.colorPostprandialInRange), getResources().getColor(R.color.colorPostprandialDefault));

        // Create LineData with datasets
        LineData lineData = new LineData(fastingDataSet, postprandialDataSet);
        lineChart.setData(lineData);

        // Customize X-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(sugarLogs.size());
        xAxis.setLabelRotationAngle(45);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = Math.round(value);
                if (index >= 0 && index < sugarLogs.size()) {
                    return formatTime(sugarLogs.get(index).getTime());
                }
                return "";
            }
        });

        // Customize Y-axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(10f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Refresh the chart
        lineChart.invalidate();

        // Set button click listener
        btnGeneratePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });

        // Calculate and display percentages
        calculateAndDisplayPercentages();
    }

    private String formatTime(String time) {
        try {
            return outputDateFormat.format(inputDateFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void updateCircles(LineDataSet dataSet, float minRange, float maxRange, int inRangeColor, int outOfRangeColor) {
        List<Integer> circleColors = new ArrayList<>();

        for (int i = 0; i < dataSet.getEntryCount(); i++) {
            Entry entry = dataSet.getEntryForIndex(i);
            if (entry.getY() >= minRange && entry.getY() <= maxRange) {
                circleColors.add(inRangeColor); // Color for in-range values
            } else {
                circleColors.add(outOfRangeColor); // Color for out-of-range values
            }
        }

        dataSet.setCircleColors(circleColors);
    }

    private void createPdf() {
        // Check for write permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            try {
                // Create a file for the PDF
                File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Sugar_log.pdf");

                // Create a PDF document
                PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                // Add the main title
                document.add(new Paragraph("Health Insights Report")
                        .setFontSize(18)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBold()
                        .setMarginBottom(20));

                // Add the graph title
                document.add(new Paragraph("Sugar Level Graph")
                        .setFontSize(16)
                        .setBold()
                        .setMarginBottom(10));

                // Add the graph to the PDF
                lineChart.setBackgroundColor(Color.WHITE);
                Bitmap bitmap = Bitmap.createBitmap(lineChart.getWidth(), lineChart.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                lineChart.draw(canvas);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = new Image(com.itextpdf.io.image.ImageDataFactory.create(stream.toByteArray()));
                document.add(image.setAutoScale(true).setMarginBottom(20));

                // Add the logs title
                document.add(new Paragraph("Sugar Log Entries")
                        .setFontSize(16)
                        .setBold()
                        .setMarginBottom(10));

                // Add sugar log entries to the PDF
                for (SugarLog log : sugarLogs) {
                    document.add(new Paragraph("Time: " + log.getTime() + " - Value: " + log.getSugarLevel()));
                }

                // Close the document
                document.close();

                // Inform the user
                Toast.makeText(getContext(), "PDF generated successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error generating PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void calculateAndDisplayPercentages() {
        int inRangeCount = 0;
        int outOfRangeCount = 0;

        for (SugarLog log : sugarLogs) {
            float value = log.getSugarLevel();
            if (value >= 70 && value <= 140) {
                inRangeCount++;
            } else {
                outOfRangeCount++;
            }
        }

        int totalCount = inRangeCount + outOfRangeCount;

        TextView tvInRangePercentage = getView().findViewById(R.id.tv_in_range_percentage);
        TextView tvOutOfRangePercentage = getView().findViewById(R.id.tv_out_of_range_percentage);

        if (totalCount > 0) {
            double inRangePercentage = (inRangeCount / (double) totalCount) * 100;
            double outOfRangePercentage = (outOfRangeCount / (double) totalCount) * 100;

            tvInRangePercentage.setText(String.format(Locale.getDefault(), "In Range: %.2f%%", inRangePercentage));
            tvOutOfRangePercentage.setText(String.format(Locale.getDefault(), "Out of Range: %.2f%%", outOfRangePercentage));
        } else {
            tvInRangePercentage.setText("Suger in Normal Range: 0.00%");
            tvOutOfRangePercentage.setText("Suger out of Normal Range: 0.00%");
        }
    }
}
