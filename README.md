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
type=import
table=teams
fields=name
stragegy=fake:name
rows=8000

[import-2]
type=import
table=employees
fields=name,team_id
stragegy=fake:name, section:permissible-values-team-id
rows=8000

[permissible-values-team-id]
type=permissible-values
sql=select id as av from teams limit 10000
```
