package com.minecraft.job.common.review.service;

import com.minecraft.job.common.review.domain.Review;

public interface ReviewService {

    Review create(Long userId, Long teamId, String content, Long score);

    void update(Long reviewId, Long userId, Long teamId, String content, Long score);

    void active(Long reviewId);

    void inactive(Long reviewId);
}
