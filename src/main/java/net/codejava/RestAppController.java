package net.codejava;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
class RestAppController {

	private final NewsRepository repository;

	RestAppController(NewsRepository repository) {
		this.repository = repository;
	}

	// Aggregate root
	// tag::get-aggregate-root[]
	@GetMapping("/news")
	List<News> all() {
		return repository.findAll();
	}
	// end::get-aggregate-root[]


	@PostMapping("/news")
	News newNews(@RequestBody News newNews) {
		return repository.save(newNews);
	}



	@PutMapping("/news/{id}")
	News replaceNews(@RequestBody News newNews, @PathVariable Long id) {

		return repository.findById(id)
				.map(news -> {
					news.setCdc(newNews.getCdc());
					news.setViewcount(newNews.getViewcount());
					news.setState(newNews.getState());
					news.setDate(newNews.getDate());
					news.setArticle(newNews.getArticle());
					news.setContents(newNews.getContents());
					return repository.save(news);
				})
				.orElseGet(() -> {
					newNews.setId(id);
					return repository.save(newNews);
				});
	}


	@DeleteMapping("/news/{id}")
	void deleteNews(@PathVariable Long id) {
		repository.deleteById(id);
	}

	@Autowired
	private UserServices userService;

	@GetMapping("/users")
	public List<User> listUsers(Model model) {
		List<User> listUsers = userService.listAll();

		return listUsers;
	}

	@PostMapping("/users")
	public String processRegister(Model model, @RequestBody User user, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {

		model.addAttribute("user", new User());

		userService.registerApi(user, getSiteURL(request));
		return "register_success";
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	@GetMapping("/verifyUser")
	public String verifyUser(@Param("code") String code) {
		if (userService.verify(code)) {
			return "verify_success";
		} else {
			return "verify_fail";
		}
	}
}

