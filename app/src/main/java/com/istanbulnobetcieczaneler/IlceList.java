package com.istanbulnobetcieczaneler;

public class IlceList {

    String ilce_adi,ilce_uzantisi;

    public IlceList() {
    }

    public IlceList(String ilce_adi, String ilce_uzantisi) {
        this.ilce_adi = ilce_adi;
        this.ilce_uzantisi = ilce_uzantisi;
    }

    public String getIlce_adi() {
        return ilce_adi;
    }

    public void setIlce_adi(String ilce_adi) {
        this.ilce_adi = ilce_adi;
    }

    public String getIlce_uzantisi() {
        return ilce_uzantisi;
    }

    public void setIlce_uzantisi(String ilce_uzantisi) {
        this.ilce_uzantisi = ilce_uzantisi;
    }

    @Override
    public String toString() {
        return getIlce_adi();
    }
}
