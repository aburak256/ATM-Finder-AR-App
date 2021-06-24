package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.ARPoint;
import com.example.myapplication.models.ApiClient;
import com.example.myapplication.models.ApiService;
import com.example.myapplication.models.AtmResponse;
import com.example.myapplication.models.AuthTokenRequest;
import com.example.myapplication.models.AuthTokenResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.appoly.arcorelocation.LocationScene;

public abstract class BaseMapRendererActivity extends AppCompatActivity {
    protected boolean installRequested;
    protected ApiService apiService;
    protected AtmResponse.Points[] pointsmain;

    protected Snackbar loadingMessageSnackbar = null;

    protected ArSceneView arSceneView;

    protected ModelRenderable andyRenderable;


    protected LocationScene locationScene;
    private String tokenKey;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiClient.getClient().create(ApiService.class);


    }

    public void getAtmList(AuthTokenRequest request, double latitude, double longitude){
        apiService.postToken(request).enqueue(new Callback<AuthTokenResponse>() {
            @Override
            public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                Log.i("tokenresult", "new token " + response.body().getToken());
                tokenKey = response.body().getToken();

                apiService.getAtm(tokenKey, "atm", latitude, longitude).enqueue(new Callback<AtmResponse>() {
                    @Override
                    public void onResponse(Call<AtmResponse> call, Response<AtmResponse> response) {
                        Log.i("atmresult", "atm " + response.body());
                        pointsmain = response.body().getPoints();
                        ArrayList<ARPoint> arPoints = new ArrayList<>();

                        for (AtmResponse.Points points : pointsmain) {
                            arPoints.add(new ARPoint(points.getName(), points.getLatitude(), points.getLongitude(), points.getDistance(),points.getAddress()));
                        }
                        arPoints = new ArrayList<>(arPoints.subList(0, 5));
                        onAtmListLoaded(arPoints);

                    }

                    @Override
                    public void onFailure(Call<AtmResponse> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
            }
        });
    }

    abstract void onAtmListLoaded(ArrayList<ARPoint> arPoints);
}
