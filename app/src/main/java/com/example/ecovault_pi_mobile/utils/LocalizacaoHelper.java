package com.example.ecovault_pi_mobile.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.Locale;

public class LocalizacaoHelper {

    public interface OnLocalizacaoResult {
        void aoEncontrarLocalizacao(Location location);
        void aoFalharLocalizacao();
    }

    private static final long IDADE_MAXIMA_LOCALIZACAO_MS = 2 * 60 * 1000L;
    private static final long TIMEOUT_REQUISICAO_MS = 8000L;

    private final Context contexto;
    private final FusedLocationProviderClient client;

    public LocalizacaoHelper(Context contexto) {
        this.contexto = contexto;
        this.client = LocationServices.getFusedLocationProviderClient(contexto);
    }

    public boolean temPermissaoLocalizacao() {
        return ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
               ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    public void obterUltimaLocalizacao(OnLocalizacaoResult callback) {
        if (!temPermissaoLocalizacao()) {
            callback.aoFalharLocalizacao();
            return;
        }

        client.getLastLocation().addOnSuccessListener(location -> {
            if (isLocalizacaoRecente(location)) {
                callback.aoEncontrarLocalizacao(location);
            } else {
                requisitarAtualizacaoUnica(callback);
            }
        }).addOnFailureListener(e -> requisitarAtualizacaoUnica(callback));
    }

    private boolean isLocalizacaoRecente(Location location) {
        if (location == null) {
            return false;
        }
        long idadeMs = System.currentTimeMillis() - location.getTime();
        return idadeMs >= 0 && idadeMs <= IDADE_MAXIMA_LOCALIZACAO_MS;
    }

    @SuppressLint("MissingPermission")
    private void requisitarAtualizacaoUnica(OnLocalizacaoResult callback) {
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5000)
                .setMaxUpdates(1)
                .build();

        Handler handler = new Handler(Looper.getMainLooper());

        LocationCallback locationCallback = new LocationCallback() {
            private boolean callbackExecutado = false;

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (callbackExecutado) {
                    return;
                }
                callbackExecutado = true;

                client.removeLocationUpdates(this);
                handler.removeCallbacksAndMessages(null);

                Location location = locationResult.getLastLocation();
                if (location != null) {
                    callback.aoEncontrarLocalizacao(location);
                } else {
                    callback.aoFalharLocalizacao();
                }
            }
        };

        client.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());

        handler.postDelayed(() -> {
            client.removeLocationUpdates(locationCallback);
            callback.aoFalharLocalizacao();
        }, TIMEOUT_REQUISICAO_MS);
    }

    public static float calcularDistancia(double lat1, double lng1, double lat2, double lng2) {
        float[] resultados = new float[1];
        Location.distanceBetween(lat1, lng1, lat2, lng2, resultados);
        return resultados[0];
    }

    public static String formatarDistancia(float metros) {
        if (metros < 1000) {
            return String.format(Locale.getDefault(), "%d m", (int) metros);
        } else {
            float km = metros / 1000f;
            return String.format(new Locale("pt", "BR"), "%.1f km", km);
        }
    }
}
