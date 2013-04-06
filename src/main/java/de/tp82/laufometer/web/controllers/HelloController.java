package de.tp82.laufometer.web.controllers;

import de.tp82.laufometer.web.model.Run;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;

@Controller  
public class HelloController {


  
    @RequestMapping(value = "/hello", method = RequestMethod.GET)  
    public String helloGet(ModelMap map) {  
        // this is your model (in future post, I will show how to use the ModelAttribute and command objects)  
        map.put("name", "Paulchen");
        // for now, this is where your "View" is  
        return "hello";  
    }

	@RequestMapping(value = "/runs", method = RequestMethod.GET)
	public String runsGet(ModelMap map) {
		List<Run> runs = Collections.emptyList();
		map.put("runs", runs);
		return "allRuns";
	}
}  