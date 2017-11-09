package com.example.cw8454tn.moviereviewsdatabase;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText mMovieNameET;
    EditText mMovieReviewTextET;
    RatingBar mMovieStars;
    Button mSaveButton;
    Button mShowAllReviewsButton;
    TextView mAllMoviesTV;
    private static final String ALL_REVIEWS_KEY = "all_reviews";
    private DatabaseReference reviewsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //References for components
        mMovieNameET = (EditText) findViewById(R.id.movie_name_et);
        mMovieReviewTextET = (EditText) findViewById(R.id.movie_review_et);
        mMovieStars = (RatingBar) findViewById(R.id.movie_stars_rb);

        mSaveButton = (Button) findViewById(R.id.save_review_button);
        mShowAllReviewsButton = (Button) findViewById(R.id.show_all_reviews_button);

        mAllMoviesTV = (TextView) findViewById(R.id.all_movie_data_tv);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference();
        reviewsReference = dbReference.child(ALL_REVIEWS_KEY);
        fetchAllReviews();

        //Register listeners for buttons
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReview();
            }
        });

        mShowAllReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchAllReviews();
            }
        });
    }

    private void saveReview() {
        String name = mMovieNameET.getText().toString();
        String reviewText = mMovieReviewTextET.getText().toString();
        float stars = mMovieStars.getRating();
        MovieReview review  = new MovieReview(name,reviewText,stars);
        DatabaseReference newReviewReference = reviewsReference.push();

        newReviewReference.setValue(review);
        Toast.makeText(this,"Review Saved!",Toast.LENGTH_SHORT).show();

        //todo save
    }

    private void fetchAllReviews() {
        reviewsReference.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList <MovieReview> allReviews = new ArrayList<MovieReview>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    MovieReview review = childSnapshot.getValue(MovieReview.class);
                    allReviews.add(review);
                }
                mAllMoviesTV.setText(allReviews.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("test","Firebase Error", databaseError.toException());
            }
        });
    }
    private void searchReviews(){

        reviewsReference.orderByChild("stars").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList <MovieReview> allReviews = new ArrayList<MovieReview>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    MovieReview review = childSnapshot.getValue(MovieReview.class);
                    allReviews.add(review);
                }
                mAllMoviesTV.setText(allReviews.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("test","Firebase Error", databaseError.toException());
            }
        });
    }

}
