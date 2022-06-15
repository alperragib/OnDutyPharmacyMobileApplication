package com.istanbulnobetcieczaneler;

public class Eczane {

    String isim,adres,adres_tarifi,tel_no;
    double latitude,longitude;

    public Eczane() {
    }

    public Eczane(String isim, String adres, String adres_tarifi, String tel_no, double latitude, double longitude) {
        this.isim = isim;
        this.adres = adres;
        this.adres_tarifi = adres_tarifi;
        this.tel_no = tel_no;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getAdres_tarifi() {
        return adres_tarifi;
    }

    public void setAdres_tarifi(String adres_tarifi) {
        this.adres_tarifi = adres_tarifi;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
