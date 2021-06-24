package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import uk.co.appoly.arcorelocation.LocationScene;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.ARPoint;
import com.example.myapplication.models.AuthTokenRequest;
import com.example.myapplication.models.GPSTracker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ViewRenderable;

import uk.co.appoly.arcorelocation.LocationMarker;
import uk.co.appoly.arcorelocation.rendering.LocationNode;
import uk.co.appoly.arcorelocation.rendering.LocationNodeRender;
import uk.co.appoly.arcorelocation.utils.ARLocationPermissionHelper;

public class MainActivity extends BaseMapRendererActivity {

    private LinearLayout bottomSheet;
    private BottomSheetBehavior sheetBehavior;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arSceneView = findViewById(R.id.ar_scene_view);

        bottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);


        AuthTokenRequest request = new AuthTokenRequest();

        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();


        AuthTokenRequest.Device device = new AuthTokenRequest.Device();
        device.setMake(android.os.Build.DEVICE);
        device.setModel(android.os.Build.MODEL);

        AuthTokenRequest.Device.Os os = new AuthTokenRequest.Device.Os();
        os.setFamily("Android");
        os.setVersion(android.os.Build.VERSION.SDK);

        device.setOs(os);

        request.setDevice(device);


        arSceneView
                .getScene()
                .setOnUpdateListener(
                        frameTime -> {
                            if (locationScene == null) {

                                locationScene = new LocationScene(this, this, arSceneView);
                                locationScene.setBearingAdjustment(locationScene.getBearingAdjustment() - 2);
                                locationScene.setRefreshAnchorsAsLocationChanges(true);


                                locationScene.setDebugEnabled(true);


                                getAtmList(request, latitude, longitude);
                            }

                            Frame frame = arSceneView.getArFrame();
                            if (frame == null) {
                                return;
                            }

                            if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
                                return;
                            }

                            if (locationScene != null) {
                                locationScene.processFrame(frame);
                            }

                            if (loadingMessageSnackbar != null) {
                                for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
                                    if (plane.getTrackingState() == TrackingState.TRACKING) {
                                        hideLoadingMessage();
                                    }
                                }
                            }
                        });


        ARLocationPermissionHelper.requestPermission(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void onAtmListLoaded(ArrayList<ARPoint> arPoints) {
        for (int i = 0; i < arPoints.size(); i++) {
            loadMarker(arPoints.get(i));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadMarker(ARPoint arPoint) {
        try {
            CompletableFuture<ViewRenderable> exampleLayout =
                    ViewRenderable.builder()
                            .setView(MainActivity.this, R.layout.example_layout)
                            .build();

            CompletableFuture.allOf(
                    exampleLayout
                            .handle(
                                    (notUsed, throwable) -> {

                                        if (throwable != null) {
                                            Utils.displayError(MainActivity.this, "Unable to load renderables", throwable);
                                            return null;
                                        }

                                        try {

                                            ViewRenderable exampleLayoutRenderable = exampleLayout.get();
                                            LocationMarker layoutLocationMarker = new LocationMarker(
                                                    arPoint.getLocation().getLongitude(), arPoint.getLocation().getLatitude(),
                                                    getExampleView(exampleLayoutRenderable)
                                            );

                                            layoutLocationMarker.setRenderEvent(new LocationNodeRender() {
                                                @Override
                                                public void render(LocationNode node) {
                                                    String distance = String.valueOf(arPoint.getdistanceatm());
                                                    View eView = exampleLayoutRenderable.getView();
                                                    TextView nameTextView = eView.findViewById(R.id.tv_name);
                                                    TextView distanceTextView = eView.findViewById(R.id.tv_distance);
                                                    TextView adressTextView = eView.findViewById(R.id.tv_adress);
                                                    TextView bottomTextView = eView.findViewById(R.id.name);
                                                    distanceTextView.setText(node.getDistance() + "\t m ");
                                                    nameTextView.setText(arPoint.getName());

                                                    eView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            BottomSheetFragment fragment = new BottomSheetFragment();
                                                            fragment.show(getSupportFragmentManager(),"");
                                                        }
                                                    });
                                                }
                                            });

                                            locationScene.mLocationMarkers.add(layoutLocationMarker);
                                        } catch (Exception ex) {
                                            Utils.displayError(MainActivity.this, "Unable to load renderables", ex);
                                        }
                                        return null;
                                    }));

        } catch (Exception e) {

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private Node getExampleView(ViewRenderable viewRenderable) {
        Node base = new Node();
        base.setRenderable(viewRenderable);
        Context c = this;
        View eView = viewRenderable.getView();

        return base;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (locationScene != null) {
            locationScene.resume();
        }

        if (arSceneView.getSession() == null) {
            try {
                Session session = Utils.createArSession(this, installRequested);
                if (session == null) {
                    installRequested = ARLocationPermissionHelper.hasPermission(this);
                    return;
                } else {
                    arSceneView.setupSession(session);
                }
            } catch (UnavailableException e) {
                Utils.handleSessionException(this, e);
            }
        }

        try {
            arSceneView.resume();
        } catch (CameraNotAvailableException ex) {
            Utils.displayError(this, "Unable to get camera", ex);
            finish();
            return;
        }

        if (arSceneView.getSession() != null) {
            showLoadingMessage();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (locationScene != null) {
            locationScene.pause();
        }

        arSceneView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        arSceneView.destroy();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        if (!ARLocationPermissionHelper.hasPermission(this)) {
            if (!ARLocationPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                ARLocationPermissionHelper.launchPermissionSettings(this);
            } else {
                Toast.makeText(
                        this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                        .show();
            }
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void showLoadingMessage() {
        if (loadingMessageSnackbar != null && loadingMessageSnackbar.isShownOrQueued()) {
            return;
        }

        loadingMessageSnackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "PLANE FINDING",
                        Snackbar.LENGTH_INDEFINITE);
        loadingMessageSnackbar.getView().setBackgroundColor(0xbf323232);
        loadingMessageSnackbar.show();
    }

    private void hideLoadingMessage() {
        if (loadingMessageSnackbar == null) {
            return;
        }

        loadingMessageSnackbar.dismiss();
        loadingMessageSnackbar = null;
    }
}