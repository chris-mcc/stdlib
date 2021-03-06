package com.peterphi.std.util.jaxb.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Encodes one or more XSDs in a single XML
 */
@XmlRootElement(name = "SchemaFiles")
public class MultiXSDSchemaFiles
{
	@XmlElement(name = "SchemaFile")
	public List<MultiXSDSchemaFile> files = new ArrayList<>();
}
