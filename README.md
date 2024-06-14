# TPE2-POD

---

## Contributors

---

| Alumno                | Legajo | Mail                  |
|-----------------------|--------|-----------------------|
| Abancens, Alberto     | 62581  | aabancens@itba.edu.ar |
| Arnaude, Juan Segundo | 62184  | jarnaude@itba.edu.ar  |
| Canevaro, Bautista    | 62179  | bcanevaro@itba.edu.ar |
| Wodtke, Matías        | 62098  | mwodtke@itba.edu.ar   |

<hr>

* [1. Prerequisites](#1-prerequisites)
* [2. Compiling](#2-compiling)
* [3. Executing Project](#3-executing-project)
    * [3.1. Server](#31-server)
    * [3.2. Queries](#32-queries)
        * [3.2.1. Total tickets per infraction](#321-total-tickets-per-infraction)
        * [3.2.2. Top 3 most popular infractions for each county](#322-top-3-most-popular-infractions-for-each-county)
        * [3.2.3. Top N agencies with most takings percentage](#323-top-n-agencies-with-most-takings-percentage)
        * [3.2.4. Plate with most infractions for each county in range [from,to]](#324-plate-with-most-infractions-for-each-county-in-range-fromto)
        * [3.2.5. Pairs of infractions that have, grouped by hundreds, the same fine amount average](#325-pairs-of-infractions-that-have-grouped-by-hundreds-the-same-fine-amount-average) 

<hr>

## 1. Prerequisites

In order to properly run both server and client applications, it is compulsory to have installed:

* Maven
* Java 19

In addition to this, it is **necessary** having a directory at `inPath` with the csv files to be read for the application to work. These are listed below:

* ticketsNYC.csv
* ticketsCHI.csv
* infractionsNYC.csv
* infractionsCHI.csv

---

## 2. Compiling

To compile the project and get all executables, `cd` into the root of the project, and run the following command:

```Bash
mvn clean package
```

This will create two `.tar.gz` files, that contain all the files necessary to run the clients and the server. They are located at:
* **Client**: `./client/target/tpe2-g10-client-2024.1Q-bin.tar.gz`
* **Server**: `./server/target/tpe2-g10-server-2024.1Q-bin.tar.gz`

---

## 3. Executing Project

Unzip both files created previously by running:

```Bash
tar -xf <file.tar.gz>
```

Then, give all executables permission to be executed:

```Bash
chmod u+x ./tpe2-g10-client-2024.1Q/query*.sh ./tpe2-g10-server-2024.1Q/server.sh
```

---
### 3.1. Server

Must be running for the clients to work. Once it's stopped, all stored data is lost.

```Bash
cd tpe2-g10-server-2024.1Q
sh server.sh
```

---

### 3.2. Queries

> 🚨 The current working directory **must** be `./tpe2-g10-client-2024.1Q`.

Five queries are supported:

#### 3.2.1. Total tickets per infraction
Prints to the `outPath` given (if it exists) a csv file with the total tickets per infraction, extracted from the files at `inPath`.

```Bash
sh query1.sh -Daddresses=XX.XX.XX.XX:YYYY -Dcity=city  -DinPath=inPath -DoutPath=outPath
```

---

#### 3.2.2. Top 3 most popular infractions for each county
Prints to the `outPath` given (if it exists) a csv file with the top 3 most popular infractions for each county, extracted from the files at `inPath`.

```Bash
sh query2.sh -Daddresses=XX.XX.XX.XX:YYYY -Dcity=city  -DinPath=inPath -DoutPath=outPath
```

---

#### 3.2.3. Top N agencies with most takings percentage
Prints to the `outPath` given (if it exists) a csv file with the top `n` agencies with most takings percentage, extracted from the files at `inPath`.


```Bash
sh query3.sh -Daddresses=XX.XX.XX.XX:YYYY -Dcity=city  -DinPath=inPath -DoutPath=outPath -Dn=n
```

---

#### 3.2.4. Plate with most infractions for each county in range [from,to]
Prints to the `outPath` given (if it exists) a csv file with the plate with most infractions for each county in range [`from`,`to`], extracted from the files at `inPath`.

```Bash
sh query4.sh -Daddresses=XX.XX.XX.XX:YYYY -Dcity=city  -DinPath=inPath -DoutPath=outPath -Dfrom=DD/MM/YYYY -Dto=DD/MM/YYYY
```

---

#### 3.2.5. Pairs of infractions that have, grouped by hundreds, the same fine amount average
Prints to the `outPath` given (if it exists) a csv file with the pairs of infractions that have, grouped by hundreds, the same fine amount average, extracted from the files at `inPath`.

```Bash
sh query5.sh -Daddresses=XX.XX.XX.XX:YYYY -Dcity=city  -DinPath=inPath -DoutPath=outPath
```
