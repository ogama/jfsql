package fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.content;

import java.util.ArrayList;
import java.util.List;

import org.gibello.zql.ZConstant;

import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.PropertyFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.PropertyFileFilter;

public class ContentFileFilterFactory implements PropertyFactory {

	public PropertyFileFilter getPropertyFileFilter(List<ZConstant> values) {
		List<String> contents = new ArrayList<String>();
		for (ZConstant constant : values) {
			contents.add(constant.getValue());
		}
		return new ContentFileFilter(contents);
	}
}
