import java.util.ArrayList;

public class KeyValue {
    String name;
    String value;
    String dataType;
    boolean isArray;
    String qt = "\"";

    KeyValue() {
    }

    KeyValue(String name, String value, boolean isArray, String dataType) {
        this.name = name;
        this.value = value;
        this.dataType = dataType;
        this.isArray = false;
    }

    public String getJsonTerm(KeyValue keyValue) {
        String jsonStr = qt + keyValue.name + qt + ":";
        switch (keyValue.dataType) {
            case "String":
            case "Date":
                jsonStr += qt + keyValue.value + qt;
                break;
            case "Number":
                jsonStr +=  keyValue.value;
                break;
            default:
                jsonStr += qt + keyValue.value + qt;
                break;
        }
        return jsonStr;
    }
    public String getJsonTerm(ArrayList<KeyValue> keyValueList) {
         int no = -1;
         String jsonStr = "{";
         int count = keyValueList.size();
         while (++no < keyValueList.size()) {
            jsonStr += getJsonTerm(keyValueList.get(no)) + ",";
            if (no == count - 1)
                jsonStr += getJsonTerm(keyValueList.get(no)) + "}";
            else
                jsonStr += getJsonTerm(keyValueList.get(no)) + ",";
         }
         return jsonStr;
    }
}


