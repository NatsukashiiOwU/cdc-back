package net.codejava;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsRepository extends JpaRepository<News, Long> {
	@Query("SELECT u FROM News u WHERE u.article = ?1")
	public News findByArticle(String article);
}
