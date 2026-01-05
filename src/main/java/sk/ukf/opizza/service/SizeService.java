package sk.ukf.opizza.service;

import sk.ukf.opizza.entity.Size;
import java.util.List;

public interface SizeService {
    List<Size> getAllSizes();
    Size getSizeById(int id);
    void saveSize(Size size);
    void deleteSize(int id);
}