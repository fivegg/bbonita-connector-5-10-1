package org.bonitasoft.connectors.bonita.filters;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FilterTests extends TestCase {

  public static Test suite() {
    TestSuite suite = new TestSuite("Filter Tests");
    suite.addTestSuite(AssignedUserTaskFilterTest.class);
    suite.addTestSuite(RandomMultipleFilterTest.class);
    suite.addTestSuite(UniqueRandomFilterTest.class);
    return suite;
  }

}
