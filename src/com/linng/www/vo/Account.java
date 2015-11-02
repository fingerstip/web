package com.linng.www.vo;

import org.springframework.data.annotation.Id;

public class Account {

	@Id
	private Integer id;

	private int age;

	private int balance;

	private String city;

	private String email;

	private String state;

	private String gender;

	private String address;

	private String employer;

	private String lastname;

	private String firstname;

	private Integer account_number;

	public Account() {
	}

	public Integer getAccount_number() {
		return account_number;
	}

	public void setAccount_number(Integer account_number) {
		this.account_number = account_number;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Account(int id, int account_number, int balance, String firstname,
			String lastname, int age, String gender, String address,
			String employer, String email, String city, String state) {
		this.id = id;
		this.account_number = account_number;
		this.balance = balance;
		this.firstname = firstname;
		this.lastname = lastname;
		this.age = age;
		this.gender = gender;
		this.address = address;
		this.employer = employer;
		this.email = email;
		this.city = city;
		this.state = state;
	}
}
