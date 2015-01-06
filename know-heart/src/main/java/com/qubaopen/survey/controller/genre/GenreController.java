package com.qubaopen.survey.controller.genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Genre;
import com.qubaopen.survey.repository.genre.GenreRepository;

@RestController
@RequestMapping("genre")
@SessionAttributes("currentUser")
public class GenreController extends AbstractBaseController<Genre, Long> {

	private static Logger logger = LoggerFactory.getLogger(GenreController.class);

	@Autowired
	private GenreRepository genreRepository;

	@Override
	protected MyRepository<Genre, Long> getRepository() {
		return genreRepository;
	}

	/**
	 * @return 获取流派
	 */
	@RequestMapping(value = "retrieveGenre", method = RequestMethod.GET)
	Map<String, Object> retrieveGenre() {

		logger.trace("-- 获取流派 --");

		Map<String, Object> map = new HashMap<String, Object>();
		List<Genre> genres = genreRepository.findAll();

		map.put("success", "1");
		map.put("genres", genres);

		return map;
	}

}
