package com.anshul.hibernate_proj;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private long id;
	
	@Column(unique = true, nullable = false)
	private String name;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<ToDo> todos;
	
	public User() {}
	
	public User(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ToDo> getTodos() {
		return todos;
	}

	public void setTodos(List<ToDo> todos) {
		this.todos = todos;
	}
	
}
