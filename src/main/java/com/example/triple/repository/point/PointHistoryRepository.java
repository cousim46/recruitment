package com.example.triple.repository.point;

import com.example.triple.entity.point.PointHistory;
import com.example.triple.entity.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PointHistoryRepository extends JpaRepository<PointHistory, UUID> {

    Slice<PointHistory> findAllByUser(User user, Pageable pageable);

}
