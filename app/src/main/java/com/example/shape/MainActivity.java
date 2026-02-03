package com.example.shape;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FrameLayout mainContainer;
    TextView timerText, scoreText, targetShapeText;
    Button circleButton, triangleButton, squareButton, ovalButton, rectangleButton;
    CountDownTimer countDownTimer;
    int score = 0;
    int timeLeft = 30;
    String[] shapes = {"Circle", "Triangle", "Square", "Oval", "Rectangle"};
    String currentTargetShape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContainer = findViewById(R.id.mainContainer);
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        mainContainer.removeAllViews();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(lp);
        layout.setBackgroundColor(Color.parseColor("#FFF8E1")); // Light yellow

        TextView title = new TextView(this);
        title.setText("Shape Game");
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
        title.setTextColor(Color.parseColor("#5D1049")); // Purple
        title.setGravity(Gravity.CENTER);

        Button playBtn = new Button(this);
        playBtn.setText("Start Game");
        playBtn.setTextSize(20);
        playBtn.setTextColor(Color.WHITE);
        playBtn.setBackgroundColor(Color.parseColor("#6200EA")); // Deep purple
        playBtn.setPadding(dpToPx(20), dpToPx(10), dpToPx(20), dpToPx(10));
        playBtn.setAllCaps(false);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnParams.topMargin = dpToPx(40);
        playBtn.setLayoutParams(btnParams);

        layout.addView(title);
        layout.addView(playBtn);
        mainContainer.addView(layout);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGameScreen();
            }
        });
    }

    private void showGameScreen() {
        mainContainer.removeAllViews();

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));
        rootLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        rootLayout.setBackgroundColor(Color.parseColor("#FFF3E0")); // Light orange

        targetShapeText = new TextView(this);
        targetShapeText.setText("Select: ");
        targetShapeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        targetShapeText.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams targetLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        targetLp.gravity = Gravity.CENTER_HORIZONTAL;
        targetLp.bottomMargin = dpToPx(40);
        targetShapeText.setLayoutParams(targetLp);

        LinearLayout shapesContainer = new LinearLayout(this);
        shapesContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams shapesLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        shapesLp.bottomMargin = dpToPx(60);
        shapesContainer.setLayoutParams(shapesLp);
        shapesContainer.setWeightSum(5);
        shapesContainer.setGravity(Gravity.CENTER);

        circleButton = createShapeButton("○");
        triangleButton = createShapeButton("▲");
        squareButton = createShapeButton("■");
        ovalButton = createShapeButton("⬭");
        rectangleButton = createShapeButton("▭");

        shapesContainer.addView(circleButton);
        shapesContainer.addView(triangleButton);
        shapesContainer.addView(squareButton);
        shapesContainer.addView(ovalButton);
        shapesContainer.addView(rectangleButton);

        LinearLayout bottomLayout = new LinearLayout(this);
        bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
        bottomLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        bottomLayout.setWeightSum(2);
        bottomLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        scoreText = new TextView(this);
        scoreText.setText("Score: 0");
        scoreText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        scoreText.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams scoreLp = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        scoreText.setLayoutParams(scoreLp);
        scoreText.setGravity(Gravity.START);

        timerText = new TextView(this);
        timerText.setText("Time: 30");
        timerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        timerText.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams timerLp = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        timerText.setLayoutParams(timerLp);
        timerText.setGravity(Gravity.END);

        bottomLayout.addView(scoreText);
        bottomLayout.addView(timerText);

        rootLayout.addView(targetShapeText);
        rootLayout.addView(shapesContainer);
        rootLayout.addView(bottomLayout);
        mainContainer.addView(rootLayout);

        circleButton.setOnClickListener(shapeClickListener);
        triangleButton.setOnClickListener(shapeClickListener);
        squareButton.setOnClickListener(shapeClickListener);
        ovalButton.setOnClickListener(shapeClickListener);
        rectangleButton.setOnClickListener(shapeClickListener);

        startGame();
    }

    private Button createShapeButton(String symbol) {
        Button btn = new Button(this);
        btn.setText(symbol);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dpToPx(130), 1f);
        lp.setMarginEnd(dpToPx(6));
        btn.setLayoutParams(lp);
        btn.setTextColor(Color.WHITE);
        btn.setAllCaps(false);

        switch (symbol) {
            case "○":
                btn.setBackgroundColor(Color.parseColor("#FF5722")); break; // Orange
            case "▲":
                btn.setBackgroundColor(Color.parseColor("#009688")); break; // Teal
            case "■":
                btn.setBackgroundColor(Color.parseColor("#3F51B5")); break; // Indigo
            case "⬭":
                btn.setBackgroundColor(Color.parseColor("#FFC107"));
                btn.setTextColor(Color.BLACK); break; // Amber
            case "▭":
                btn.setBackgroundColor(Color.parseColor("#8BC34A"));
                btn.setTextColor(Color.BLACK); break; // Green
        }

        return btn;
    }

    private void startGame() {
        score = 0;
        updateScore();
        pickRandomShape();

        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeft = (int) (millisUntilFinished / 1000);
                timerText.setText("Time: " + timeLeft);
            }
            public void onFinish() {
                timerText.setText("Time: 0");
                Toast.makeText(MainActivity.this, "Time's up! Your score: " + score, Toast.LENGTH_LONG).show();
                showWelcomeScreen();
            }
        };
        countDownTimer.start();
    }

    private void pickRandomShape() {
        Random random = new Random();
        int index = random.nextInt(shapes.length);
        currentTargetShape = shapes[index];
        targetShapeText.setText("Select: " + currentTargetShape);
    }

    private void updateScore() {
        scoreText.setText("Score: " + score);
    }

    private final View.OnClickListener shapeClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String clickedShape = "";

            if (v == circleButton) clickedShape = "Circle";
            else if (v == triangleButton) clickedShape = "Triangle";
            else if (v == squareButton) clickedShape = "Square";
            else if (v == ovalButton) clickedShape = "Oval";
            else if (v == rectangleButton) clickedShape = "Rectangle";

            if (clickedShape.equals(currentTargetShape)) {
                score += 10;
                updateScore();
                animateCorrectShape(v);
                pickRandomShape();
            } else {
                score -= 5;
                if (score < 0) score = 0;
                updateScore();
                Toast.makeText(MainActivity.this, "Wrong Shape!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void animateCorrectShape(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f, 1f);
        scaleX.setDuration(300);
        scaleY.setDuration(300);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleX.start();
        scaleY.start();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}