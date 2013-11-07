package com.peterphi.std.guice.crudsample.db.entity;

import com.peterphi.std.util.HexHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity(name = "blog_user")
public class UserEntity
{
	@Id
	private String id;

	@Column(name = "full_name", length = 160, nullable = false)
	private String name;

	@Column(name = "password_hash", length = 100, nullable = false)
	private String passwordHash;

	@Column(name = "session_id", length = 15, nullable = false)
	private String sessionId = HexHelper.generateHex(15);

	@Version
	@Column(name = "version", nullable = false)
	private int version = 0;


	public String getId()
	{
		return id;
	}


	public void setId(final String id)
	{
		this.id = id;
	}


	public String getName()
	{
		return name;
	}


	public void setName(final String name)
	{
		this.name = name;
	}


	public String getSessionId()
	{
		return sessionId;
	}


	public void setSessionId(final String sessionId)
	{
		this.sessionId = sessionId;
	}


	public String getPasswordHash()
	{
		return passwordHash;
	}


	public void setPasswordHash(final String passwordHash)
	{
		this.passwordHash = passwordHash;
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
