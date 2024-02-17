package com.example.siestasiestabitirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    private DatabaseReference Ref;
    private RatingBar ratingBar;
    private EditText feedback;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        feedback = (EditText) findViewById(R.id.feedback);

        Ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://siestasiesta-2d852-default-rtdb.firebaseio.com/");
    }

    public void feedbacksent(View view) {
        float ratingInput = ratingBar.getRating();
        String feedbackInput = feedback.getText().toString().trim();

        if (ratingInput != 0 && !feedbackInput.isEmpty()) {
            DatabaseReference newFeedbackRef = Ref.child("Feedbacks").push();

            Map<String, Object> feedbackData = new HashMap<>();
            feedbackData.put("rating", ratingInput);
            feedbackData.put("feedback", feedbackInput);

            newFeedbackRef.setValue(feedbackData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(FeedbackActivity.this, "Feedback sent successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FeedbackActivity.this, "Failed to send feedback: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Rating and feedback must not be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
