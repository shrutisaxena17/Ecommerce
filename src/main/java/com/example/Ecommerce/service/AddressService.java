package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.Address;
import com.example.Ecommerce.repo.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@Service
public class AddressService {

    @Autowired
    private AddressRepo addressRepo;

    public List<Address> getAllAddresses() {
        return addressRepo.findAll();
    }

    public Address getAddressById(Long id) {
        return addressRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found for ID: " + id));
    }

    public void deleteAddressById(Long id) {
        if (addressRepo.existsById(id)) {
            addressRepo.deleteById(id);
        } else {
            throw new RuntimeException("Address not found for ID: " + id);
        }
    }


    public Address updateAddress(Long id, Address addressDetails) {
        Address addressToUpdate = addressRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found for ID: " + id));

        addressToUpdate.setStreet(addressDetails.getStreet());
        addressToUpdate.setCity(addressDetails.getCity());
        addressToUpdate.setState(addressDetails.getState());
        addressToUpdate.setCountry(addressDetails.getCountry());
        addressToUpdate.setPincode(addressDetails.getPincode());

        return addressRepo.save(addressToUpdate);
    }
}

