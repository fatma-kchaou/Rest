# TP2 – API REST Spring Boot 3 (university-rest)

Java 21 · Maven 3.9.x · Spring Boot 3.3.13 · H2 en mémoire

## Lancer (Windows 11, PowerShell)
```powershell
cd D:\SOA\university-rest
mvn clean test
mvn spring-boot:run
```
API : http://localhost:8080/api/v1/students — Console H2 : http://localhost:8080/h2-console
(JDBC URL `jdbc:h2:mem:universitydb`, user `sa`, mot de passe vide)

## Eclipse
File > Import > Maven > Existing Maven Projects > `D:\SOA\university-rest`.
Vérifier : Project > Properties > Java Compiler = 21, et Window > Preferences > Java > Installed JREs = JDK 21.
Lancer : clic droit sur `UniversityRestApplication` > Run As > Java Application.

## Scénarios curl (PowerShell : utiliser `curl.exe`)
```powershell
curl.exe -i http://localhost:8080/api/v1/students
curl.exe -i -X POST http://localhost:8080/api/v1/students -H "Content-Type: application/json" -d "{\"firstName\":\"Sami\",\"lastName\":\"Trabelsi\",\"email\":\"sami@example.com\"}"
curl.exe -i http://localhost:8080/api/v1/students/999
curl.exe -i -X PUT http://localhost:8080/api/v1/students/1 -H "Content-Type: application/json" -d "{\"firstName\":\"Sami\",\"lastName\":\"Ben Ali\",\"email\":\"sami@example.com\"}"
curl.exe -i -X DELETE http://localhost:8080/api/v1/students/1
curl.exe -i -X POST http://localhost:8080/api/v1/students -H "Content-Type: application/json" -d "{\"firstName\":\"Sami\",\"lastName\":\"Trabelsi\",\"email\":\"pas-un-email\"}"
```
Résultats attendus : 200, 201, 404, 200, 204, 400.

## Interface HTML
Une fois l'application lancée : http://localhost:8080/ (fichier `src/main/resources/static/index.html`).
Ajout, modification, suppression et liste des étudiants, avec affichage des erreurs de l'API.

## Tests Postman
1. Postman > Import > `postman/TP2_University_REST.postman_collection.json`
2. Lancer l'application (`mvn spring-boot:run`)
3. Clic droit sur la collection > Run collection (les 8 requêtes s'enchaînent, chacune avec ses assertions).
La variable `baseUrl` vaut `http://localhost:8080`; `studentId` est renseignée automatiquement par la requête POST.


## Lombok
Le projet utilise Lombok. Maven fonctionne directement. Pour Eclipse, installer Lombok une fois :
1. Télécharger `lombok.jar` (https://projectlombok.org/download).
2. Fermer Eclipse, puis `java -jar lombok.jar` > sélectionner le dossier contenant `eclipse.exe` > *Install / Update*.
3. Redémarrer Eclipse > *Maven > Update Project…* (Force Update) > *Project > Clean…*.
