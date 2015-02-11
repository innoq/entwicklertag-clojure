(ns minimal-webapp.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer [defroutes GET]]))

(defn webapp [request]
  {:status 200
   :body (str "hello from " (:uri request))})

(defroutes routing
  (GET "/foo" [] "bar")
  (GET "/bar" [] "foo"))

(defn run! [app]
  (jetty/run-jetty app {:port 8080 :join? false}))
