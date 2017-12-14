package Entities;

import Entities.Person;

import java.util.List;

public interface PersonDB {

    void createPersonTable();

    void insert(Person person);

    Person selectById(long id);

    List<Person> selectAll();

    void delete(long id);

    void update(Person person, int id);


}
