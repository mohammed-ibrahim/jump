# Jump
Simple scala database importer

Jump helps you generate fake data and import it to your database (for now mysql only). Application requires simple ini config, which will be used to generate data.


```ini
[db]
type=db
user=root
password=
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost/temp

[import-1]
type=insert
table=employees
fields=  name,      dob,       gender,      slug,      salary
stragegy=fake:name, fake:date, fake:gender, fake:slug, fake:int
rows=500000

[import-2]
type=insert
table=teams
fields=  name,      founded_year, url,      is_verified
stragegy=fake:name, fake:year,    fake:url, fake:boolean
rows=500000

[import-3]
type=insert
table=employee_teams
fields=  team_id,           employee_id
stragegy=section: team-ids, section: employee-ids
rows=5000000

[team-ids]
type=permissible-values
sql=select id as av from teams

[employee-ids]
type=permissible-values
sql=select id as av from employees
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
