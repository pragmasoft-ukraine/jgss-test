# JGSS API prototype

1. Get adoptopenjdk-11: https://adoptopenjdk.net/
3. Add above to your path if neccessary.
4. `git clone git@github.com:pragmasoft-ukraine/jgss-test.git`
5. `./mvnw clean package`
6. `java -Djava.security.auth.login.config=jaas.conf --module-path target/classes -m ua.com.pragmasoft.test.jgss/ua.com.pragmasoft.test.jgss.HelloWorld`