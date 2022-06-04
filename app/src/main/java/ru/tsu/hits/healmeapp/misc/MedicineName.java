package ru.tsu.hits.healmeapp.misc;

import java.io.Serializable;

public class MedicineName implements Serializable {
    private String medicine_ID;
    private String medicine_Name;
    private String medicine_Quantity;

    public String getMedicine_ID() {
        return medicine_ID;
    }

    public void setMedicine_ID(String medicine_ID) {
        this.medicine_ID = medicine_ID;
    }

    public String getMedicine_Name() {
        return medicine_Name;
    }

    public void setMedicine_Name(String medicine_Name) {
        this.medicine_Name = medicine_Name;
    }

    public String getMedicine_Quantity() {
        return medicine_Quantity;
    }

    public void setMedicine_Quantity(String medicine_Quantity) {
        this.medicine_Quantity = medicine_Quantity;
    }

    @Override
    public String toString() {
        return medicine_Name;
    }
}
