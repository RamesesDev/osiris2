/*
 * SchemaTest.java
 * JUnit based test
 *
 * Created on December 28, 2009, 10:03 PM
 */

package dmstest;

import com.rameses.dms.DataSet;
import com.rameses.dms.Migrator;
import com.rameses.dms.SchemaManager;
import com.rameses.dms.SqlUtil;
import com.rameses.dms.datasource.DataSourceManager;
import com.rameses.dms.dialects.MySqlDialect;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class SchemaTest extends TestCase {
    
    public SchemaTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void XtestSchema() throws Exception{
        DataSet ds = SchemaManager.getSchema("sample").newInstance();
        MySqlDialect md = new MySqlDialect();
        System.out.println( md.getSourceListSql(ds.getTableInstance()));
    }
    
    public void xtestSqlUtil() throws Exception{
        String sql = "select o where id = $P{p1} and data = $P{p2}";
        StringBuffer buffer = new StringBuffer();
        List params = new ArrayList();
        SqlUtil.parseStatement(sql, buffer, params);
        System.out.println(buffer.toString());
        for(Object o: params)System.out.println(o);
    }
    
    public void xtestSqlStatements() throws Exception{
        DataSet ds = SchemaManager.getSchema("sample").newInstance();
        MySqlDialect d = new MySqlDialect();
        System.out.println("STARTING TEST");
        System.out.println("**** create table ****");
        System.out.println( d.getCreateTable(ds.getTableInstance()));        
        System.out.println("**** select ****");
        System.out.println( d.getSourceListSql(ds.getTableInstance()));
        System.out.println("**** test insert ****");
        System.out.println(d.getInsertSql(ds.getTableInstance()));
    }
    
    public void xtestConnect() throws Exception  {
        DataSource ds = DataSourceManager.getDataSource("DefaultDS");
        System.out.println(ds.getConnection());
    }
    
    public void testMigrate() throws Exception {
        DataSet ds = SchemaManager.getSchema("sample").newInstance();
        ds.getTableInstance().setTablename("sample3");
        ds.getParams().put("fname", "TEST3");
        Migrator m = new Migrator(ds);
        m.setSourceDS( DataSourceManager.getDataSource("mlkpDS") );
        m.setTargetDS( DataSourceManager.getDataSource("target") );
        m.execute();
        
    }
    
    
}
