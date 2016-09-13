package com.favorites.domain;


import org.springframework.data.jpa.repository.JpaRepository;

import com.favorites.domain.enums.IsDelete;


public interface CollectRepository extends JpaRepository<Collect, Long> {
	
	Long countByUserIdAndIsDelete(Long userId,IsDelete isDelete);
}