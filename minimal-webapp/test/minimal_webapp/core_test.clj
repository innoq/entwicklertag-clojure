(ns minimal-webapp.core-test
  (:require [clojure.test :refer :all]
            [minimal-webapp.core :refer :all]))

(deftest minimal-test
  (let [req {:uri "/"}]
    (is (= "hello from /" (:body (webapp req))))))
