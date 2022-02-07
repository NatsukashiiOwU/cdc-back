package net.codejava;

import javax.persistence.*;

@Entity
@Table(name = "news")
public class News {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String cdc;

	@Column
	private String article;
	
	@Column
	private String date;
	
	@Column
	private String viewcount;

	@Column
	private String contents;
	
	@Column(length = 1)
	private Integer state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCdc() {
		return cdc;
	}

	public void setCdc(String cdc) {
		this.cdc = cdc;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getViewcount() {
		return viewcount;
	}

	public void setViewcount(String viewcount) {
		this.viewcount = viewcount;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Integer getState() {	return state; }

	public void setState(Integer state) { this.state = state; }

}
