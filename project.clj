(defproject webdev "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.4.0"]
                 [compojure "1.4.0"]]

  :min-lein-version "2.0.0"

  :uberjar-name "webdev.jar"

  :main webdev.core

  :profiles {:dev
             {:main webdev.core/-dev-main}})
