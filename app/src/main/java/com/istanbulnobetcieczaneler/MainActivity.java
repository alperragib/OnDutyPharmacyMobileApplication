package com.istanbulnobetcieczaneler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Gerekli değişkenler tanımlandı
    Spinner spinner_ilce;
    TextView textViewBulunanSayi;
    RecyclerView recyclerViewEczaneler;
    ArrayList<IlceList> ilceList;
    ArrayList<Eczane> eczaneList;
    EczanelerAdapter eczanelerAdapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Tasarım ile değişkenler asarında bağlantı sağlandı
        spinner_ilce = findViewById(R.id.spinner_ilce);
        textViewBulunanSayi = findViewById(R.id.textViewBulunanSayi);
        recyclerViewEczaneler = findViewById(R.id.recyclerViewEczaneler);
        refreshLayout = findViewById(R.id.swiperefresh);

        //Yenileme yapılabilmesi için swipe refresh ekledim.
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(ilceList.size()>0){
                    getEczaneList(ilceList.get(spinner_ilce.getSelectedItemPosition()));
                    refreshLayout.setRefreshing(false);
                }else{
                    getIlceList();
                    refreshLayout.setRefreshing(false);
                }
            }
        });

        //Burada uygulama açılınca verileri çekmesi için fonksiyonu çağırdım.
        getIlceList();

        //Burada spinner dan seçilen ilçeye göre eczaneleri getirmesini sağladım.
        spinner_ilce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getEczaneList(ilceList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });



    }


    void getIlceList() {

        //Burada api ye istek gönderdim.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.nosyapi.com/apiv2/pharmacy/city?city=istanbul";

        ilceList = new ArrayList<>(); //Listeyi temizledim.

        // Sağlanan URL’den bir dize yanıtı istenmektedir.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Gelen JSON versisine parse işlemi yaptım.
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("message").trim().equals("ok")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String ilce_adi = object.getString("ilceAd");
                                    String ilce_uzantisi = object.getString("ilceSlug");

                                    //Nesne oluşturdum.
                                    ilceList.add(new IlceList(ilce_adi, ilce_uzantisi));
                                }
                                //Spinner listeme atadım.
                                ArrayAdapter<IlceList> adapter = new ArrayAdapter<IlceList>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, ilceList);
                                spinner_ilce.setAdapter(adapter);
                            } else {
                                Toast.makeText(MainActivity.this, "Bir hata meydana geldi! Lütfen daha sonra tekrar deneyin.", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "İnternet bağlantısı bulunamadı! Lütfen internet bağlantınızı kontrol edin.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "İnternet bağlantısı bulunamadı! Lütfen internet bağlantınızı kontrol edin.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Burada ise api ye istek gönderirken header aracılığıyla giriş bilgimi gönderdim.
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "XxniniFBPXPReGo7NJ3zl0Vz7zliW75V5NaPE9pAwF01C98Cr3iYJakuhaU7");
                return params;
            }
        };

        //isteği RequestQueue ekledim.
        queue.add(stringRequest);
    }

    void getEczaneList(IlceList ilceList) {

        //Burada api ye istek gönderdim.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.nosyapi.com/apiv2/pharmacy?city=istanbul&county=" + ilceList.ilce_uzantisi;

        eczaneList = new ArrayList<>();//Eczane listesini temizledim.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //Gelen JSON versisine parse işlemi yaptım.
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("message").trim().equals("ok")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String adi = object.getString("EczaneAdi");
                                    String adresi = object.getString("Adresi");
                                    String adres_tarifi = object.getString("YolTarifi");
                                    String telefon = object.getString("Telefon");
                                    double latitude = object.getDouble("latitude");
                                    double longitude = object.getDouble("longitude");

                                    //Nesne oluşturdum.
                                    Eczane eczane = new Eczane(adi, adresi, adres_tarifi, telefon, latitude, longitude);
                                    eczaneList.add(eczane);

                                }
                                //Adapter a atama yaptım.
                                eczanelerAdapter = new EczanelerAdapter(MainActivity.this, eczaneList);
                                recyclerViewEczaneler.setAdapter(eczanelerAdapter);

                                recyclerViewEczaneler.setHasFixedSize(true);
                                recyclerViewEczaneler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

                                if(eczaneList.size()>0){
                                    textViewBulunanSayi.setText(ilceList.ilce_adi+" ilçesinde " +eczaneList.size()+" nöbetçi eczane bulundu.");
                                }else{
                                    textViewBulunanSayi.setText(ilceList.ilce_adi+" ilçesinde nöbetçi eczane bulunamadı.");
                                }

                                //Recyclerview ile ekranda gösterdim.


                            } else {
                                Toast.makeText(MainActivity.this, "Bir hata meydana geldi! Lütfen daha sonra tekrar deneyin.", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "İnternet bağlantısı bulunamadı! Lütfen internet bağlantınızı kontrol edin.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "İnternet bağlantısı bulunamadı! Lütfen internet bağlantınızı kontrol edin.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Burada ise api ye istek gönderirken header aracılığıyla giriş bilgimi gönderdim.
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "XxniniFBPXPReGo7NJ3zl0Vz7zliW75V5NaPE9pAwF01C98Cr3iYJakuhaU7");
                return params;
            }
        };

        //isteği RequestQueue ekledim.
        queue.add(stringRequest);
    }


}