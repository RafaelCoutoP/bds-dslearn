package com.devsuperior.dslearn.services;

import java.time.Instant;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dslearn.dto.NotificationDTO;
import com.devsuperior.dslearn.entities.Deliver;
import com.devsuperior.dslearn.entities.Notification;
import com.devsuperior.dslearn.entities.User;
import com.devsuperior.dslearn.observers.DeliverRevisionObserver;
import com.devsuperior.dslearn.repositories.NotificationRepository;

@Service
public class NotificationService implements DeliverRevisionObserver {

	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private DeliverService deliverService;
	
	@PostConstruct //executa o metodo logo após o objeto ser iniciado.
	public void initialize() {
		deliverService.subscribeDeliverRevisionObserver(this);
	}
	
	@Transactional(readOnly = true)
	public Page<NotificationDTO> notificationsForCurrentUser(boolean unreadOnly ,Pageable pageable){
		User user = authService.authenticated();
		Page<Notification> page = notificationRepository.find(user, unreadOnly, pageable);
		return page.map(x -> new NotificationDTO(x));
	}
	
	@Transactional
	public void saveDeliverNotification(Deliver deliver) {
		
		Long sectionId = deliver.getLesson().getSection().getId();
		Long resourceId = deliver.getLesson().getSection().getResource().getId();
		Long offerId = deliver.getLesson().getSection().getResource().getOffer().getId();
		
		String route = "/offers/" + offerId + "/resources/" + resourceId + "/sections/" + sectionId;
		String text = deliver.getFeedback();
		Instant moment = Instant.now();
		User user = deliver.getEnrollment().getStudent();
		
		Notification notification = new Notification(null, text, moment, false, route, user);
		notificationRepository.save(notification);
	}

	@Override
	public void onDeliverRevision(Deliver deliver) {
		saveDeliverNotification(deliver);
	}
}
