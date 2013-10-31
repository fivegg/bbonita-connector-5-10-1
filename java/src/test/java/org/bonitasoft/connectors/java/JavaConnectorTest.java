package org.bonitasoft.connectors.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

public class JavaConnectorTest extends TestCase {

  protected static final Logger LOG = Logger.getLogger(JavaConnectorTest.class.getName());

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    if (JavaConnectorTest.LOG.isLoggable(Level.WARNING)) {
      JavaConnectorTest.LOG.warning("======== Starting test: " + this.getClass().getName() + "." + getName()
          + "() ==========");
    }
  }

  @Override
  protected void tearDown() throws Exception {
    if (JavaConnectorTest.LOG.isLoggable(Level.WARNING)) {
      JavaConnectorTest.LOG.warning("======== Ending test: " + getName() + " ==========");
    }
    super.tearDown();
  }

  public void testValidateConnector() throws BonitaException {
    final List<ConnectorError> errors = Connector.validateConnector(JavaConnector.class);
    for (final ConnectorError error : errors) {
      System.out.println(error.getField() + " " + error.getError());
    }
    Assert.assertTrue(errors.isEmpty());
  }

  public void testNullClassName() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("sayHello");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(null);
    connector.setMethods(methods);
    containsErrors(connector, 1);
  }

  public void testEmptyClassName() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("sayHello");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName("");
    connector.setMethods(methods);
    containsErrors(connector, 1);
  }

  public void testUnknownClassName() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("sayHello");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName("org.bonitasoft.connectors.java.HelloEarth");
    connector.setMethods(methods);
    containsErrors(connector, 1);
  }

  public void testNullMethods() throws BonitaException {
    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setMethods(null);
    containsErrors(connector, 1);
  }

  public void testUnknownMethodName() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("sayNothing");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setMethods(methods);
    containsErrors(connector, 1);
  }

  public void testSimpleExecution() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("sayHello");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setMethods(methods);
    work(connector);
  }

  public void testExecutionWithAMethodUsingAParameter() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("saySomething");
    first.add("Bye");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setMethods(methods);
    work(connector);
  }

  public void testExecutionWithMainMethodParameters() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("sayCompositeSomething");
    first.add("Hello World");
    first.add(new Long(10));
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setMethods(methods);
    work(connector);
  }

  public void testExecutionWithANullMainMethodParameters() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("sayCompositeSomething");
    first.add(null);
    first.add(new Long(10));
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setMethods(methods);
    work(connector);
  }

  public void testExecutionWithAnEmptyMainMethodParameters() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("sayCompositeSomething");
    first.add("");
    first.add(new Long(10));
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setMethods(methods);
    work(connector);
  }

  public void testExecutionWithParameters() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("add");
    first.add(new Integer(4));
    first.add(new Integer(10));
    methods.add(first);
    final List<Object> second = new ArrayList<Object>();
    second.add("addition");
    methods.add(second);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setMethods(methods);
    work(connector);
  }

  public void testExecutionWithNullMethodName() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add(null);
    first.add(new Integer(4));
    first.add(new Integer(10));
    methods.add(first);
    final List<Object> second = new ArrayList<Object>();
    second.add("addition");
    methods.add(second);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setMethods(methods);
    containsErrors(connector, 1);
  }

  public void testExecutionWithNullMethodNameAndParameters() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add(null);
    methods.add(first);
    final List<Object> second = new ArrayList<Object>();
    second.add("addition");
    methods.add(second);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setMethods(methods);
    containsErrors(connector, 1);
  }

  public void testExecutionWithField() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("addition");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    final Map<String, Object> fields = new HashMap<String, Object>();
    fields.put("position", new Integer(10));
    connector.setFields(fields);
    connector.setMethods(methods);
    work(connector);
  }

  public void testExecutionWithAConstructor() throws BonitaException {
    final ArrayList<Object> params = new ArrayList<Object>();
    params.add("a");
    params.add("b");
    params.add("c");

    final Map<String, Object> fields = new HashMap<String, Object>();
    fields.put("position", new Integer(10));

    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("addition");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setConstructorParameters(params);
    connector.setFields(fields);
    connector.setMethods(methods);
    work(connector);
  }

  public void testExecutionWithFields() throws BonitaException {
    final Map<String, Object> fields = new HashMap<String, Object>();
    fields.put("privateString", "private ?");
    fields.put("protectedString", "protected ?");
    fields.put("publicString", "public ?");

    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("printStrings");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setFields(fields);
    connector.setMethods(methods);
    work(connector);
  }

  public void testExecutionWithNullFields() throws BonitaException {
    final Map<String, Object> fields = new HashMap<String, Object>();
    fields.put("privateString", "private ?");
    fields.put("protectedString", "protected ?");
    fields.put("publicString", null);

    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("printStrings");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setFields(fields);
    connector.setMethods(methods);
    work(connector);
  }

  public void testExecutionWithPrivateConstructor() throws BonitaException {
    final List<Object> params = new ArrayList<Object>();
    params.add("12");

    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("addition");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(HelloWorld.class.getName());
    connector.setConstructorParameters(params);
    connector.setMethods(methods);
    work(connector);
  }

  public void testInvokeAMethodFromAnAbstractClass() throws BonitaException {
    final List<List<Object>> methods = new ArrayList<List<Object>>();
    final List<Object> first = new ArrayList<Object>();
    first.add("sayHello");
    methods.add(first);

    final JavaConnector connector = new JavaConnector();
    connector.setClassName(AbstractHello.class.getName());
    connector.setMethods(methods);
    work(connector);
  }

  public void work(final Connector connector) throws BonitaException {
    if (connector.containsErrors()) {
      containsErrors(connector, 1);
      fail(connector.getClass().getName() + " contains errors!");
    }
    try {
      connector.execute();
    } catch (final Exception e) {
      e.printStackTrace();
      fail("The execution of " + connector.getClass().getName() + " should work.");
    }
  }

  public void containsErrors(final Connector connector, final int errorNumber) throws BonitaException {
    final List<ConnectorError> errors = connector.validate();
    for (final ConnectorError error : errors) {
      System.out.println(error.getField() + " " + error.getError());
    }
    Assert.assertEquals(errorNumber, errors.size());
  }

  public void fail(final Connector connector) {
    try {
      connector.execute();
      fail("It should fail!");
    } catch (final Exception e) {
    }
  }
}
