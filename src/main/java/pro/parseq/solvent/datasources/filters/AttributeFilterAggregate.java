/*******************************************************************************
 *     Copyright 2016-2017 the original author or authors.
 *
 *     This file is part of CONC.
 *
 *     CONC. is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     CONC. is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with CONC. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package pro.parseq.solvent.datasources.filters;

import java.io.Serializable;
import java.util.function.Predicate;

import pro.parseq.solvent.utils.PropertiesAware;

public class AttributeFilterAggregate implements Serializable {

	private static final long serialVersionUID = 7643423193776845082L;
	
	private final long id;
	private final Predicate<PropertiesAware> predicate;

	public AttributeFilterAggregate(long id, Predicate<PropertiesAware> predicate) {

		this.id = id;
		this.predicate = predicate;
	}

	public long getId() {
		return id;
	}

	public Predicate<PropertiesAware> getPredicate() {
		return predicate;
	}
}
