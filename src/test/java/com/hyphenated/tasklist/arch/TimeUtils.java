package com.hyphenated.tasklist.arch;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface TimeUtils {

	default public Timestamp timeZoneAdjustedDate(String formattedDate){
		LocalDateTime  due = LocalDateTime.parse(formattedDate);
		return Timestamp.from(due.toInstant( ZoneOffset.ofHours(0)));
	}
	
	default public LocalDateTime localDateFromInstant(Instant instant){
		return LocalDateTime.ofInstant(instant,  ZoneOffset.ofHours(0));
	}
}
