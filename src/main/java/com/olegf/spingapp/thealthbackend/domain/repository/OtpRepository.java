package com.olegf.spingapp.thealthbackend.domain.repository;

import com.olegf.spingapp.thealthbackend.domain.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    @Query("""
select o from otps o
where CAST(o.phone as text) = :phone
""")
    Optional<Otp> findByPhone(@Param("phone") String phone);

    @Query("""
SELECT COUNT(*) > 0
FROM otps
WHERE CAST(phone as text) = :phone
""")
    boolean existsByPhone(@Param("phone") String phone);

    @Query("""
delete o from otps o
where o.created_at CAST(:interval as interval)
where created_at < (now() - CAST(interval as interval))
""")
    void deleteExpired(@Param("interval") String interval);
}
