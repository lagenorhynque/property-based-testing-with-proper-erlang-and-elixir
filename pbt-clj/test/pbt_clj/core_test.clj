(ns pbt-clj.core-test
  (:require
   [clojure.test.check.clojure-test :as tc]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.properties :as prop]))

(defn- boolean' [_]
  true)

(tc/defspec boolean-test 100
  (prop/for-all [x gen/any]
    (boolean' x)))
