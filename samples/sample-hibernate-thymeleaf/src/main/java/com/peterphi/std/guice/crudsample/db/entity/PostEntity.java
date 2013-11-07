package com.peterphi.std.guice.crudsample.db.entity;

import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "blog_post")
public class PostEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "poster_id", nullable = false)
	private UserEntity poster;

	@Column(name = "posted_ts", nullable = false)
	private DateTime posted = DateTime.now();

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "reply_to_id", nullable = true)
	private PostEntity replyTo;

	@Column(name = "comment", length = 160, nullable = false)
	private String comment;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "replyTo")
	private List<PostEntity> replies = new ArrayList<>();

	@Version
	@Column(name = "version", nullable = false)
	private int version = 0;


	public Long getId()
	{
		return id;
	}


	public void setId(final Long id)
	{
		this.id = id;
	}


	public UserEntity getPoster()
	{
		return poster;
	}


	public void setPoster(final UserEntity poster)
	{
		this.poster = poster;
	}


	public DateTime getPosted()
	{
		return posted;
	}


	public void setPosted(final DateTime posted)
	{
		this.posted = posted;
	}


	public PostEntity getReplyTo()
	{
		return replyTo;
	}


	public void setReplyTo(final PostEntity replyTo)
	{
		this.replyTo = replyTo;
	}


	public String getComment()
	{
		return comment;
	}


	public void setComment(final String comment)
	{
		this.comment = comment;
	}


	public List<PostEntity> getReplies()
	{
		return replies;
	}


	public void setReplies(final List<PostEntity> replies)
	{
		this.replies = replies;
	}


	public int getVersion()
	{
		return version;
	}


	public void setVersion(final int version)
	{
		this.version = version;
	}
}
