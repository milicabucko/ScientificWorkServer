package com.scientificworkserver.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.scientificworkserver.lucene.indexing.filters.CyrillicLatinConverter;

@Entity
@Document(indexName = "scientificwork", type = "work",shards=1, replicas=0)
public class ScientificWork implements Serializable {

	private static final long serialVersionUID = -5751071733158938457L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Field(type=FieldType.Long, index = FieldIndex.not_analyzed, store = true)
	private Long id;
	
	@Field(type=FieldType.String, index=FieldIndex.analyzed, store = true)
	private String title;

	@Field(type=FieldType.String, index=FieldIndex.analyzed, store = true)
	private String author;
	
	@Transient
	private String highlight;
	
    @Field(type = FieldType.String, index = FieldIndex.analyzed, store = true)
    @Transient
    private String text;

	@Field(type=FieldType.String,index=FieldIndex.analyzed, store = true)
	private String keywords;

	@Field(type=FieldType.String,index=FieldIndex.analyzed, store = true)
	private String magazine;

	@Field(type=FieldType.String,index=FieldIndex.analyzed, store = true)
	private String category;

	@Field(type=FieldType.Integer, store = true)
	private Integer publicationYear;

	@Field(type=FieldType.Boolean, store = true)
	private Boolean openAccess;

	@Column(unique=true, nullable=false)
	@Field(type=FieldType.String, index=FieldIndex.analyzed, store = true)
	private String filename;

	@Field(type=FieldType.String,index=FieldIndex.analyzed, store = true)
	private String mime;
	
	@ManyToOne(optional = true)
	private User user;
	
	@ManyToOne
	private Language language;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getMagazine() {
		return magazine;
	}

	public void setMagazine(String magazine) {
		this.magazine = magazine;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getOpenAccess() {
		return openAccess;
	}

	public void setOpenAccess(Boolean openAccess) {
		this.openAccess = openAccess;
	}

	public void convertToLat() {
		this.text = CyrillicLatinConverter.cir2lat(text.toLowerCase());
    	this.title = CyrillicLatinConverter.cir2lat(title.toLowerCase());
        this.author = CyrillicLatinConverter.cir2lat(author.toLowerCase());
        this.keywords = CyrillicLatinConverter.cir2lat(keywords.toLowerCase());
        this.magazine = CyrillicLatinConverter.cir2lat(magazine.toLowerCase());
        this.category = CyrillicLatinConverter.cir2lat(category.toLowerCase());
    }
	
	
}
