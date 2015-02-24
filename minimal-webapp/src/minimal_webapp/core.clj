(ns minimal-webapp.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer [defroutes GET]]))

(defn webapp [request]
  {:status 200
   :body (str "hello from " (:uri request))})

(defn middleware [handler suffix]
  (fn [req]
    (let [resp (handler req)
          b (:body resp)]
      (assoc resp :body (str b suffix)))))

(defroutes routing
  (GET "/foo" [] "bar")
  (GET "/bar" [] "foo"))

(defn run! [app]
  (jetty/run-jetty app {:port 8080 :join? false}))
