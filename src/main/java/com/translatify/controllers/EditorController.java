package com.translatify.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.translatify.models.Branch;
import com.translatify.models.Document;
import com.translatify.models.User;
import com.translatify.repository.BranchRepository;
import com.translatify.repository.UserRepository;

@Controller
public class EditorController {

	@Autowired
	private UserRepository userDAO;
	
	@Autowired
	private BranchRepository branchDAO;
	
	@RequestMapping(value = "/editor/{id}", method = RequestMethod.GET)
	public ModelAndView editor(@PathVariable("id") long branchId) {
		try {
			User auth = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			if (auth == null) {
				return new ModelAndView("redirect:/login");
			} else {
				ModelAndView mv = new ModelAndView("editor");
				Branch branch = branchDAO.findById(branchId);
				Document doc = branch.getDocument();
				mv.addObject("auth", auth);
				mv.addObject("doc", doc);
				mv.addObject("branch", branch);
				return mv;
			}
		} catch (Exception e) {
			return new ModelAndView("redirect:/");
		}
	}
	
	@RequestMapping(value = "/editor/{branchId}", method = RequestMethod.POST)
	public ModelAndView editorSubmit(@PathVariable("branchId") long branchId, Branch branch) {
		try {
			User auth = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			if (auth == null) {
				return new ModelAndView("redirect:/login");
			} else {
				Branch base = branchDAO.findById(branchId);	
				branch.setUser(auth);
				branch.setDocument(base.getDocument());
				branch.setBase(base);
				branch.setDate(new Date());
				Branch branchSubmitted = branchDAO.save(branch);
				return new ModelAndView("redirect:/document/" + branchSubmitted.getId());
			}	
		} catch (Exception e) {
			return new ModelAndView("redirect:/");
		}
	}
}
