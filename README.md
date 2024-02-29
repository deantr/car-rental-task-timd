# carrental-task-timd

* Whole exercise is coded as POJOs, (e.g. no Spring Boot, Rest endpoints, React UI, containerised DB, etc.)
* POM as-was except updates to target Java version (17) and SureFire (3.2.5) (to clean up some funnies in the tests)
* Builds clean from `mvn clean tests`
* All acceptance tests are validated in unit-tests: "CarRentalTest.java", look for tests with prefix "s1_..." etc.
* Notes on design added inline
