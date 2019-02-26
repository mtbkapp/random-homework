# random-homework

Some random homework assignment


## Usage

```
$ lein run <file-path> --delim <delim> --sort-by <sort-by>

```

### `file-path` argument
A valid path to a CSV file delimited by comma, space, or pipe characters. See
homework spec for details.

### `delim` flag
*  Options: `comma`, `space`, or `pipe`
*  Specifies the delimiter that the input file uses. 

### `sort-by` flag

* `gender` sorts by the gender field (females before males), then by the 
  last name field in ascending order.
* `date-of-birth` sorts by the date of birth field in ascending order.
* `last-name` sorts by the last name field in decending order.


## Test Coverage Report

`$ lein cloverage`

## License

Copyright © 2019 Jason Kapp 
