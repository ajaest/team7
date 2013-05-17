package se.chalmers.eda397.team7.so.data.entity;

import java.util.Map;
import java.util.Set;

public interface FullTextable {

	public Map<String,Set<String>> getFullTextIndexes();
	
	
}
