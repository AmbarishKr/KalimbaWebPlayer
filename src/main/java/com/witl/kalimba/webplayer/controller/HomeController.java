package com.witl.kalimba.webplayer.controller;

/*@auther : Ambarish Kumar
 * 
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.witl.kalimba.webplayer.common.User;
import com.witl.kalimba.webplayer.common.UserJDBCTemplate;

@Controller
public class HomeController {

	@Autowired
	private UserJDBCTemplate userDAO;
	ModelAndView modelAndView;

	// handler methods go here...
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public ModelAndView saveUser(HttpServletRequest request, HttpSession session) {
		User user = new User();
		if (request.getParameter("name") != null) {
			user.setName(request.getParameter("name"));
			session.setAttribute("sessionName", request.getParameter("name"));
		}
		if (request.getParameter("email") != null) {
			user.setEmail(request.getParameter("email"));
		}
		if (request.getParameter("first_name") != null) {
			user.setFirst_Name(request.getParameter("first_name"));
		}
		if (request.getParameter("last_name") != null) {
			user.setLast_Name(request.getParameter("last_name"));
		}
		if (request.getParameter("gender") != null) {
			user.setGender(request.getParameter("gender"));
		}
		if (request.getParameter("birthday") != null) {
			user.setBirthday(request.getParameter("birthday"));
		}
		if (request.getParameter("location") != null) {
			user.setLocation(request.getParameter("location"));
		}

		System.out.println("location" + user.getLocation());
		if (request.getParameter("hometown") != null) {
			user.setHometown(request.getParameter("hometown"));
		}
		System.out.println("hometown" + user.getHometown());
		if (request.getParameter("relationship") != null) {
			user.setRelationship(request.getParameter("relationship"));
		}
		if (request.getParameter("timezone") != null) {
			user.setTimezone(request.getParameter("timezone"));
		}
		if (request.getParameter("providerId") != null) {
			String provider = request.getParameter("providerId");
			user.setProvider(provider);
			if (!provider.equals("")) {
				if (provider.equals("1")) {
					user.setProvider("FB");
				} else {
					user.setProvider("GMAIL");
				}
				user.setProvider_id(Integer.parseInt(provider));
			}
		}
		if (request.getParameter("timezone") != null) {
			user.setTimezone(request.getParameter("timezone"));
		}
		if (request.getParameter("email") != null) {
			session.setAttribute("sessionEmail", request.getParameter("email"));
			System.out.println("session email"
					+ session.getAttribute("sessionEmail"));
		}
		if (request.getParameter("userType") != null) {
			session.setAttribute("sessionUserType",
					request.getParameter("userType"));
		}
		if (session.getAttribute("sessionUserType") != null) {
			Object userType = session.getAttribute("sessionUserType");
			user.setUserType(userType.toString());
			System.out.println("session userType" + userType);
		}
		userDAO.create(user);
		
			modelAndView = new ModelAndView("redirect:/");
		
		// modelAndView.addObject("returnfrom", "modelandview");
		return modelAndView;
	}

}