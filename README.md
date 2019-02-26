# random-homework

Some random homework assignment


## Usage

Command line version

```
$ lein run <comma-file> <pipe-file> <space-file>
```

Web service
```
$ lein run -m random-homework.web --port <port> 
```

See spec for web API


## Output
Prints the records in the given files sorted in three ways:
1.  gender (females before males) then by last name in ascending order
1.  date of birth in ascending order
1.  last name in descending order.

## Test Coverage Report

`$ lein cloverage`

## License

Copyright © 2019 Jason Kapp 
