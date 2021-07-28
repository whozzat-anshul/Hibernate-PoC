package com.anshul.hibernate_proj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ToDo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="todo_id")
	private long id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false, length = 1000)
	private String description;

	@ManyToOne
	@JoinColumn(name="user_id")//, referencedColumnName = "name")
	// if we want to reference column other than primary key use referencedColumnName also make other side column @NaturalId and implements Serializable on class
	User user;
	
	public ToDo() {}
	
	public ToDo(String title, String description, User user) {
		this.title = title;
		this.description = description;
		this.user = user;
	}

	@Override
	public String toString() {
		return "ToDo [id=" + id + ", title=" + title + ", description=" + description + ", user=" + user + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(title==null)
			return ;
		
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if(description==null)
			return ;
		
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
