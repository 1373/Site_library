package library;

import java.sql.Connection;
import java.sql.SQLException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class TestConnection extends TestCase {

    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        TestSuite suite = new TestSuite();
        suite.addTest(new TestConnection("testGetConnection"));
        runner.doRun(suite);
    }

    public TestConnection() {
    }

    public TestConnection(String name) {
        super(name);
    }

    public void testGetConnection() throws SQLException {
        Connection conn = ConnectionToDB.getConnection();
    }

}
