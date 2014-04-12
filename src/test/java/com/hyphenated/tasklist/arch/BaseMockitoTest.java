package com.hyphenated.tasklist.arch;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public abstract class BaseMockitoTest {

	@Before
	public void beforeMethod() throws Exception{
		MockitoAnnotations.initMocks(this);
	}
}
