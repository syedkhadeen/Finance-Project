
public class Holiday {
    Dates dtHoliday = new Dates();
    String countryExchange;
    char type; // T = trade, P = payment, S = settle

    Holiday() {
        dtHoliday = new Dates();
        countryExchange = UnAvails.NOTAVAILSTRING;
        type = 'S';

    }

    void setHolidayDt(Dates date) {
        this.dtHoliday = date;
    }

    void setCountry(String isoCountry) {
        this.countryExchange = isoCountry;
    }

    void setType(char type) {
        this.type = type;
    }


}