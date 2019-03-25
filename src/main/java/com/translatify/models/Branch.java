package com.translatify.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Branch implements Serializable {

	private static final long serialVersionUID = 300L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private Document document;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Branch base;
	
	@OneToMany(mappedBy="base")
	private List<Branch> forks;
	
	@OneToMany
	List<Comment> comments;
	
	@NotEmpty
	@Column(nullable = false)
	private String name;
	
	@NotEmpty
	@Column(nullable = false)
	private String flag;
	
	@NotEmpty
	@Column(nullable = false)
	private String language;
	
	@NotEmpty
	@Column(nullable = false, length = 65535, columnDefinition="text")
	private String content;
	
	@Column(columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	public Branch() {}

	public Branch(Document document, User user, Branch base, @NotEmpty String name, @NotEmpty String flag,
			@NotEmpty String language, @NotEmpty String content, Date date) {
		super();
		this.document = document;
		this.user = user;
		this.base = base;
		this.name = name;
		this.flag = flag;
		this.language = language;
		this.content = content;
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Branch getBase() {
		return base;
	}

	public void setBase(Branch base) {
		this.base = base;
	}

	public List<Branch> getForks() {
		return forks;
	}

	public void setForks(List<Branch> forks) {
		this.forks = forks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
