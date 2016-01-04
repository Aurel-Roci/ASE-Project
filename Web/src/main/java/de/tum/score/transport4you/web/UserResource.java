package de.tum.score.transport4you.web;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.util.List;
import javax.xml.bind.DatatypeConverter;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Key;



public class UserResource extends ServerResource {

    @Get
    public String represent() throws Exception {
		User u = ObjectifyService.ofy().load().type(User.class).filter("email",getAttribute("email")).first().now();

		if(!getAttribute("password").equals(u.password) ){
			return "<error>Wrong Password!</error>";
		}

		if(u == null){
			System.err.println("User with email '"+getAttribute("email")+"' is null!");
			return "";
		}

		String result = "";	
		result += "<user>\n";
		result += "\t<id>"		+ u.id 		+ "</id>\n";
		result += "\t<name>"	+ u.name 	+ "</name>\n";
		result += "\t<email>"	+ u.email 	+ "</email>\n";
		result += "\t<ticket>"	+ u.ticket	+ "</ticket>\n";
		result += "</user>\n";
        return result;
		
    }

}
