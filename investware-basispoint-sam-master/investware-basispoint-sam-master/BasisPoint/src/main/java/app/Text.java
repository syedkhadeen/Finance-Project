package app;

public class Text {
  public static String toSentenceCase(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }
    StringBuilder result = new StringBuilder();
    boolean capitalizeNext = true;
    for (char c : input.toCharArray()) {
      if (Character.isWhitespace(c)) {
        capitalizeNext = true;
      } else if (capitalizeNext) {
        c = Character.toUpperCase(c);
        capitalizeNext = false;
      } else {
        c = Character.toLowerCase(c);
      }
      result.append(c);
    }

    return result.toString();
  }
}