package com.example.demo45;

public class DataModel {
    private double[] values = new double[12];
    private String color;
    private static final DataModel instance = new DataModel();

    private TetrahedronData tetrahedronData;

    public DataModel() { }

    public static DataModel getInstance() {
        return instance;
    }

    public void setTetrahedronData(TetrahedronData tetrahedronData) {
        this.tetrahedronData = tetrahedronData;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
