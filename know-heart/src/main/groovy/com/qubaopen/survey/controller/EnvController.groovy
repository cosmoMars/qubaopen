package com.qubaopen.survey.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

@RestController
@RequestMapping('env')
@SessionAttributes('currentUser')
public class EnvController {
	
	@RequestMapping(value = 'getEnv', method = RequestMethod.POST)
	getEnv(){
		[
			'DYLD_LIBRARY_PATH' : System.getenv("LD_LIBRARY_PATH"),
			'os.name' : System.getProperty("os.name"),
			'os.arch' : System.getProperty("os.arch"),
			'os.version' : System.getProperty("os.version"),
			'PATH' : System.getenv("PATH"),
			'env' : System.getenv(),
			'BuilderJABootstrap' : System.mapLibraryName("BuilderJABootstrap"),
			'java.library.path' : System.getProperty("java.library.path")
		]
		
	}
}
