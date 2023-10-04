package com.mentoring.TravelInsurance.repository;


import com.mentoring.TravelInsurance.model.ERole;
import com.mentoring.TravelInsurance.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
