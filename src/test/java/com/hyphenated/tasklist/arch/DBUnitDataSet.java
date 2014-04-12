package com.hyphenated.tasklist.arch;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Records the dataset that will be used by DBUnit for the test case.<br /><br />
 * Tries to load the dataset for the method. Otherwise, use a class dataset.
 * 
 * @author jacobhyphenated
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DBUnitDataSet{
	/**
	 * Class resource path.  Any file name starting with "/" is on the classpath root.
	 * Relative paths are relative to the class package.
	 * @return value
	 */
	String[] value();
}