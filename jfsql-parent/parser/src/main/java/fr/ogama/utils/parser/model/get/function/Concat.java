package fr.ogama.utils.parser.model.get.function;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fr.ogama.utils.parser.JFSQLExecutionException;
import fr.ogama.utils.parser.model.Utils;
import fr.ogama.utils.parser.model.get.Expression;

public class Concat extends AbstractFunction {

	public Concat(String operator) {
		super(operator);
	}

	@Override
	public List<Comparable> execute(Map<String, Comparable> params)
			throws JFSQLExecutionException {
		if (nbOperands() < 2) {
			throw new IllegalArgumentException(
					"Concat : Expected at least 2 arguments but found "
							+ nbOperands());
		}

		StringBuilder concat = new StringBuilder();

		for (Expression param : getOperands()) {
			if (param != null) {
				List<Comparable> results = param.execute(params);

				if (results.size() == 1) {
					concat.append(Utils.toString(results.get(0)));
				} else {
					concat.append(Utils.toString(results));
				}
			}
		}

		return Arrays.asList((Comparable) concat.toString());
	}
}