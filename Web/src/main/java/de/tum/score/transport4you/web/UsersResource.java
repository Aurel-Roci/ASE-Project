package de.tum.score.transport4you.web;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.util.List;
import javax.xml.bind.DatatypeConverter;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Key;



public class UsersResource extends ServerResource {

    @Get
    public String represent() {
		Key<Group> group = Key.create(Group.class, "default");
		List<User> users = ObjectifyService.ofy()
		.load()
		.type(User.class)
		.ancestor(group)
		.order("-date") 
		.list();

		String result = "<users>\n";
		for (User u : users) {
			result += "<user>\n";
			result += "\t<id>"		+ u.id 		+ "</id>\n";
			result += "\t<name>"	+ u.name 	+ "</name>\n";
			result += "\t<password>"	+ u.password 	+ "</password>\n";
			result += "\t<email>"	+ u.email 	+ "</email>\n";
			result += "\t<ticket>"	+ u.ticket	+ "</ticket>\n";
			result += "</user>\n";
		}
		result += "</users>";
        return result;
    }

}
