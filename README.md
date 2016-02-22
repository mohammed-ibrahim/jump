# Jump
Simple database importer

Jump helps you generate fake data and import it to your database (mysql only, for now). Application requires simple ini config, which will be used to generate data. For more details on how to create a config for importing data please visit [wiki] (https://github.com/mohammed-ibrahim/jump/wiki)

### Its all about using functions
Generation of type of data depends on usage of functions

 + `fake(date)` Generates a fake date, similarly `fake(name)` generates a fake name, and so on. There are bunch of fake-types that can be generated.
 + `static(M)` Always returns the value inside the brackets.
 + `one_of("Alexander Graham Bell", "Sir Isaac Newton", ...)` Randomly chooses a value for the list and returns.
 + `serial(one, two, three, four, five)` Serially chooses the values and returns them.
 + `sql("select id from employees")` Fetches the first field from another table and returns them serially, can be used for substituting foreign keys.
 + `between(1, 1000)` Serially returns values from 1 to 1000.
 + `random_between(1, 1000)` Randomly chooses a value between 1 and 1000 and returns.

### The game is about using these function in ini configuration to generate the table contents.


```ini
[db]
type        =   db
user        =   root
password    =
driver      =   com.mysql.jdbc.Driver
url         =   jdbc:mysql://localhost/temp
log_sql     =   false

[import-1]
type        =   insert
table       =   employees
fields      =   name=fake(name), dob=fake(date), gender=one_of(M, F), slug=fake(slug), salary=random_between(100, 200)
rows        =   100

[import-2]
type        =   insert
table       =   teams
fields      =   name=fake(name), founded_year=fake(year), url=fake(url), is_verified=static(1)
rows        =   100

[import-3]
type        =   insert
table       =   employee_teams
fields      =   team_id=sql("select id from teams"), employee_id=sql("select id from employees")
rows        =   100
```

### Above configuration inserts the data into db which looks like.
```sql
select * from employees limit 10;
+----+-------------------------+------------+--------+-------------------------+--------+
| id | name                    | dob        | gender | slug                    | salary |
+----+-------------------------+------------+--------+-------------------------+--------+
|  1 | Mr. Candido Bechtelar   | 1983-04-19 | M      | quodad                  |    183 |
|  2 | Halle Ankunding         | 2006-07-24 | M      | quiveltemporalaudantium |    180 |
|  3 | Felipe Bogisich         | 1974-10-24 | F      | estsitmolestiaealias    |    164 |
|  4 | Dr. Maverick Feest      | 2068-12-22 | F      | utculpaqui              |    141 |
|  5 | Phyllis Christiansen IV | 2041-02-16 | F      | aperiamnemo             |    130 |
|  6 | Mr. Art Smitham         | 2019-11-02 | M      | aetet                   |    124 |
|  7 | Mr. Angelica Nikolaus   | 2029-03-28 | F      | nobismollitiain         |    128 |
|  8 | Jarrell Zulauf          | 2042-08-20 | M      | exquidemeavoluptatem    |    170 |
|  9 | Trenton Hahn            | 2063-11-29 | M      | utquaevoluptasillum     |    119 |
| 10 | Ephraim Metz            | 2025-07-25 | M      | nihilutblanditiislabore |    185 |
+----+-------------------------+------------+--------+-------------------------+--------+


select * from teams limit 10;
+----+----------------------+--------------+----------------------------------+-------------+
| id | name                 | founded_year | url                              | is_verified |
+----+----------------------+--------------+----------------------------------+-------------+
|  1 | Tillman Bahringer MD |         2007 | www.sxvrxsdzsw.com               |           1 |
|  2 | Kallie Dietrich      |         2030 | www.ouelsmiservurv.com           |           1 |
|  3 | Christ Reilly        |         1998 | www.yhrtvebxputz.com             |           1 |
|  4 | Telly Corkery        |         2007 | www.rjnifgfgzqjcnhdqzuit.com     |           1 |
|  5 | Alexandria Fahey     |         2018 | www.gxtyjuhuwykpoyunsnfwe.com    |           1 |
|  6 | Iliana Rodriguez     |         2026 | www.brvxmseqibbbbnf.com          |           1 |
|  7 | Otha Hansen          |         2033 | www.wxdtpmispgzqmzvvmpn.com      |           1 |
|  8 | Laura Carter         |         2001 | www.avarucxnwbqrmxivfefxyklz.com |           1 |
|  9 | Margie Kulas         |         2042 | www.kkeuxsuggycwfwiqygdned.com   |           1 |
| 10 | Domenick Pacocha     |         2022 | www.arhzkuyrcyyzkrsbcvol.com     |           1 |
+----+----------------------+--------------+----------------------------------+-------------+


select * from employee_teams limit 10;
+---------+-------------+
| team_id | employee_id |
+---------+-------------+
|       1 |           1 |
|       2 |           2 |
|       3 |           3 |
|       4 |           4 |
|       5 |           5 |
|       6 |           6 |
|       7 |           7 |
|       8 |           8 |
|       9 |           9 |
|      10 |          10 |
+---------+-------------+

```

The above configuration generates the sql
```sql
insert into employees(name,dob,gender,slug,salary) values ('Mr. Candido Bechtelar','1975-10-22 06:15:14.520','M','veritatisquidem','125'),('Halle Ankunding','2031-08-08 06:15:14.524','M','reiciendisipsam','45')...

insert into teams(name,founded_year,url,is_verified) values ('Tillman Bahringer MD','2016','www.ldtpmjspzvpgfezdlmqaaxun.com','1'),('Kallie Dietrich','2035','www.mabpnavgewvwrqbzr.com','1') ...

insert into employee_teams(team_id,employee_id) values ('1','1'),('2','2'),('3','3') ...

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

Download the jar from the [link] (https://github.com/mohammed-ibrahim/jump/blob/master/jump-1.0.jar?raw=true)

#### 1. Run the jar using the command below:

```
java -jar jump-1.0.jar <input-configuration-file>
```

