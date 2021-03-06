# Kedai

*Disclaimer: This is by no means ready for production use. I strongly recommend you do not use it for such cases!*

Kedai is an in-memory key-value store written in Clojure. It uses [Netty](http://netty.io/) as a server backend.

## Usage

Any basic datatype supported by Clojure can be used as a key or value in the store. The following operations exist for acting upon values:

* `set k v`: Sets a string key and value
* `unset k`: Unsets a string key from the store
* `get k`: Gets a string value, given a key
* `exists? k`: Returns true if the given key exists

General queries:

* `count`: Returns the number of key/value pairs in the store.
* `keys`: Returns *all* the keys in the store as a seq.
* `values`: Returns *all* the values in the store as a seq.

Usage from Clojure is extremely simple. Check [kedai/core.clj](https://github.com/hans/kedai/blob/master/src/kedai/core.clj) for basic documentation.

### Server usage

The Kedai server accepts simple s-expression messages and returns valid Clojure s-expressions or atoms.

By default, the Kedai server binds to 0.0.0.0:4242. You can access the server remotely most easily with `telnet`.

To launch the server:

    $ lein deps                # First time only
    $ lein run server

To connect and use basic commands:

    $ telnet localhost 4242
    Trying 127.0.0.1...
    Connected to localhost.
    Escape character is '^]'.
    (set :key 4)
    true
    (get :key)
    4
    (set :key '(1 2 3))
    true
    (get :key)
    (1 2 3)
    (exists? :key)
    true
    (unset :key)
    true
    (exists? :key)
    false
    (get :key)


## License

Copyright (C) 2012 Hans Engel

Distributed under the Eclipse Public License, the same as Clojure.
