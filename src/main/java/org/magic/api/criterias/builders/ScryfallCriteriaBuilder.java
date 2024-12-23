package org.magic.api.criterias.builders;

import org.magic.api.criterias.AbstractQueryBuilder;
import org.magic.api.criterias.MTGCrit;

public class ScryfallCriteriaBuilder extends AbstractQueryBuilder<String> {


	@Override
	public String build(MTGCrit<?>... crits) {


		var temp = new StringBuilder();


		for(MTGCrit<?> c : crits)
		{
			if(c.getType()==Boolean.class)
			{
				if(c.getFirst().toString().equals("true"))
					temp.append("is:").append(c.getAtt());
				else
					temp.append("not:").append(c.getAtt());
			}
			else
			{
				temp.append(c.getAtt()).append(":").append(c.getFirst());
			}
			temp.append(" ");
		}
		
		return temp.toString().trim();
	}

}
