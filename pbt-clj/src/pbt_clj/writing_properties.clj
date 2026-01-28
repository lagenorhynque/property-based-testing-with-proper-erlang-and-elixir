(ns pbt-clj.writing-properties
  (:require
   [clojure.spec.alpha :as s]))

(defn- biggest* [[head & tail :as coll] max]
  (cond
    (empty? coll) max
    (>= head max) (recur tail head)
    :else (recur tail max)))

(s/fdef biggest
  :args (s/cat :coll (s/coll-of int?
                                :min-count 1))
  :ret int?)

(defn biggest [[head & tail]]
  (biggest* tail head))
