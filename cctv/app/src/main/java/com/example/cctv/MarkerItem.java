package com.example.cctv;

public class MarkerItem {
    private String LAT;
    private  String LOGT;
    private  String addr;
    private  String tel;
    private String Purpose;
    private  String Installation;
    private  String Storage;
    private  String DataDate;
    private String CNT;

    public MarkerItem(String LAT, String LOGT, String addr, String tel, String purpose, String installation, String storage,String cnt, String dataDate) {
        this.LAT = LAT;
        this.LOGT = LOGT;
        this.addr = addr;
        this.tel = tel;
        Purpose = purpose;
        Installation = installation;
        Storage = storage;
        DataDate = dataDate;
        this.CNT = cnt;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLOGT() {
        return LOGT;
    }

    public void setLOGT(String LOGT) {
        this.LOGT = LOGT;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getInstallation() {
        return Installation;
    }

    public void setInstallation(String installation) {
        Installation = installation;
    }

    public String getStorage() {
        return Storage;
    }

    public void setStorage(String storage) {
        Storage = storage;
    }

    public String getDataDate() {
        return DataDate;
    }

    public void setDataDate(String dataDate) {
        DataDate = dataDate;
    }

    public String getCNT() {
        return CNT;
    }

    public void setCNT(String cnt) { CNT = cnt; }
}