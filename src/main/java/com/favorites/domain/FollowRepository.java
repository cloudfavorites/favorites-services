package com.favorites.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.favorites.domain.enums.FollowStatus;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	
	Long countByUserIdAndStatus(Long userId,FollowStatus status);
	
	Long countByFollowIdAndStatus(Long followId,FollowStatus status);	
	
	@Query("select f.followId from User u,Follow f where u.id=f.userId and f.status='follow' and u.id=:userId")
	List<Long> findMyFollowIdByUserId(@Param("userId") Long userId);

	@Query("select u.userName from Follow f ,User u  where f.userId=:userId and f.followId = u.id and f.status = 'follow'")
	List<String> findByUserId(@Param("userId") Long userId);
	
	@Query("select u.userName , u.introduction  ,u.profilePicture ,u.id  from Follow f ,User u where f.userId=:userId and f.followId = u.id and f.status = 'follow'")
	List<String> findFollowUserByUserId(@Param("userId") Long userId);
	
	@Query("select u.userName , u.introduction  ,u.profilePicture ,u.id   from Follow f,User u where f.followId=:followId and f.userId = u.id and f.status='follow'")
	List<String> findFollowedUserByFollowId(@Param("followId") Long followId);
	
}