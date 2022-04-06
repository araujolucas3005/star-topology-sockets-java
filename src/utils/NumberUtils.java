package utils;

public class NumberUtils {

  public static boolean isDigit(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return false;
    }

    return true;
  }

}
