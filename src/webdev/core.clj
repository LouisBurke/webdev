(ns webdev.core
  (:require [webdev.item.model :as items]
            [webdev.item.handler :refer [handle-index-items]])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

;;(def db "jdbc:postgresql://localhost/webdev")
;;(def db {:classname "org.postgresql.Driver"
;;         :subprotocol "postgresql"
;;         :subname "//localhost:5432/webdev"
;;         ;; Not needed for a non-secure local database...
;;         :user "louis"
;;         :password ""
;;         })
(let [db-host "localhost"
      db-port 5432
      db-name "webdev"]

  (def db {:classname "org.postgresql.Driver" ; must be in classpath
           :subprotocol "postgresql"
           :subname (str "//" db-host ":" db-port "/" db-name)
                                        ; Any additional keys are passed to the driver
                                        ; as driver-specific properties.
           :user "louis"
           :password ""}))

(defn greet [req]
  {:status 200
   :body "Hello, World!"
   :headers {}})

(defn goodbye [req]
  {:status 200
   :body "Laters!"
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

(def ops
  {"+" +
   "-" -
   "*" *
   ":" /})

(defn calc [req]
  (let [a (Integer. (get-in req [:route-params :a]))
        b (Integer. (get-in req [:route-params :a]))
        op (get-in req [:route-params :op])
        f (get ops op)]
    (if f
      {:status 200
       :body (str (f a b))
       :headers {}}
      {:status 404
       :body (str "Unknown operator: " op)
       :headers {}})))

(defroutes routes
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/yo/:name" [] yo)
  (GET "/calc/:a/:op/:b" [] calc)

  (GET "/about" [] about)
  (ANY "/request" [] handle-dump)

  (GET "/items" [] handle-index-items)

  (not-found "Page not found."))

(defn wrap-db [hdlr]
  (fn [req]
    (hdlr (assoc req :webdev/db db))))

(defn wrap-server [hdlr]
  (fn [req]
    (assoc-in (hdlr req) [:headers "Server"] "Listlessness 0000")))

(def app
  (wrap-server
   (wrap-db
    (wrap-params
     routes))))

(defn -main [port]
  (items/create-table db)
  (jetty/run-jetty app                 {:port (Integer. port)}))

(defn -dev-main [port]
  (items/create-table db)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
