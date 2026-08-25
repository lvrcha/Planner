package com.example.planner;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private GridLayout calendarGrid;
    private TextView monthTitle;
    private TextView selectedDayTitle;
    private TextView selectedDayInfo;

    private YearMonth currentMonth;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarGrid = findViewById(R.id.calendarGrid);
        monthTitle = findViewById(R.id.monthTitle);
        selectedDayTitle = findViewById(R.id.selectedDayTitle);
        selectedDayInfo = findViewById(R.id.selectedDayInfo);

        ImageButton backButton =
                findViewById(R.id.backButton);

        Button previousMonthButton =
                findViewById(R.id.previousMonthButton);

        Button nextMonthButton =
                findViewById(R.id.nextMonthButton);

        currentMonth = YearMonth.now();
        selectedDate = LocalDate.now();

        showCalendar();

        backButton.setOnClickListener(v -> {
            finish();
        });

        previousMonthButton.setOnClickListener(v -> {
            currentMonth = currentMonth.minusMonths(1);
            selectedDate = null;
            showCalendar();
        });

        nextMonthButton.setOnClickListener(v -> {
            currentMonth = currentMonth.plusMonths(1);
            selectedDate = null;
            showCalendar();
        });
    }

    private void showCalendar() {

        calendarGrid.removeAllViews();

        String monthName = currentMonth
                .getMonth()
                .getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        new Locale("ru")
                );

        monthTitle.setText(
                capitalize(monthName)
                        + " "
                        + currentMonth.getYear()
        );

        LocalDate firstDay =
                currentMonth.atDay(1);

        int firstDayOfWeek =
                firstDay.getDayOfWeek().getValue();

        int daysInMonth =
                currentMonth.lengthOfMonth();

        int row = 0;
        int column = 0;

        for (int i = 1; i < firstDayOfWeek; i++) {

            addEmptyCell(row, column);

            column++;
        }

        for (int day = 1; day <= daysInMonth; day++) {

            LocalDate date =
                    currentMonth.atDay(day);

            addDayCell(
                    date,
                    row,
                    column
            );

            column++;

            if (column == 7) {

                column = 0;
                row++;
            }
        }
    }

    private void addEmptyCell(int row, int column) {

        TextView emptyCell = new TextView(this);

        GridLayout.LayoutParams params =
                new GridLayout.LayoutParams();

        params.width = 0;
        params.height = 80;

        params.columnSpec =
                GridLayout.spec(column, 1f);

        params.rowSpec =
                GridLayout.spec(row, 1);

        params.setMargins(2, 2, 2, 2);

        emptyCell.setLayoutParams(params);

        calendarGrid.addView(emptyCell);
    }

    private void addDayCell(LocalDate date, int row, int column) {

        TextView dayCell = new TextView(this);

        GridLayout.LayoutParams params =
                new GridLayout.LayoutParams();

        params.width = 0;
        params.height = 80;

        params.columnSpec =
                GridLayout.spec(column, 1f);

        params.rowSpec =
                GridLayout.spec(row, 1);

        params.setMargins(2, 2, 2, 2);

        dayCell.setLayoutParams(params);

        dayCell.setText(
                String.valueOf(date.getDayOfMonth())
        );

        dayCell.setTextSize(16);

        dayCell.setGravity(
                Gravity.TOP | Gravity.START
        );

        dayCell.setPadding(8, 8, 8, 8);

        if (date.equals(selectedDate)) {
            dayCell.setBackgroundColor(Color.LTGRAY);
        }

        dayCell.setOnClickListener(v -> {

            selectedDate = date;

            showSelectedDay();

            showCalendar();
        });

        calendarGrid.addView(dayCell);
    }

    private void showSelectedDay() {

        if (selectedDate == null) {
            selectedDayTitle.setText(
                    "Выберите день"
            );

            selectedDayInfo.setText(
                    "Здесь будут отображаться задачи"
            );

            return;
        }

        String dayName = selectedDate
                .getDayOfWeek()
                .getDisplayName(
                        TextStyle.FULL,
                        new Locale("ru")
                );

        String monthName = selectedDate
                .getMonth()
                .getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        new Locale("ru")
                );

        selectedDayTitle.setText(
                selectedDate.getDayOfMonth()
                        + " "
                        + monthName
                        + ", "
                        + dayName
        );

        selectedDayInfo.setText(
                "Здесь будут отображаться задачи"
        );
    }

    private String capitalize(String text) {

        if (text == null || text.isEmpty()) {
            return text;
        }

        return text.substring(0, 1).toUpperCase(
                new Locale("ru")
        ) + text.substring(1);
    }
}