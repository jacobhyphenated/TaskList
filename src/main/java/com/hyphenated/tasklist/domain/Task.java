package com.hyphenated.tasklist.domain;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Entity
@Table(name="task")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Task {

	@Column(name="task_id")
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="task_seq_gen")
	@SequenceGenerator(name="task_seq_gen", sequenceName="task_id_seq")
	private long id;
	
	@Column
	private String title;
	
	@Column
	private String description;
	
	@Column(name="due_date")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Timestamp dueDate;
	
	@Column(name="task_group")
	private String group;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Repeatable repeatable = Repeatable.NONE;
	
	@Column(name="complete_date")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Timestamp completeDate;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserEntity user;
	
	@JsonProperty("isActive")
	@Transient
	public boolean isActiveTask(){
		//If there is no due date, the task is active if there is no completion date
		if(dueDate == null){
			return completeDate == null;
		}
		
		//If the task is not repeatable, then it is only active if there is no complete date
		if(repeatable == null || repeatable == Repeatable.NONE){
			return completeDate == null;
		}
		
		//If the task is repeatable and has a due date, it is still active
		return true;
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the dueDate
	 */
	public Timestamp getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the repeatable
	 */
	public Repeatable getRepeatable() {
		return repeatable;
	}

	/**
	 * @param repeatable the repeatable to set
	 */
	public void setRepeatable(Repeatable repeatable) {
		this.repeatable = repeatable;
	}

	/**
	 * @return the completeDate
	 */
	public Timestamp getCompleteDate() {
		return completeDate;
	}

	/**
	 * @param completeDate the completeDate to set
	 */
	public void setCompleteDate(Timestamp completeDate) {
		this.completeDate = completeDate;
	}

	/**
	 * @return the user
	 */
	public UserEntity getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	public static class InstantSerializer extends JsonSerializer<Instant>{

		@Override
		public void serialize(Instant value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			
			//Use a ZoneOffset of 0 so we don't change time zones on the user
			//Values should be stored time zone agnostic with no time zone changes.
			jgen.writeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(
					LocalDateTime.ofInstant(value, ZoneOffset.ofHours(0) )));
			
		}
		
		
	}
}
