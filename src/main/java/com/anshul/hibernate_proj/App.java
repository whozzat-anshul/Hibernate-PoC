package com.anshul.hibernate_proj;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class App {
	
	private static final SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	
	private static Session session;
	
	private User currentUser;
	
	App(){}
	
	private Session getSession() {
		if(session==null || !session.isOpen()) {
			session = factory.openSession();
		}
		
		return session;
	}
	
	private String getInput() {
		return new Scanner(System.in).nextLine();
	}
	
	private void showMenu() {
		System.out.println();
		System.out.println("--------------MENU--------------");
		System.out.println("1. Register");
		System.out.println("2. Log In");
		System.out.println("3. Show all Users");
		System.out.println("4. Show all ToDos");
		System.out.println("5. Quit");
		System.out.println();
	}
	
	private void Register() {
		System.out.print("Enter username: ");
		String username = getInput();
		
		Session session = getSession();
		
		User u = (User)session.createQuery("from User where name = :username").setParameter("username", username).uniqueResult();
		if(u!=null) {
			session.close();
			System.err.println("User already exist");
			return ;
		}
		
		User user = new User(username);
		Transaction tx = session.beginTransaction();
		long id = (Long)session.save(user);
		tx.commit();
		session.close();
		
		System.out.println("Registered successfully with id: " + id);
	}
	
	private void logIn() {
		System.out.print("Enter username: ");
		String username = getInput();
		
		Session session = getSession();
		User u = (User)session.createQuery("FROM User WHERE name = :username").setParameter("username", username).uniqueResult();
		session.close();
		
		if(u!=null) {
			currentUser = u;
			System.out.println("Log In successful");
			userDashBoard();
		}else {
			System.err.println("User not registered");
		}
	}
	
	@SuppressWarnings("unchecked")
	private void showAllUsers() {
		Session session = getSession();
		List<User> usersList = session.createQuery("FROM User").list();
		for(User user : usersList) {
			System.out.println("-------User-------");
			System.out.println("Id: " + user.getId());
			System.out.println("Name: " + user.getName());
			System.out.println("ToDos: " + user.getTodos().size());
			System.out.println();
		}
		session.close();
	}
	
	@SuppressWarnings("unchecked")
	private void showAllToDo(List<ToDo> todoList) {
		if(todoList == null) {
			Session session = getSession();
			todoList = session.createQuery("FROM ToDo").list();
			session.close();
		}
		
		for(ToDo todo : todoList) {
			System.out.println("-------ToDo-------");
			System.out.println("Id: " + todo.getId());
			System.out.println("Username: " + todo.getUser().getName());
			System.out.println("Title: " + todo.getTitle());
			System.out.println("Description: " + todo.getDescription());
			System.out.println();
		}
	}
	
	private void closeApp() {
		System.out.println("Closing...");
		
		if(session!=null || session.isOpen()) {
			session.close();
		}
		
		factory.close();
		System.exit(0);
	}
	
	private void userDashBoard() {
		showUserMenu();
		
		while(true) {
        	System.out.print("Choose option: ");
        	int input = Integer.parseInt(getInput());
        	
        	System.out.println();
        	
        	switch(input) {
        		case 1 : showAllToDo(currentUser.getTodos());
        			break;
        		case 2 : createToDo();
        			break;
        		case 3 : 
        			System.out.print("ToDo id: ");
        			updateToDo(Long.parseLong(getInput()));
        			break;
        		case 4 : 
        			System.out.print("ToDo id: ");
        			deleteToDo(Long.parseLong(getInput()));
        			break;
        		case 5 : goBack();
        			return ;
        		default : System.out.println("Incorrect input");
        			break;
        	}
        	
        	showUserMenu();
        }
	}
	
	private void showUserMenu() {
		System.out.println();
		System.out.println("--------------USER MENU--------------");
		System.out.println("1. Show Todos");
		System.out.println("2. New ToDo");
		System.out.println("3. Update ToDo");
		System.out.println("4. Delete ToDo");
		System.out.println("5. Back");
		System.out.println();
	}
	
	private String getTitle() {
		System.out.print("Title: ");
		String title = getInput();
		return title;
	}
	
	private String getDescription() {
		System.out.println("Describe: ");
		String description = getInput();
		return description;
	}
	
	private void createToDo() {
		String title = getTitle(), description = getDescription();
		ToDo todo = new ToDo(title, description, currentUser);
		
		Session session = getSession();
		
		Transaction tx = session.beginTransaction();
		long id = (Long)session.save(todo);
		tx.commit();
		
		currentUser = (User)session.get(User.class, currentUser.getId());
		session.close();
		
		System.out.println();
		System.out.println("ToDo id: " + id);
	}
	
	private void updateToDo(long id) {
		Session session = getSession();
		ToDo todo = (ToDo)session.get(ToDo.class, id);
		
		if(todo == null || todo.user.getId() != currentUser.getId()){
			System.out.println("Invalid id");
			return ;
		}
		
		String title = null, description = null;
		
		System.out.print("Update title (Y/N): ");
		String input = getInput().toLowerCase();
		if(input.equals("y")) {
			title = getTitle();
		}
		
		System.out.print("Update Discription (Y/N): ");
		input = getInput().toLowerCase();
		if(input.equals("y")) {
			description = getDescription();
		}
		
		todo.setTitle(title);
		todo.setDescription(description);
		
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(todo);
		tx.commit();
		
		currentUser = (User)session.get(User.class, currentUser.getId());
		session.close();
		
		System.out.println("\nToDo updated successfully");
	}
	
	private void deleteToDo(long id) {
		Session session = getSession();
		ToDo todo = (ToDo)session.get(ToDo.class, id);
		
		if(todo == null || todo.user.getId() != currentUser.getId()){
			System.out.println("Invalid id");
			return ;
		}
		
		Transaction tx = session.beginTransaction();
		session.delete(todo);
		tx.commit();
		
		session.clear();
		currentUser = (User)session.get(User.class, currentUser.getId());
		session.close();
		
		System.out.println("\nToDo deleted successfully");
	}
	
	private void goBack() {
		currentUser = null;
	}
	
    public static void main(String[] args) {
        System.out.println( "Application started..." );
        
        App app = new App();
        app.showMenu();
        
        while(true) {
        	System.out.print("Choose option: ");
        	int input = Integer.parseInt(app.getInput());
        	
        	System.out.println();
        	
        	switch(input) {
        		case 1 : app.Register();
        			break;
        		case 2 : app.logIn();
        			break;
        		case 3 : app.showAllUsers();
        			break;
        		case 4 : app.showAllToDo(null);
        			break;
        		case 5 : app.closeApp();
        			break;
        		default : System.out.println("Incorrect input");
        			break;
        	}
        	
        	app.showMenu();
        }
        
    }
    
}
