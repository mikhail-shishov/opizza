package sk.ukf.opizza.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.ukf.opizza.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {

}