package com.devsuperior.dslearn.services;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dslearn.dto.DeliverRevisionDTO;
import com.devsuperior.dslearn.entities.Deliver;
import com.devsuperior.dslearn.observers.DeliverRevisionObserver;
import com.devsuperior.dslearn.repositories.DeliverRepository;

@Service
public class DeliverService {

	@Autowired
	private DeliverRepository repository;
	
	private Set<DeliverRevisionObserver> deliverRevisionObserver = new LinkedHashSet<>();
	
	@Transactional
	public void saveRevision(Long id, DeliverRevisionDTO dto) {
		Deliver deliver = repository.getOne(id);
		deliver.setStatus(dto.getStatus());
		deliver.setFeedback(dto.getFeedback());
		deliver.setCorrectCount(dto.getCorrectCount());
		repository.save(deliver);
		for (DeliverRevisionObserver observer : deliverRevisionObserver) {
			observer.onDeliverRevision(deliver);
		}
	}
	
	public void subscribeDeliverRevisionObserver(DeliverRevisionObserver observer) {
		deliverRevisionObserver.add(observer);
	}
}
