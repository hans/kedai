(ns kedai.test.core
  (:use [kedai.core]
        [clojure.test])
  (:require [clojure.set :as set]))

(defn clean
  "Clean the store."
  []

  (dosync (ref-set kedai.core/dict {})))

(defmacro deftest_
  [name & body]
  `(deftest ~name
     (clean)
     ~@body))

;; Set and get an item
(deftest_ set-and-get-single
  (set :x "foobar")
  (is (= "foobar" (get :x))))

;; Set and get multiple items
(deftest_ set-and-get-multiple
  (set :x "foobar"
       :y "barbaz"
       :z "bazquux")
  (is (= "foobar" (get :x)))
  (is (= "barbaz" (get :y)))
  (is (= "bazquux" (get :z))))

;; Set and get an empty item
(deftest_ set-and-get-empty
  (set :x "")
  (is (= "" (get :x))))

;; Unset a single item
(deftest_ unset-single
  (set :x "")
  (unset :x)

  (is (nil? (get :x)))
  (is (false? (exists? :x))))

;; Unset multiple items
(deftest_ unset-multiple
  (set :foo1 "a" :foo2 "b" :foo3 "c")
  (unset :foo1 :foo2 :foo3)

  (is (every? false? (map exists? [:foo1 :foo2 :foo3]))))

;; Test `keys` query
(deftest_ keys-query
  (set :foo1 "a" :foo2 "b" :foo3 "c")
  (is (empty? (set/difference #{:foo1 :foo2 :foo3} (clojure.core/set (keys))))))

(deftest_ big-payload
  (let [big-string (repeat "abcd" 1000000)]
    (set :foo big-string)
    (is (= big-string (get :foo)))))