package com.example.Ecommerce.repo;

import com.example.Ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address,Long> {
    Address findByUser_Id(Long userId);
}
