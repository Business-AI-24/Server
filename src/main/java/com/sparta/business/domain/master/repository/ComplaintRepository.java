package com.sparta.business.domain.master.repository;

import com.sparta.business.entity.Complaint;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, UUID> {

}
