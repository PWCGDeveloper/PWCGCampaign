package pwcg.mission;

import pwcg.core.exception.PWCGException;

public interface IMissionDescription {

	String createDescription() throws PWCGException;

	String getHtml();

	String getTitle();

	String getAuthor();
}