
import java.util.ArrayList;

public class Attributes {
    ArrayList<Attribute> attributeList = new ArrayList<>();
    Attribute attribute;

    public void addAttribute(String name, String value) {
        Attribute attribute = new Attribute(name, value);
        attributeList.add(attribute);

    }

    public void setAttribute(String name) {


    }
}
