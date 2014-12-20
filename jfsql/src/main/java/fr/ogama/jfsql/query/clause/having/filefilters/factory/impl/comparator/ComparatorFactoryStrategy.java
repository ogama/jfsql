package fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.gibello.zql.ZConstant;
import org.gibello.zql.ZExp;
import org.gibello.zql.ZExpression;

import fr.ogama.jfsql.query.clause.having.filefilters.factory.FileFilterFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.PropertyFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.PropertyFileFilter;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.content.ContentFileFilterFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.creationdate.CreationDateFileFilterFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.lastaccessdate.LastAccessDateFileFilterFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.lastmodificationdate.LastModificationFileFilterFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.name.NameFileFilterFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.owner.OwnerFileFilterFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.parent.ParentFileFilterFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.path.PathFileFilterFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.size.SizeFileFilterFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.status.StatusFileFilterFactory;
import fr.ogama.jfsql.query.clause.having.filefilters.factory.impl.properties.type.TypeFileFilterFactory;
import fr.ogama.jfsql.query.clause.having.operators.comparaison.Between;
import fr.ogama.jfsql.query.clause.having.operators.comparaison.ComparatorOperator;
import fr.ogama.jfsql.query.clause.having.operators.comparaison.Equals;
import fr.ogama.jfsql.query.clause.having.operators.comparaison.GratherThan;
import fr.ogama.jfsql.query.clause.having.operators.comparaison.GratherThanOrEqual;
import fr.ogama.jfsql.query.clause.having.operators.comparaison.In;
import fr.ogama.jfsql.query.clause.having.operators.comparaison.LessThan;
import fr.ogama.jfsql.query.clause.having.operators.comparaison.LessThanOrEqual;
import fr.ogama.jfsql.query.clause.having.operators.comparaison.Like;
import fr.ogama.jfsql.query.clause.having.operators.comparaison.Unequal;

public class ComparatorFactoryStrategy implements FileFilterFactory {

	private Map<String, Class<? extends ComparatorOperator>> operatorStrategy;
	private Map<String, PropertyFactory> propertyStrategy;
	private Map<String, String> subQueries;

	public ComparatorFactoryStrategy(Map<String, String> subQueries) {
		this.subQueries = subQueries;
		
		operatorStrategy = new HashMap<String, Class<? extends ComparatorOperator>>();
		operatorStrategy.put("=", Equals.class);
		operatorStrategy.put("<>", Unequal.class);
		operatorStrategy.put(">", GratherThan.class);
		operatorStrategy.put(">=", GratherThanOrEqual.class);
		operatorStrategy.put("<", LessThan.class);
		operatorStrategy.put("<=", LessThanOrEqual.class);
		operatorStrategy.put("like", Like.class);
		operatorStrategy.put("in", In.class);
		operatorStrategy.put("between", Between.class);

		propertyStrategy = new HashMap<String, PropertyFactory>();
		propertyStrategy.put("name", new NameFileFilterFactory());
		propertyStrategy.put("path", new PathFileFilterFactory());
		propertyStrategy.put("content", new ContentFileFilterFactory());
		propertyStrategy.put("parent", new ParentFileFilterFactory());
		propertyStrategy.put("size", new SizeFileFilterFactory());
		propertyStrategy.put("creation_date", new CreationDateFileFilterFactory());
		propertyStrategy.put("last_update_date", new LastModificationFileFilterFactory());
		propertyStrategy.put("last_access_date", new LastAccessDateFileFilterFactory());
		propertyStrategy.put("owner", new OwnerFileFilterFactory());
		propertyStrategy.put("type", new TypeFileFilterFactory());
		propertyStrategy.put("status", new StatusFileFilterFactory());
	}

	public IOFileFilter getFileFilter(ZExpression expression) {
		try {
			String operator = expression.getOperator().toLowerCase();
			operator = operator.trim().replaceAll("[ ]?not[ ]?", "");
			
			List<ZConstant> constants = new ArrayList<ZConstant>();
			for (ZExp exp : (Vector<ZExp>) expression.getOperands()) {
				if (exp instanceof ZConstant) {
					
					if (((ZConstant) exp).getValue().matches("subQuery[0-9]")) {
						String subQueryString = subQueries.get(((ZConstant) exp).getValue());
						subQueryString = subQueryString.substring(2, subQueryString.length() - 1);
						constants.add(new ZConstant(subQueryString, ZConstant.STRING));
					} else {
						constants.add((ZConstant) exp);
					}
				}
			}

			if (constants.size() >= 2) {
				ComparatorOperator comparatorOperator = operatorStrategy.get(
						operator).newInstance();
				
				PropertyFileFilter propertyFileFilter =  propertyStrategy.get(
						constants.get(0).getValue().toLowerCase()).getPropertyFileFilter(constants.subList(1, constants.size()));
				
				propertyFileFilter.setOperator(comparatorOperator);
				
				return (IOFileFilter) propertyFileFilter;
			}			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
