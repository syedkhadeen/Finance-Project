

import java.time.LocalDate;

public class Attribute {
    String name;
    String value;
    double dVal;
    int iVal;
    Dates dtDate;


    Attribute() {

    }

    Attribute(String name, String value) {
        this.name = name;
        this.value = value;

    }


    public String getString() {
        return this.value;
    }

    public int getInt() {
        int iVal = 0;
        try {
            iVal = Integer.parseInt(value);
        } catch (Exception ex) {
            String error = "Error, can not convert " + name + ":" + value + " to integer";
        }
        return iVal;
    }

    public double getDouble() {
        double dVal = 0.;
        try {
            dVal = Double.valueOf(value);
        } catch (Exception ex) {
            String edror = "Error, can not convert " + name + ":" + value + "to double";
        }
        return dVal;
    }

    public LocalDate getDate() {
        LocalDate localDate = null;
        int year = 0, month = 0, day = 0;
        try {
            String[] parts = value.split("-");
            year = Integer.valueOf(parts[0]);
            month = Integer.valueOf(parts[1]);
            day = Integer.valueOf(parts[2]);
            LocalDate date = LocalDate.of(year, month, day);
        } catch (Exception ex) {
            String edror = "Error, can not convert " + name + ":" + value + "to local date";
        }
        return localDate;
    }

    public Attribute(Dates dtDate, double dVal ) {
        this.name = name;
        this.dtDate = dtDate;
        this.dVal = dVal;
    }

    public String getArray() {
        return value;
    }

}
