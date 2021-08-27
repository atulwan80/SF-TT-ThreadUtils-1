package verify;

public record Verify() {
  public static void main(String[] args) {
    Object o = "Hello World!";
    if (o instanceof String s) {
      System.out.println(s);
    }
  }
}
