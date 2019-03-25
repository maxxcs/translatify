package com.translatify.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.translatify.models.Branch;
import com.translatify.models.User;
import com.translatify.repository.BranchRepository;
import com.translatify.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userDAO;
	
	@Autowired
	private BranchRepository branchDAO;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(@RequestParam(required = false, defaultValue = "", value="search") String search) {
		try {
			ModelAndView mv = new ModelAndView("index");
			User auth = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			if (search.equals("")) {
				Iterable<Branch> branches = branchDAO.findAll();
				mv.addObject("branches", branches);
			} else {
				Iterable<Branch> branchesSearch = branchDAO.findAllBySearch(search);
				mv.addObject("branches", branchesSearch);
			}
			mv.addObject("search", search);
			if (auth == null) {
				return mv;
			} else {
				mv.addObject("auth", auth);
				return mv;
			}
		} catch (Exception e) {
			return new ModelAndView("redirect:/");
		}

	}
}
