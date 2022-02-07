package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServices {

	@Autowired
	private NewsRepository repo;
	
	public List<News> listAll() {
		return repo.findAll();
	}
	
}
