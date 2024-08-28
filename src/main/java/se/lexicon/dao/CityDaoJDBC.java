package se.lexicon.dao;

import se.lexicon.dbConnection.MySQLConnection;
import se.lexicon.model.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CityDaoJDBC implements CityDao{
    @Override
    public City findById(int id) {
        City city = null;
        try (
                Connection connection = MySQLConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement("select * from city where id = ?")
        ) {
            statement.setInt(1, id);
            try (
                    ResultSet resultSet = statement.executeQuery()
            ) {
                if (resultSet.next()) {
                    int cityId = resultSet.getInt(1);
                    String cityName = resultSet.getString(2);
                    String countryCode = resultSet.getString(3);
                    String district = resultSet.getString(4);
                    int population = resultSet.getInt(5);

                    city = new City(cityId, cityName, countryCode, district, population);
                } else {
                    throw new IllegalArgumentException("City not found... Enter a valid City id...");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: ");
            e.printStackTrace();
        }
        return city;
    }

    @Override
    public List<City> findByCode(String code) {
        List<City> cityList = new ArrayList<>();
        try (
                Connection connection = MySQLConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement("select * from city where countrycode = ?")
        ) {
            statement.setString(1, code);
            try (
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    int cityId = resultSet.getInt(1);
                    String cityName = resultSet.getString(2);
                    String countryCode = resultSet.getString(3);
                    String district = resultSet.getString(4);
                    int population = resultSet.getInt(5);

                    City city = new City(cityId, cityName, countryCode, district, population);
                    cityList.add(city);
                }
                cityList.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: ");
            e.printStackTrace();
        }
        return cityList;
    }

    @Override
    public List<City> findByName(String name) {
        List<City> cityList = new ArrayList<>();
        try (
                Connection connection = MySQLConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement("select * from city where name like ?")
        ) {
            statement.setString(1, "%"+name+"%");
            try (
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while(resultSet.next()) {
                    int cityId = resultSet.getInt(1);
                    String cityName = resultSet.getString(2);
                    String countryCode = resultSet.getString(3);
                    String district = resultSet.getString(4);
                    int population = resultSet.getInt(5);

                    City city = new City(cityId, cityName, countryCode, district, population);
                    cityList.add(city);
                }
                cityList.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: ");
            e.printStackTrace();
        }
        return cityList;
    }

    @Override
    public List<City> findAll() {
        List<City> cityList = new ArrayList<>();
        try {
            Connection connection = MySQLConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from city");
            while (resultSet.next()) {
                int cityId = resultSet.getInt(1);
                String cityName = resultSet.getString(2);
                String countryCode = resultSet.getString(3);
                String district = resultSet.getString(4);
                int population = resultSet.getInt(5);

                City city = new City(cityId, cityName, countryCode, district, population);
                cityList.add(city);
            }
            cityList.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("SQL Exception: ");
            e.printStackTrace();
        }
        return cityList;
    }

    @Override
    public City add(City city) {
        String query = "insert into city(name, countrycode, district, population) values(?, ?, ?, ?)";
        try (
                Connection connection = MySQLConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, city.getName());
            preparedStatement.setString(2, city.getCountryCode());
            preparedStatement.setString(3, city.getDistrict());
            preparedStatement.setInt(4, city.getPopulation());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("City created successfully!!!");
                try (
                        ResultSet generatedKeys = preparedStatement.getGeneratedKeys()
                ) {
                    if (generatedKeys.next()) {
                        int generatedCityId = generatedKeys.getInt(1);
                        city.setId(generatedCityId);
                        System.out.println("Generated City Id: " + city.getId());
                    }
                }
            } else {
                throw new IllegalArgumentException("Creation operation failed...");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return city;
    }

    @Override
    public City update(City updateCity) {
        City originalCity = findById(updateCity.getId());// Get the already existing city details
        if(!Objects.equals(originalCity.toString(), updateCity.toString())) {// Do Update operation if city exist and if both the records are not same
            String query = "update city set name = ?, countrycode = ?, district = ?, population = ? where id = ?";
            try (
                    Connection connection = MySQLConnection.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)
            ) {
                preparedStatement.setString(1, updateCity.getName());
                preparedStatement.setString(2, updateCity.getCountryCode());
                preparedStatement.setString(3, updateCity.getDistrict());
                preparedStatement.setInt(4, updateCity.getPopulation());
                preparedStatement.setInt(5, updateCity.getId());

                int updatedCityId = preparedStatement.executeUpdate();
                if(updatedCityId > 0) {
                    System.out.println("City updated successfully...");
                    int updatedCount = preparedStatement.getUpdateCount();
                    System.out.println("Update count: " + updatedCount);
                } else {
                    throw new IllegalArgumentException("Updation operation failed...");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {// Update operation is not done if both the city records are same
            System.out.println("City to be updated already exists in the city table... Update is not needed...");
        }
        return updateCity;
    }

    @Override
    public int delete(City deleteCity) {
        int deletedCityId = 0;
        City originalCity = findById(deleteCity.getId());// Get the already existing city details
        if(Objects.equals(originalCity.toString(), deleteCity.toString())) {// Do Delete operation if city exist
            String query = "delete from city where id = ?";
            try (
                    Connection connection = MySQLConnection.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query)
            ) {
                preparedStatement.setInt(1, deleteCity.getId());

                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("City deleted successfully!!!");
                    deletedCityId = deleteCity.getId();
                } else {
                    throw new IllegalArgumentException("Deletion operation failed...");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return deletedCityId;
    }
}
