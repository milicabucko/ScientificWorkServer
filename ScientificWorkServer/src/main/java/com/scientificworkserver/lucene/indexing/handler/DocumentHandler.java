package com.scientificworkserver.lucene.indexing.handler;

import java.io.File;

import com.scientificworkserver.model.ScientificWork;

public abstract class DocumentHandler {

	public abstract ScientificWork getScientificWork(File file);

	public abstract String getText(File file);

}
