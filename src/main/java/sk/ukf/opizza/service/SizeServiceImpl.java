package sk.ukf.opizza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.dao.SizeRepository;
import sk.ukf.opizza.entity.Size;
import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAllByOrderByWeightGramsAsc();
    }

    @Override
    public Size getSizeById(int id) {
        return sizeRepository.findById(id).orElse(null);
    }

    @Override
    public void saveSize(Size size) {
        sizeRepository.save(size);
    }

    @Override
    public void deleteSize(int id) {
        sizeRepository.deleteById(id);
    }
}