package uk.co.revsys.jsont.jexl;

public class CoreUtil {

    public String toStringSafe(Object value) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

}
