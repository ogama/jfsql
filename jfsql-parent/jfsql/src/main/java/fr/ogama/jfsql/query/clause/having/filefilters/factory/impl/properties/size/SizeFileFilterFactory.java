package fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.size;

import java.util.ArrayList;
import java.util.List;

import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.PropertyFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.PropertyFileFilter;
import fr.ogama.utils.parser.model.get.Constant;

public class SizeFileFilterFactory implements PropertyFactory {

	public PropertyFileFilter getPropertyFileFilter(List<Constant> values) {
		List<String> sizes = new ArrayList<String>();
		for (Constant constant : values) {
			sizes.add(constant.getValue());
		}
		return new SizeFileFilter(sizes);
	}

}