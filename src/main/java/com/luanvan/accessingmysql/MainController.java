package com.luanvan.accessingmysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.CharacterEncodingFilter;

@RestController // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
	@Autowired // This means to get the bean called userRepository
	// Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;
	@PostMapping(path="/add") //Map ONLY POST Requests
	public @ResponseBody String addNewUser(@RequestBody String jsonString){
		// @ResponseBody means the returned String is the response, not a view name
    		// @RequestBody means it's a String or Json from GET or POST request
		// @RequestParam means it is a parameter from the GET or POST request
		try{
			JSONObject json = new JSONObject(jsonString);
			User n = new User();
			n.setName(json.getString("name"));
			n.setEmail(json.getString("email"));
			userRepository.save(n);
			System.out.println("JSON: "+json.toString());
		}catch(JSONException e){
			System.out.println("JSON Error: "+e);
		}
		return "Saved";
	}

	@PostMapping(path="/delete")
	public @ResponseBody String deleteUser(@RequestBody String id){
		int ID = Integer.parseInt(id);
		userRepository.deleteById(ID);
		return id+" was deleted";
	}

	@GetMapping(path="/info/{searchkey}")
	public @ResponseBody String getUserInfo(@PathVariable("searchkey") String searchkey){
		Iterable<User> users = userRepository.findAll();
		int ID = Integer.parseInt(searchkey);
                for(User user: users){
                        if(user.getId()==ID){
				JSONObject jsonObject = new JSONObject();
                		jsonObject.put("id", user.getId());
                		jsonObject.put("name", user.getName());
				jsonObject.put("email", user.getEmail());
				return jsonObject.toString();
			}
                }
		return null;
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers(){
		Iterable<User> users = userRepository.findAll();
		for(User user: users){
			System.out.println("Name: "+user.getName());
		}
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}

	CharacterEncodingFilter characterEncodingFilter() {
    	CharacterEncodingFilter filter = new CharacterEncodingFilter();
    	filter.setEncoding("UTF-8");
    	filter.setForceEncoding(true);
    	return filter;
	}
}
