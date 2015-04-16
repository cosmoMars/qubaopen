package com.qubaopen.backend.controller.genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.genre.GenreRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Genre;

@RestController
@RequestMapping("genre")
public class GenreController extends AbstractBaseController<Genre, Long> {

	private static Logger logger = LoggerFactory
			.getLogger(GenreController.class);

	@Autowired
	private GenreRepository genreRepository;

	@Override
	protected MyRepository<Genre, Long> getRepository() {
		return genreRepository;
	}

	/**
	 * @return 获取流派列表
	 */
	@RequestMapping(value = "retrieveGenre", method = RequestMethod.GET)
	Map<String, Object> retrieveGenre() {

		logger.trace("-- 获取流派 --");

		Map<String, Object> map = new HashMap<String, Object>();
		List<Genre> genres = genreRepository.findAll();

		map.put("success", "1");
		map.put("list", genres);

		return map;
	}

	/**
	 * @param id
	 * @param name
	 * @return 新增/修改 流派
	 */
	@RequestMapping(value = "generateGenre", method = RequestMethod.POST)
	private Object generateGenre(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) String name) {

		Genre genre = null;

		if (id != null) {
			genre = genreRepository.findOne(id);
		} else {
			genre = new Genre();
		}

		genre.setName(name);

		genreRepository.save(genre);

		return "{\"success\" : \"1\",  \"genreId\" : " + genre.getId() + "}";
	}

}
