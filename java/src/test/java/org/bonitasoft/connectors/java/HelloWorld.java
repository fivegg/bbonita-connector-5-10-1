package org.bonitasoft.connectors.java;

public class HelloWorld extends Hello {

  private Integer position;

  public HelloWorld() {
    super();
  }

  public HelloWorld(String privateString, String protectedString,
      String publicString) {
    super(privateString, protectedString, publicString);
  }

  @SuppressWarnings("unused")
  private HelloWorld(String same) {
    this.position = new Integer(same);
  }

  public void add(Integer one, Integer two) {
    position = one + two;
  }

  public void sayHello() {
    System.out.println("Hello World!");
  }

  public void saySomething(String message) {
    System.out.println(message);
  }

  public void sayCompositeSomething(String message, Long number) {
    System.out.println(number + message);
  }

  public Integer addition() {
    System.out.println(position);
    return position;
  }
}
