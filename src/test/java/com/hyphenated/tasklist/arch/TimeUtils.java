package com.hyphenated.tasklist.arch;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public interface TimeUtils {

	default public Timestamp timeZoneAdjustedDate(String formattedDate){
		LocalDateTime  due = LocalDateTime.parse(formattedDate);
		return Timestamp.valueOf(due);
	}
	
	default public LocalDateTime localDateFromInstant(Instant instant){
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}
}
