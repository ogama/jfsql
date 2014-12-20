package fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.type;

import java.util.ArrayList;
import java.util.List;

import org.gibello.zql.ZConstant;

import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.PropertyFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.PropertyFileFilter;

public class TypeFileFilterFactory implements PropertyFactory {

	@Override
	public PropertyFileFilter getPropertyFileFilter(List<ZConstant> values) {
		List<String> types = new ArrayList<String>();
		for (ZConstant constant : values) {
			types.add(constant.getValue());
		}
		return new TypeFileFilter(types);
	}

}
