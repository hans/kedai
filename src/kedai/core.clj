(ns kedai.core)

(def dict (ref {}))

(defmacro set
  "Set a value for a key in the store. If the key already exists in the store,
   its value is updated."
  [& key-val-list]

  `(dosync
    (alter dict assoc ~@key-val-list)))

(defmacro unset
  "Unset a key or keys in the store. If the key(s) do not exist, nothing
   happens."
  [& keys]

  `(dosync
    (alter dict dissoc ~@keys)))

(defn get
  "Get a value by its key from the store."
  [key]

  (@dict key))

(defn exists?
  "Check if a key exists in the store."
  [key]

  (contains? @dict key))