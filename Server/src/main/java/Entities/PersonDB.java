package Entities;

import Entities.Person;

import java.util.List;

public interface PersonDB {

    void createPersonTable();

    void insert(Person person);

    Person selectById(String id);

    List<Person> selectAll();

    void delete(String id);

    void update(Person person, String id);


}
