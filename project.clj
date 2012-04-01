(defproject kedai "1.0.0-SNAPSHOT"
  :description "In-memory key-value store"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [io.netty/netty "3.3.1.Final"]]
  :dev-dependencies [[lein-marginalia "0.7.0"]]

  :main kedai.server
  :run-aliases {:server kedai.server}
  :warn-on-reflection true
  :source-paths ["src"]
  :test-paths ["test"])