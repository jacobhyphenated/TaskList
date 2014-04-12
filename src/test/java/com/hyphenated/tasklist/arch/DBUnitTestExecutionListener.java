package com.hyphenated.tasklist.arch;

import java.io.InputStream;
import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * 
 * Unit Test Execution listener used to load DBUnit Dataset files when the Spring DataSource
 * is not accessible and the test are being run with Spring.
 * <br /><br />
 * This listener will use the ${code @DBUnitDataSet} annotation to determine what dataset file to load.
 * 
 * @author jacobhyphenated
 */
public class DBUnitTestExecutionListener extends AbstractTestExecutionListener {

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception{
		//no method level datasource, so attempt to load the class datasource
		Class<?> clazz = testContext.getTestClass();
		DBUnitDataSet annotation = clazz.getAnnotation(DBUnitDataSet.class);
		if(annotation != null){
			loadDatasource(annotation, testContext);
		}
		//Try to load method specific data set
		Method method = testContext.getTestMethod();
		annotation = method.getAnnotation(DBUnitDataSet.class);
        if (annotation != null) {
        	loadDatasource(annotation, testContext);
        }
	}
	
	@Override
	public void afterTestClass(TestContext testContext) throws Exception{
		//Clear Database at the end of the class execution
		DataSource dataSource = testContext.getApplicationContext().getBean(DataSource.class);
		dataSource.getConnection().createStatement().execute("TRUNCATE SCHEMA PUBLIC AND COMMIT NO CHECK");
	}
	
	private void loadDatasource(DBUnitDataSet annotation, TestContext testContext) throws Exception{
		DataSource dataSource = testContext.getApplicationContext().getBean(DataSource.class);
		IDatabaseTester databaseTester = new DataSourceDatabaseTester(dataSource, "PUBLIC");	
		
		//Ignore referential integrity so DBUnit can correctly delete rows related to the data set
		dataSource.getConnection().createStatement().execute("SET DATABASE REFERENTIAL INTEGRITY FALSE;");

		for(String sourceFile : annotation.value()){
			InputStream input = this.getClass().getResourceAsStream(sourceFile);
			FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
			builder.setColumnSensing(true);
			IDataSet ds = builder.build(input);
			databaseTester.setDataSet(ds);
			databaseTester.onSetup();			
		}
		
		//Add Referential constraints back in, now that the dbunit setup is complete
		dataSource.getConnection().createStatement().execute("SET DATABASE REFERENTIAL INTEGRITY TRUE;");
	}
}
