package com.favorites.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.favorites.domain.enums.FollowStatus;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	
	Long countByUserIdAndStatus(Long userId,FollowStatus status);
	
	Long countByFollowIdAndStatus(Long followId,FollowStatus status);	

}