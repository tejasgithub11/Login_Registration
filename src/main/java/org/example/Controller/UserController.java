package org.example.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.Model.User;
import org.example.Service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

	@Autowired
	userservice userService;

	@GetMapping("/")
	public String home(HttpServletRequest request, Model model) {
		String error = request.getParameter("message");
		if (error != null) {
			model.addAttribute("errorMessage", error);
		}
		return "index";
	}

	@GetMapping("/registrationts")
	public String registration() {
		return "userRegistration";
	}

	@PostMapping("/register")
	public String processRegistrationForm(@RequestParam("name") String name, @RequestParam("email") String email,
										  @RequestParam("password") String password,
										  @RequestParam("mobileno") String mobileNo, Model model) {
		if (userService.existsByEmail(email)) {
			model.addAttribute("emailError", "Email is already registered.");
			return "registrationts";
		}
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setMobile_no(mobileNo);
		user.setAdmin_id(0);
		userService.saveUser(user);
		return "redirect:/";
	}

	@PostMapping("/login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password,
						Model model, HttpSession session) {
		User user = userService.findByemail(email);
		if (user != null && userService.verifyPassword(password, user.getPassword())) {
			session.setAttribute("loggedInUser", user);
			model.addAttribute("message", "Login successful!");
			return "loginSuccess";  // Return loginSuccess.html template
		} else {
			model.addAttribute("error", "Invalid username or password");
			String error = "Invalid Credential";
			return "redirect:/?message=" + error;
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("loggedInUser");
		return "redirect:/";
	}
}
