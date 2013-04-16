package de.tp82.laufometer.model.run;

import java.util.Date;

/**
 * @author Thorsten Platz
 */
public interface Run extends RunTickProvider {
	Date getBegin();

	Date getEnd();

	double getDistance();

	double getDuration();

	double getAverageSpeed();
}
