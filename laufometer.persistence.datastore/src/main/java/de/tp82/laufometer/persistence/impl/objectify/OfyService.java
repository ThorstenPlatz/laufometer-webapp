package de.tp82.laufometer.persistence.impl.objectify;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import de.tp82.laufometer.persistence.impl.objectify.dbo.RunDBO;
import de.tp82.laufometer.persistence.impl.objectify.dbo.WatchdogDBO;

/**
 * @author Thorsten Platz
 */
public class OfyService {
	static {
		// register all Entities here
		factory().register(RunDBO.class);
		factory().register(WatchdogDBO.class);
	}

	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}

	public static ObjectifyFactory factory() {
		return ObjectifyService.factory();
	}

}
