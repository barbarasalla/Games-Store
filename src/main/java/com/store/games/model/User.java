package com.store.games.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name="tb_user")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotBlank
	@Size(min=3)
	@Column(name = "name")
	private String name;
	
	@Schema
	@NotNull
	@Email(message = "O Usuário deve ser um email válido!")
	@Column(name = "userName")
	private String userName;
	
	@NotBlank
	@Size(min=5)
	@Column(name = "password")
	private String password;
	
	
	@Column(name = "birthDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private LocalDate birth;

	@Column(name = "photo")
	private String photo;
	
	@NotNull
	@Column(name = "userType")
	private String userType;

	@OneToMany(mappedBy="user", cascade= CascadeType.ALL)
	@JsonIgnoreProperties("user")
	private List<Review> review;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<Review> getReview() {
		return review;
	}

	public void setReview(List<Review> review) {
		this.review = review;
	}
}
