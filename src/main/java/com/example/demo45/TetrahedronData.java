package com.example.demo45;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class TetrahedronData {
    private double v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12;
    private String color;

    public TetrahedronData() {} // нужен дефолтный конструктор
    public TetrahedronData(double v1, double v2, double v3, double v4, double v5, double v6, double v7, double v8, double v9, double v10, double v11, double v12, String color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.v5 = v5;
        this.v6 = v6;
        this.v7 = v7;
        this.v8 = v8;
        this.v9 = v9;
        this.v10 = v10;
        this.v11 = v11;
        this.v12 = v12;
        this.color = color;
    }

    // Геттеры и сеттеры
    public double getV1() { return v1; }
    public void setV1(double v1) { this.v1 = v1; }
    public double getV2() { return v2; }
    public void setV2(double v2) { this.v2 = v2; }
    public double getV3() { return v3; }
    public void setV3(double v3) { this.v3 = v3; }
    public double getV4() { return v4; }
    public void setV4(double v4) { this.v4 = v4; }
    public double getV5() { return v5; }
    public void setV5(double v5) { this.v5 = v5; }
    public double getV6() { return v6; }
    public void setV6(double v6) { this.v6 = v6; }
    public double getV7() { return v7; }
    public void setV7(double v7) { this.v7 = v7; }
    public double getV8() { return v8; }
    public void setV8(double v8) { this.v8 = v8; }
    public double getV9() { return v9; }
    public void setV9(double v9) { this.v9 = v9; }
    public double getV10() { return v10; }
    public void setV10(double v10) { this.v10 = v10; }
    public double getV11() { return v11; }
    public void setV11(double v11) { this.v11 = v11; }
    public double getV12() { return v12; }
    public void setV12(double v12) { this.v12 = v12; }
    public String getColor() { return color; }
    public String setColor() { this.color = color;
        return color;
    }
    public static TetrahedronData loadFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, TetrahedronData.class);
    }
}




