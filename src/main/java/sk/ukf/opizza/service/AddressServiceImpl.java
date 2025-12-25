package sk.ukf.opizza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.dao.AddressRepository;
import sk.ukf.opizza.entity.Address;
import sk.ukf.opizza.entity.User;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> getAddressesByUser(User user) {
        return addressRepository.findByUserId(user.getId());
    }

    @Override
    public Address getAddressById(int id) {
        return addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Adresa nebola nájdená"));
    }

    @Override
    public void saveAddress(Address address, User user) {
        address.setUser(user);
        addressRepository.save(address);
    }

    @Override
    public void deleteAddress(int id) {
        addressRepository.deleteById(id);
    }
}