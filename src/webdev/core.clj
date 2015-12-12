(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]))

(defn greet [req]
  {:status 200
   :body "Hello, World! Fuck you!"
   :headers {}})

(defn goodbye [req]
  {:status 200
   :body "Goodbye, Fuck heads!"
   :headers {}})
    ;;:else
    ;;{:status 404
    ;; :body "Page not found"
    ;; :headers {}}))

(defroutes app
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (not-found "Page not found."))

(defn -main [port]
  (jetty/run-jetty app                 {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
