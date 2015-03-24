package com.knowheart3.controller.discovery
import com.knowheart3.repository.discovery.DailyDiscoveryPicRepository
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.topic.DailyDiscoveryPic
import com.qubaopen.survey.entity.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping('discoveryPic')
@SessionAttributes('currentUser')
public class DiscoveryPicController extends AbstractBaseController<DailyDiscoveryPic, Long> {

	@Autowired
	DailyDiscoveryPicRepository dailyDiscoveryPicRepository
	

	
	@Override
	MyRepository<DailyDiscoveryPic, Long> getRepository() {
		dailyDiscoveryPicRepository
	}
	
	/**
	 * @param id
	 * @param time
	 * @param user
	 * @return
	 * 获取明日发现图片
	 */
	@RequestMapping(value = 'retrieveDiscoveryPic', method = RequestMethod.GET)
	retrieveDiscoveryContent(@PageableDefault(size = 1, page =  0) Pageable pageable,
		@ModelAttribute('currentUser') User user) {

		def dailyDiscoveryPic=dailyDiscoveryPicRepository.findTodayPic(new Date(),pageable);
		
		if(dailyDiscoveryPic.size()>=1){
			[
				'success' : '1',
				'picId' : dailyDiscoveryPic[0]?.id,
				'picName' : dailyDiscoveryPic[0]?.name,
				'picUrl' : dailyDiscoveryPic[0]?.picUrl
			]
		}
	}
		
}
