package com.istanbulnobetcieczaneler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class EczanelerAdapter extends RecyclerView.Adapter<EczanelerAdapter.CardViewTasarimTutucu> {

    private Context context;
    private List<Eczane> eczaneList;

    public EczanelerAdapter(Context context, List<Eczane> eczaneList) {

        this.context = context;
        this.eczaneList = eczaneList;

    }

    public class CardViewTasarimTutucu extends RecyclerView.ViewHolder {
        public TextView isim, adres, adres_tarifi;
        public Button hemen_ara, yol_tarifi;
        public LinearLayout linearLayoutAdresTarifi;

        public CardViewTasarimTutucu(@NonNull View itemView) {
            super(itemView);
            //Tasarım ile değişkenler asarında bağlantı sağlandı
            isim = itemView.findViewById(R.id.textViewEczaneBaslik);
            adres = itemView.findViewById(R.id.textViewEczaneAdres);
            adres_tarifi = itemView.findViewById(R.id.textViewEczaneAdresTarifi);
            hemen_ara = itemView.findViewById(R.id.buttonEczaneAra);
            yol_tarifi = itemView.findViewById(R.id.buttonEczaneYolTarifi);
            linearLayoutAdresTarifi = itemView.findViewById(R.id.linearLayoutAdresTarifi);


        }
    }

    @NonNull
    @Override
    public EczanelerAdapter.CardViewTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Yaptığım kart tasarımını adaptöre bağladım
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.eczane_card, parent, false);
        return new CardViewTasarimTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EczanelerAdapter.CardViewTasarimTutucu holder, int position) {

        final Eczane eczane = eczaneList.get(position); //Listedeki eczane öğesi alındı.
        holder.isim.setText(eczane.getIsim()); //Gelen verilerle textview ler dolduruldu.
        holder.adres.setText(eczane.getAdres());
        if(eczane.getAdres_tarifi().length()<=0){ // Eğer adres tarifi yoksa bu alanın gösterilmesini istemedim.
            holder.linearLayoutAdresTarifi.setVisibility(View.GONE);
        }
        holder.adres_tarifi.setText(eczane.getAdres_tarifi());


        holder.hemen_ara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Burada butana tıklayınca arama kısmına yönlendirme işlemi yaptım.
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", eczane.getTel_no(), null));
                context.startActivity(intent);
            }
        });

        holder.yol_tarifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Burada ise butana tıklayınca Google Maps den konuma yönlendirme yaptım.
                String geoUri = "http://maps.google.com/maps?q=loc:" + eczane.getLatitude() + "," + eczane.getLongitude() + " (" + eczane.getIsim() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return eczaneList.size();
    }

}
