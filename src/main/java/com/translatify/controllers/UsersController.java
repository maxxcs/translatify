package com.translatify.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.translatify.models.Branch;
import com.translatify.models.Comment;
import com.translatify.models.User;
import com.translatify.repository.BranchRepository;
import com.translatify.repository.CommentRepository;
import com.translatify.repository.UserRepository;

@Controller
public class UsersController {
	
	@Autowired
	private UserRepository userDAO;
	
	@Autowired
	private BranchRepository branchDAO;
	
	@Autowired
	private CommentRepository commentDAO;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		User auth = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if (auth == null) {
			return new ModelAndView("login");
		} else {
			return new ModelAndView("redirect:/");
		}
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		User auth = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if (auth == null) {
			return new ModelAndView("register");
		} else {
			return new ModelAndView("redirect:/");
		}
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView registerSubmit(User user) {
		try {
			User auth = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			if (auth == null) {
				System.out.println(user.getEmail());
				System.out.println(user.getUsername());
				System.out.println(user.getPassword());
				user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
				userDAO.save(user);
				return new ModelAndView("redirect:/login");	
			} else {
				return new ModelAndView("redirect:/");
			}
		} catch (Exception e) {
			ModelAndView mv = new ModelAndView("register");
			mv.addObject("errors", e);
			return mv;
		}
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ModelAndView user(@PathVariable("id") long userId) {
		try {
			User auth = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			if (auth == null) {
				return new ModelAndView("redirect:/login");
			} else {
				User user = userDAO.findById(userId);
				if (user == null) {
					return new ModelAndView("redirect:/");
				} else {
					ModelAndView mv = new ModelAndView("user");
					Iterable<Branch> branches = branchDAO.findAllByUser(user);
					Iterable<Comment> comments = commentDAO.findAllByUser(user);
					mv.addObject("auth", auth);
					mv.addObject("user", user);
					mv.addObject("branches", branches);
					mv.addObject("comments", comments);
					return mv;
				}
			}
		} catch (Exception e) {
			return new ModelAndView("redirect:/");
		}
	}
}
