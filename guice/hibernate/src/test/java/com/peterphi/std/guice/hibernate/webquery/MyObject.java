package com.peterphi.std.guice.hibernate.webquery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
class MyObject
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "obj_name")
	private String name;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "other_object_id", nullable = true)
	private MyOtherObject otherObject;


	public Long getId()
	{
		return id;
	}


	public void setId(final Long id)
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


	public MyOtherObject getOtherObject()
	{
		return otherObject;
	}


	public void setOtherObject(final MyOtherObject otherObject)
	{
		this.otherObject = otherObject;
	}
}
