package org.bonitasoft.connectors.java;

public class Hello {

  private String privateString;
  protected String protectedString;
  public String publicString;

  public Hello() {
    privateString = "private";
    protectedString = "protected";
    publicString = "public";
  }

  public Hello(String privateString, String protectedString, String publicString) {
    this.privateString = privateString;
    this.protectedString = protectedString;
    this.publicString = publicString;
  }

  public String getPrivateString() {
    return privateString;
  }

  public String getProtectedString() {
    return protectedString;
  }

  public String getPublicString() {
    return publicString;
  }
  
  public void printStrings() {
    System.out.println(privateString + " : " + protectedString + " : " + publicString);
  }
}
