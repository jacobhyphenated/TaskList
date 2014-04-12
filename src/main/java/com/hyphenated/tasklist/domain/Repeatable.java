package com.hyphenated.tasklist.domain;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public enum Repeatable {
	NONE(null),
	DAILY(ChronoUnit.DAYS),
	WEEKLY(ChronoUnit.WEEKS),
	MONTHLY(ChronoUnit.MONTHS),
	YEARLY(ChronoUnit.YEARS);
	
	private TemporalUnit timeUnit;
	
	private Repeatable(TemporalUnit timeUnit){
		this.timeUnit = timeUnit;
	}
	
	public TemporalUnit getTimeUnit(){
		return timeUnit;
	}
}
