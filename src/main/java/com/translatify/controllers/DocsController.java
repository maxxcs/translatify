package com.translatify.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.translatify.models.Branch;
import com.translatify.models.Comment;
import com.translatify.models.Document;
import com.translatify.models.User;
import com.translatify.repository.BranchRepository;
import com.translatify.repository.CommentRepository;
import com.translatify.repository.DocumentRepository;
import com.translatify.repository.UserRepository;
import com.translatify.util.PDFHandler;

@Controller
public class DocsController {

	@Autowired
	private UserRepository userDAO;
	
	@Autowired
	private DocumentRepository documentDAO;
	
	@Autowired
	private BranchRepository branchDAO;
	
	@Autowired
	private CommentRepository CommentDAO;
	
	@Autowired
	private PDFHandler pdf;
	
	@RequestMapping(value = "/document/{branchId}", method = RequestMethod.GET)
	public ModelAndView document(@PathVariable("branchId") long branchId) {
		try {
			User auth = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			ModelAndView mv = new ModelAndView("document");
			Branch branch = branchDAO.findById(branchId);
			Document doc = branch.getDocument();
			Branch raw = branchDAO.findRaw(doc);
			Iterable<Branch> branches = branchDAO.findAllByDoc(doc);
			Iterable<Comment> comments = CommentDAO.findAllByBranch(branch);
			mv.addObject("branches", branches);
			mv.addObject("doc", doc);
			mv.addObject("branch", branch);
			mv.addObject("raw", raw);
			mv.addObject("comments", comments);
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
	
	@RequestMapping(value = "/document/{branchId}", method = RequestMethod.POST)
	public ModelAndView documentComment(Comment comment, @PathVariable("branchId") long branchId) {
		try {
			User auth = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			if (auth == null) {
				return new ModelAndView("redirect:/document/" + branchId);
			} else {
				Branch branch = branchDAO.findById(branchId);
				comment.setBranch(branch);
				comment.setUser(auth);
				comment.setDate(new Date());
				CommentDAO.save(comment);
				return new ModelAndView("redirect:/document/" + branchId);
			}
		} catch (Exception e) {
			return new ModelAndView("redirect:/document/" + branchId);
		}
	}
	
	@RequestMapping(value = "/document/{branchId}/pdf", method = RequestMethod.GET)
	public ResponseEntity<byte[]> documentDownload(@PathVariable("branchId") long branchId) {
		try {
				Branch branch = branchDAO.findById(branchId);
				String pdfPath = pdf.createPDF(branch.getContent(), String.valueOf(branch.getId()));
				Path path = Paths.get(pdfPath);
				byte[] contents = Files.readAllBytes(path);
			    HttpHeaders headers = new HttpHeaders();
			    headers.setContentType(MediaType.parseMediaType("application/pdf"));
			    String filename = "content.pdf";
			    headers.add("content-disposition", "inline;filename=" + filename);
			    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
			    return response;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(null, null, HttpStatus.BAD_REQUEST);
			return response;
		}
	}
	
	@RequestMapping(value = "/new-document", method = RequestMethod.GET) 
	public ModelAndView newDocument() {
		try {
			User auth = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			if (auth == null) {
				return new ModelAndView("redirect:/login");
			} else {
				ModelAndView mv = new ModelAndView("new_doc");
				mv.addObject("auth", auth);
				return mv;
			}
		} catch (Exception e) {
			return new ModelAndView("redirect:/");
		}
	}
	
	@RequestMapping(value = "/new-document", method = RequestMethod.POST)
	public ModelAndView newDocumentSubmit(Document doc) {
		try {
			User auth = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			if (auth == null) {
				return new ModelAndView("redirect:/login");
			} else {
				doc.setUser(auth);
				doc.setDate(new Date());
				Document docSubmitted = documentDAO.save(doc);
				Branch branch = new Branch(docSubmitted, auth, null, docSubmitted.getTitle(), "RAW", docSubmitted.getLanguage(), doc.getContent(), docSubmitted.getDate());
				Branch branchSubmitted = branchDAO.save(branch);
				return new ModelAndView("redirect:/document/" + branchSubmitted.getId());
			}		
		} catch (Exception e) {
			return new ModelAndView("redirect:/");
		}
	}
}
