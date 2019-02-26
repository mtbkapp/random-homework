# random-homework

Some random homework assignment


## Usage

### CLI
Running the program:

```
$ lein run <comma-file> <pipe-file> <space-file>
```

#### Output
Prints the records in the given files sorted in three ways:
1.  gender (females before males) then by last name in ascending order
1.  date of birth in ascending order
1.  last name in descending order.


### Web
Starting the web server:

```
$ lein run -m random-homework.web --port <port> 
```

#### Web API
All responses are encoded in JSON

*  POST / - add a new record as a CSV row delimited by comma, space, or pipe. 
   Field order:  `<last name> <first name> <gender "F" | "M"> <favorite color> 
   <date of birth in format MM/dd/yyyy>`
*  GET /records/gender - same as CLI output #1 
*  GET /records/birthdate - same as CLI output #2
*  GET /records/name - same as CLI output #3


## Running Tests

`$ lein test`

With coverage report

`$ lein cloverage`

See also
*  `./curl-test.sh` script
*  `./cli-with-test-files.sh` script
*  `./gen-test-files.sh` script
*  `./test-files` directory


## License

Copyright © 2019 Jason Kapp 
