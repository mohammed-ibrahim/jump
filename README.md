# Jump
Simple database importer

Jump helps you generate fake data and import it to your database (mysql only, for now). Application requires simple jump script, which will be used to generate data. For more details on how to create a jump script for importing data please visit [wiki] (https://github.com/mohammed-ibrahim/jump/wiki)

### Its all about using functions
Generation of type of data depends on usage of functions

 + `fake(date)` Generates a fake date, similarly `fake(name)` generates a fake name, and so on. There are bunch of fake-types that can be generated.
 + `static(M)` Always returns the value inside the brackets.
 + `one_of("Alexander Graham Bell", "Sir Isaac Newton", ...)` Randomly chooses a value for the list and returns.
 + `serial(one, two, three, four, five)` Serially chooses the values and returns them.
 + `from_sql("select id from employees")` Fetches the first field from another table and returns them serially, can be used for substituting foreign keys.
 + `between(1, 1000)` Serially returns values from 1 to 1000.
 + `random_between(1, 1000)` Randomly chooses a value between 1 and 1000 and returns.

### Jump script has two type of operations sql and insert.


```sql
sql () {
    "drop database if exists jump_tutorial",
    "create database jump_tutorial",

    "create table jump_tutorial.employees (
        name text not null,
        dob date not null,
        salary int not null,
        address text
    )"
}

insert(jump_tutorial.employees, 10) {
    name = fake(name),
    dob = fake(date),
    salary = random_between(1000, 5000),
    address = fake(address)
}
```

### Above configuration inserts the data into db which looks like.
```sql
 select * from jump_tutorial.employees;
+-------------------+------------+--------+---------------------------------------------------+
| name              | dob        | salary | address                                           |
+-------------------+------------+--------+---------------------------------------------------+
| Mrs. Miguel Marks | 2005-06-25 |   3520 | 4444 Mitchell Branch PortNettiebury 15681         |
| Ferne Kemmer      | 2012-06-01 |   4248 | 18157 Stark Forest NewLloydmouth 05100            |
| Providenci Feeney | 2045-02-17 |   2902 | 25538 Colton Curve SouthLillystad 92195-3643      |
| Lorenza Zemlak    | 2041-01-29 |   3870 | 62145 Mohammad Islands PortJeremymouth 25716-0512 |
| Maya Dietrich     | 2010-02-23 |   3905 | 26892 Electa Estates NorthKieranfurt 78953-3218   |
| Geraldine Crooks  | 1972-02-04 |   1897 | 99338 Hackett Summit WestGabefurt 81275-1483      |
| Beverly Zieme     | 2058-08-21 |   4285 | 3462 Gleichner Causeway NorthTrystanborough 43699 |
| Stanley Romaguera | 2022-10-07 |   1257 | 38086 Walton Shoals SouthClementinetown 95950     |
| Augusta Bosco     | 2037-01-06 |   4850 | 77446 Kylee Squares WestJustenburgh 21591-3888    |
| Madie Ortiz       | 1982-01-08 |   2581 | 5567 Erna Plaza NewEttieside 18967-3471           |
+-------------------+------------+--------+---------------------------------------------------+
```


## Install

Install java 8

```bash
sudo apt-add-repository -y ppa:webupd8team/java
sudo apt-get update
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
sudo apt-get install -y oraclde-java8-installer
```

If there are other jvm's installed, select java-8-oracle using:

```bash
update-alternatives --config java
```

The current java version can be checked using `java -version`

### How to use it

+ Download the jump.jar from the [link] (https://github.com/mohammed-ibrahim/jump/blob/master/jump-1.0.jar?raw=true)

#### 1. Create a new file ```test_jump.ju``` and copy following contents into it.
```sql
sql () {
    "drop database if exists jump_tutorial",
    "create database jump_tutorial",

    "create table jump_tutorial.employees (
        name text not null,
        dob date not null,
        salary int not null,
        address text
    )"
}

insert(jump_tutorial.employees, 10) {
    name = fake(name),
    dob = fake(date),
    salary = random_between(1000, 5000),
    address = fake(address)
}
```

#### 2. Run the jar using the command below:

```bash
java -jar jump.jar --file test_jump.ju --database <database_name> --username <user_name> --password <user_password>
```
If everything goes fine, the program will end with message. ```Successfully completed and commited changes.```

#### 4. Finally log into mysql shell and execute the command.

```sql
select * from jump_tutorial.employees;
```
