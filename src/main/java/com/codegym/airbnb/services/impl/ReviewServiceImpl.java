package com.codegym.airbnb.services.impl;

import com.codegym.airbnb.model.Review;
import com.codegym.airbnb.repositories.ReviewRepository;
import com.codegym.airbnb.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Iterable<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Iterable<Review> findByRoomIdQuery(Long id) {
        return reviewRepository.findByRoomIdQuery(id);
    }
}
