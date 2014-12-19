//package com.qubaopen.survey.controller.payment;
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.web.bind.annotation.ModelAttribute
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RequestMethod
//import org.springframework.web.bind.annotation.RequestParam
//
//import com.qubaopen.core.controller.AbstractBaseController
//import com.qubaopen.core.repository.MyRepository
//import com.qubaopen.survey.entity.payment.OrderRequest
//import com.qubaopen.survey.entity.payment.PayEntity
//import com.qubaopen.survey.entity.payment.PaymentMode
//import com.qubaopen.survey.entity.user.User
//import com.qubaopen.survey.repository.booking.BookingRepository
//import com.qubaopen.survey.repository.payment.OrderRequestRepository
//import com.qubaopen.survey.repository.payment.PayEntityRepository
//import com.qubaopen.survey.repository.payment.PayTypeRepository
//
////@RestController
////@RequestMapping('orderResources')
//public class OrderResources extends AbstractBaseController<OrderRequest, Long> {
//
//	@Autowired
//	OrderRequestRepository orderRequestRepository
//	
//	@Autowired
//	PayTypeRepository payTypeRepository
//	
//	@Autowired
//	PayEntityRepository payEntityRepository
//	
//	@Autowired
//	BookingRepository bookingRepository
//	
//	@Override
//	MyRepository<OrderRequest, Long> getRepository() {
//		orderRequestRepository
//	}
//	
//	@RequestMapping(value = 'pay', method = RequestMethod.POST)
//	pay(@RequestParam(required = false) Long bookingId, @ModelAttribute('currentUser') User user) {
//		
//		def booking = bookingRepository.findOne(bookingId)
//		
//		def payEntity = new PayEntity(
//			booking : booking,
//			payAmount : booking.money,
//			user : user,
//			payTime : new Date()
//		)
//		payEntityRepository.save(payEntity)
//		
//		def payTypes = payTypeRepository.findAll()
//		
//		payTypes.each {
//			PaymentMode mode = PaymentMode.parse(it.id)
//			
//			
//		}
//		
//		
//		payTypes << [
//			
//		]
//		
//	}
//
//}
