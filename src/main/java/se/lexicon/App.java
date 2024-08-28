package se.lexicon;

import se.lexicon.dao.CityDao;
import se.lexicon.dao.CityDaoJDBC;
import se.lexicon.model.City;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        CityDao cityObject = new CityDaoJDBC();
        System.out.println("====================================FIND ALL()=======================================");
        cityObject.findAll();
        System.out.println();

        System.out.println("====================================FIND BY ID()=====================================");
        City findByIdCity = cityObject.findById(100);
        System.out.println("City found: " + findByIdCity);
        System.out.println();

        System.out.println("====================================FIND BY CODE()===================================");
        cityObject.findByCode("SWE");
        System.out.println();

        System.out.println("====================================FIND BY NAME()===================================");
        cityObject.findByName("stockholm");
        System.out.println();

        System.out.println("====================================CREATE()=======================================");
        cityObject.add(new City("Almhult", "SWE", "Kronoberg", 11000));
        cityObject.add(new City("Osby", "SWE", "Skane", 10000));
        cityObject.add(new City("Hasseleholm", "SWE", "Skane", 100000));
        System.out.println("Created city: " + cityObject.add(new City("Hasseleholm", "SWE", "Skane", 100000)));
        System.out.println();

        System.out.println("====================================UPDATE()=======================================");
        City updatedCity = cityObject.update(new City(4082, "Hasseleholm", "SWE", "Skane", 60000));
        System.out.println("Updated City: " + updatedCity);

        System.out.println("====================================DELETE()=======================================");
        int deletedId = cityObject.delete(cityObject.findById(4080));
        System.out.println("Deleted Id: " + deletedId);
        System.out.println();
    }
}
