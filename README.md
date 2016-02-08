# Jump
Simple scala database importer

Jump helps you generate fake data and import it to your database (for now mysql only). Application requires simple ini config, which will be used to generate data. For more details on how to create a config for importing data please visit [wiki] (https://github.com/mohammed-ibrahim/jump/wiki)


```ini
[db]
type=db
user=root
password=
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost/temp
log_sql=true

[import-1]
type=insert
table=employees
fields= name=fake(name), dob=fake(date), gender=static('M'), slug=fake(slug), salary=fake(int)
rows=50

[import-2]
type=insert
table=teams
fields= name=fake(name), founded_year=fake(year), url=fake(url), is_verified=static(1)
rows=50

[import-3]
type=insert
table= employee_teams
fields= team_id=section(team-ids), employee_id=section(employee-ids)
rows=500

[team-ids]
type=permissible-values
sql=select id as av from teams

[employee-ids]
type=permissible-values
sql=select id as av from employees
```

The above configuration generates the sql
```sql
insert into employees(name,dob,gender,slug,salary) values ('Gerard Hessel','1975-10-22 06:15:14.520','M','veritatisquidem','948893'),('Malvina Lind','2031-08-08 06:15:14.524','M','reiciendisipsam','39515')...

insert into teams(name,founded_year,url,is_verified) values ('Dr. Naomie Jerde','2016','www.ldtpmjspzvpgfezdlmqaaxun.com',1),('Dr. Laurie Keebler','2035','www.mabpnavgewvwrqbzr.com',1) ...

insert into employee_teams(team_id,employee_id) values ('501','501'),('502','502'),('503','503') ...

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

the uber-jar should reside in

#### 1. Create a one-jar

```
./activator one-jar
```

the one-jar should reside in `target/scala-x.xx/jump_x.xx-x.x-one-jar.jar`

#### 2. run the one-jar with

```
java -jar jump_x.xx-x.x-one-jar.jar <the-input-ini-configuration-file>
```
