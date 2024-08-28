package se.lexicon.dao;

import se.lexicon.model.City;

import java.util.List;

public interface CityDao {
    City findById(int id);
    List<City> findByCode(String code);
    List<City> findByName(String name);
    List<City> findAll();
    City add(City city);
    City update(City city);
    int delete(City city);
}
