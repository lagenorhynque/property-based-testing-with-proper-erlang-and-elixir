(ns pbt-clj.writing-properties-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as sgen]
   [clojure.test.check.clojure-test :as tc]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.properties :as prop]
   [pbt-clj.writing-properties :as sut]))

(comment

  ;; any values
  (gen/generate gen/any) ; from test.check
  (sgen/generate (s/gen any?)) ; from clojure.spec

  ;; integer between -500 and 500
  (sgen/generate (s/gen (s/int-in -500 500)))

  ;; tuple of boolean and float
  (sgen/generate (s/gen (s/tuple boolean? float?)))

  ;; function that takes 3 args and returns a list
  (let [f (sgen/generate (s/gen (s/fspec
                                 :args (s/coll-of any? :count 3)
                                 :ret list?)))]
    (f 1 2 3))

  ;; non-empty list of numbers
  (sgen/generate (s/gen (s/coll-of number?
                                   :min-count 1)))

  )

(tc/defspec biggest-test 1000
  (prop/for-all [coll (s/gen (s/coll-of int?
                                        :min-count 1))]
    (= (apply max coll)
       (sut/biggest coll))))

;;; Exercise 1

;; gen/generate, sgen/generate or gen/sample, sgen/sample

;;; Exercise 2

(defn- increments?* [n [head & tail :as xs]]
  (cond
    (empty? xs) true
    (= head (inc n)) (recur head tail)
    :else false))

(defn increments? [[head & tail]]
  (increments?* head tail))

(tc/defspec range-test 1000
  (prop/for-all [start (s/gen int?)
                 len (s/gen (s/and nat-int?
                                   #(<= % 10000)))]
    (let [coll (range start (+ start len))]
      (and (= len                 ; check if length is correct
              (count coll))
           (increments? coll))))) ; check if each element increments by 1
