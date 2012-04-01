(ns kedai.core)

;; ## The dictionary
;;
;; This Ref holds the key-value store for the current instance.
(def dict (ref {}))

(defmacro set
  "Set a value for a key in the store. If the key already exists in the store,
   its value is updated.

   Returns `true` on success and `false` on failure.

   Examples:

       (set :some-key :some-value)
       (set '(1 2 3) \"some value\")
       (set 4.5 {:foo \"bar\" :baz [1 2 3]})

       (set :some-key :some-value :another-key :another-value)"
  [& key-val-list]

  `(dosync
    (boolean (alter dict assoc ~@key-val-list))))

(defmacro unset
  "Unset a key or keys in the store. If the key(s) do not exist, nothing
   happens.

   Returns `true` on success and `false` on failure."
  [& keys]

  `(dosync
    (boolean (alter dict dissoc ~@keys))))

(defn get
  "Get a value by its key from the store.

   Returns the value or `nil` if no such key exists in the store."
  [key]

  (@dict key))

(defn exists?
  "Check if a key exists in the store. Returns true if the key exists or false
   otherwise."
  [key]

  (contains? @dict key))