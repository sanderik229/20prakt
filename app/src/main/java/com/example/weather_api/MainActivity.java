package com.example.weather_api;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText movieIdInput;
    private Button btn;
    private TextView movieInfo;

    private static final String BASE_URL = "https://kinopoiskapiunofficial.tech/";
    private static final String API_KEY = "88c6b664-a80f-47b7-9aa8-8fcad45dbf83";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieIdInput = findViewById(R.id.cityInput);
        btn = findViewById(R.id.searchButton);
        movieInfo = findViewById(R.id.weatherInfo);

        btn.setOnClickListener(v -> {
            String input = movieIdInput.getText().toString().trim();
            if (!input.isEmpty()) {
                try {
                    int movieId = Integer.parseInt(input);
                    getMovieInfo(movieId);
                } catch (NumberFormatException e) {
                    movieInfo.setText("Введите корректный числовой ID фильма.");
                }
            }
        });
    }

    private void getMovieInfo(int id) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("X-API-KEY", API_KEY)
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)  // <-- передаём кастомный клиент с заголовками
                .build();

        API api = retrofit.create(API.class);
        Call<MovieResponse> call = api.getMovieById(id);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieResponse movie = response.body();
                    StringBuilder genres = new StringBuilder();
                    for (MovieResponse.Genre g : movie.getGenres()) {
                        genres.append(g.getGenre()).append(", ");
                    }

                    String movieDetails = "Название: " + movie.getNameRu() +
                            "\nГод: " + movie.getYear() +
                            "\nРейтинг: " + movie.getRatingKinopoisk() +
                            "\nЖанры: " + genres +
                            "\nОписание: " + movie.getDescription();

                    movieInfo.setText(movieDetails);
                } else {
                    switch (response.code()) {
                        case 404:
                            movieInfo.setText("Фильм не найден (404)");
                            break;
                        case 500:
                            movieInfo.setText("Ошибка сервера (500)");
                            break;
                        default:
                            movieInfo.setText("Ошибка: " + response.code());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                movieInfo.setText("Ошибка сети: " + t.getMessage());
            }
        });
    }

}

