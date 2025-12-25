package sk.ukf.opizza.service;

import sk.ukf.opizza.entity.Address;
import sk.ukf.opizza.entity.User;
import java.util.List;

public interface AddressService {
    List<Address> getAddressesByUser(User user);
    Address getAddressById(int id);
    void saveAddress(Address address, User user);
    void deleteAddress(int id);
}