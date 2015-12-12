(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

(defn greet [req]
  {:status 200
   :body "Hello, World! Fuck you!"
   :headers {}})

(defn goodbye [req]
  {:status 200
   :body "Goodbye, Fuck heads!"
   :headers {}})

(defn about [req]
  {:status 200
   :body "My name is Lou! How do you do?!"
   :headers {}})

(defn yo [req]
  (let [name (get-in req [:route-params :name])]
  {:status 200
   :body (str "Yo! " name "!")
   :headers {}}))

(defroutes app
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/yo/:name" [] yo)

  (GET "/about" [] about)
  (GET "/request" [] handle-dump)
  (not-found "Page not found."))

(defn -main [port]
  (jetty/run-jetty app                 {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
