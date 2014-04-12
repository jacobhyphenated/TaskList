package com.hyphenated.tasklist.arch;


import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * Base Test Class for running DAO Tests using Spring and the Custom DB Unit Test Listener to load DBUnit datasets.
 * @author jacobhyphenated
 */
@ContextConfiguration(locations={ "classpath:spring-test-db.xml",
	"classpath:spring/spring-service.xml"})
@TestExecutionListeners(DBUnitTestExecutionListener.class)
public abstract class BaseUnitilsDatasetSpringText extends AbstractTransactionalJUnit4SpringContextTests{

}
