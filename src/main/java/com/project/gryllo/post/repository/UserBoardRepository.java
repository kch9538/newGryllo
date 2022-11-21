package com.project.gryllo.post.repository;

import com.project.gryllo.post.entity.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserBoardRepository extends JpaRepository<Image, Integer> {
	// 특정유저 게시물 조회
	@Query(value = "select * from image where userId = ?1", nativeQuery = true)
	List<Image> mUserBoard(int BoardUserid);
}
