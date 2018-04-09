package org.panda.frexttobiopax;

import org.biopax.paxtools.model.BioPAXFactory;
import org.biopax.paxtools.model.BioPAXLevel;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.model.level3.Conversion;
import org.biopax.paxtools.model.level3.ConversionDirectionType;
import org.biopax.paxtools.model.level3.PhysicalEntity;

import java.util.*;

/**
 * Created by babur on 1/21/2016.
 */
public class ConversionRegistry
{
	Map<Code, Conversion> registry;
	BioPAXFactory factory = BioPAXLevel.L3.getDefaultFactory();
	Model model;

	public ConversionRegistry()
	{
		registry = new HashMap<>();
	}

	public void setModel(Model model)
	{
		this.model = model;
	}

	/**
	 * Checks if it is ok to generate a conversion for this interaction.
	 * @return true if can generate conversion, false if a similar one already registered before
	 */
	public Conversion getConversion(Set<PhysicalEntity> in, Set<PhysicalEntity> out, Class<? extends Conversion> clazz)
	{
		Code code = new Code(in, out);

		if (registry.containsKey(code)) return registry.get(code);
		else
		{
			Conversion cnv = factory.create(clazz, "Conversion/" + NextNumber.get());
			model.add(cnv);
			in.forEach(cnv::addLeft);
			out.forEach(cnv::addRight);
			cnv.setConversionDirection(ConversionDirectionType.LEFT_TO_RIGHT);

			registry.put(code, cnv);
			return cnv;
		}
	}
	class Code
	{
		Set<PhysicalEntity> in;
		Set<PhysicalEntity> out;

		public Code(Set<PhysicalEntity> in, Set<PhysicalEntity> out)
		{
			this.in = in;
			this.out = out;
		}

		public int hashCode()
		{
			int h = 0;
			for (PhysicalEntity pe : in)
			{
				h += pe.hashCode();
			}
			for (PhysicalEntity pe : out)
			{
				h += pe.hashCode();
			}
			return h;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof Code)
			{
				Code c = (Code) obj;
				return in.equals(c.in) && out.equals(c.out);
			}
			else return this == obj;
		}
	}
}
